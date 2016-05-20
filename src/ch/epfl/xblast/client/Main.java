package ch.epfl.xblast.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.sun.glass.events.KeyEvent;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;

public class Main {
    private final static Map<Integer, PlayerAction> KB = new HashMap<>(createMap());
    private final static XblastComponent XBC = new XblastComponent();
    private final static int MAXIMUM_BYTES_SIZE = 500;

    /**
     * Initiate map linking each PlayerAction to a specific KeyEvent
     * 
     * @return
     */
    private static Map<Integer, PlayerAction> createMap() {
        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        return kb;
    }

    /*
     * Shows the window
     */
    private static void createUI() {
        JFrame game = new JFrame();
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.add(XBC);
        game.pack();
        game.setVisible(true);
    }

    public static void main(String[] args) throws IOException, InvocationTargetException, InterruptedException {
        System.out.println("princesse");
       InetSocketAddress address = new InetSocketAddress(args.length == 0 ? "localhost" : args[0],
                ch.epfl.xblast.server.Main.PORT);
        DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
        ByteBuffer buffer = ByteBuffer.allocate(1);
        System.out.println("princesse");
        SwingUtilities.invokeAndWait(() -> createUI());
        Consumer<PlayerAction> consumer = playerAction -> {
            try {
                    buffer.put((byte) playerAction.ordinal());
                    buffer.flip();
                    channel.send(buffer, address);
                    buffer.clear();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        };

        // Start a game window and the component
        
        XBC.addKeyListener(new KeyboardEventHandler(KB, consumer));
        XBC.requestFocus();

        // Send request to join the game and wait to receive data
        channel.configureBlocking(false);
        ByteBuffer byteNull = ByteBuffer.allocate(1), data = ByteBuffer.allocate(MAXIMUM_BYTES_SIZE);
        while (channel.receive(data) == null) {
            channel.send(byteNull, address);
            byteNull.rewind();
            Thread.sleep(Time.MS_PER_S);
        }

        channel.configureBlocking(true);
        PlayerID playerID;
        List<Byte> gameState = new ArrayList<>();
        while (channel.receive(data) != null) {
            data.flip();
            playerID = PlayerID.values()[data.get()];
            while (data.hasRemaining()) {
                gameState.add(data.get());
            }
            XBC.setGameState(GameStateDeserializer.deserializeGameState(gameState), playerID);
            data.clear();
        }
    }
}
