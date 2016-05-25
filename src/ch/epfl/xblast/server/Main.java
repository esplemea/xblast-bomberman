package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;
import ch.epfl.xblast.client.PlayerAction;

public class Main {

    private final static int DEFAULT_PLAYER_NMB = 1; //TODO
    public final static int PORT = 2016;

    public static void main(String[] args)
            throws IOException, InterruptedException {
        Map<SocketAddress, PlayerID> playersIP = new HashMap<>();
        int playerNumber = args.length == 0 ? DEFAULT_PLAYER_NMB
                : Integer.getInteger(args[0]);

        DatagramChannel channel = DatagramChannel
                .open(StandardProtocolFamily.INET);
        SocketAddress senderAddress;

        channel.bind(new InetSocketAddress(PORT));
        channel.configureBlocking(true);
        ByteBuffer buffer = ByteBuffer.allocate(1);
        
        while (playersIP.size() < playerNumber) {
            buffer.clear();
            senderAddress = channel.receive(buffer);
            buffer.flip();

            if (buffer.hasRemaining() && buffer.get() == PlayerAction.JOIN_GAME.ordinal()) {
                playersIP.putIfAbsent(senderAddress,
                        PlayerID.values()[playersIP.size()]);
            }
        
        }
        GameState gameState = Level.DEFAULT_LEVEL.getGameState();
        BoardPainter defaultBoard = Level.DEFAULT_LEVEL.getBoardPainter();
        List<Byte> serialisedGame;
        Map<PlayerID, Optional<Direction>> moveEvent = new HashMap<>();
        Set<PlayerID> bombDropEvent = new HashSet<>();
        long startingTime = System.nanoTime();

        channel.configureBlocking(false);
        while (!gameState.isGameOver()) {
            serialisedGame = GameStateSerializer.serialize(defaultBoard,
                    gameState);
            buffer = ByteBuffer.allocate(serialisedGame.size() + 1);
            buffer.put((byte) 0);
            for (byte b : serialisedGame)
                buffer.put(b);
            buffer.flip();

            for (Map.Entry<SocketAddress, PlayerID> entry : playersIP
                    .entrySet()) {
                buffer.put(0, (byte) entry.getValue().ordinal());
                channel.send(buffer, entry.getKey());
                buffer.rewind();
            }

            buffer = ByteBuffer.allocate(1);
            byte event;
            while (!((senderAddress = channel.receive(buffer)) == null)) {
                buffer.flip();
                event = buffer.get();
                switch (PlayerAction.values()[event]) {
                case DROP_BOMB:
                    if (!bombDropEvent.contains(playersIP.get(senderAddress)))
                        bombDropEvent.add(playersIP.get(senderAddress));
                    break;
                case MOVE_N:
                case MOVE_S:
                case MOVE_E:
                case MOVE_W:
                    moveEvent.put(playersIP.get(senderAddress),
                            Optional.of(Direction.values()[event - 1]));
                    break;
                case STOP:
                    moveEvent.put(playersIP.get(senderAddress),
                            Optional.empty());
                    break;
                default:
                    break;
                }
                buffer.clear();
            }
            gameState = gameState.next(moveEvent, bombDropEvent);
            moveEvent.clear();
            bombDropEvent.clear();

            long timeLeft = (int)(startingTime
                    + (gameState.ticks() + 1) * Ticks.TICK_NANOSECOND_DURATION- System.nanoTime());
            if (timeLeft > 0) {
                Thread.sleep( (timeLeft / Time.US_PER_S),
                        (int) (timeLeft % Time.US_PER_S));


            }
        }

    }

}
