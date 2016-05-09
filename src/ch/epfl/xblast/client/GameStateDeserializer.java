
package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;

public final class GameStateDeserializer {
    
    /*TODO En fait serialize est censé retourner d'abord la taille, il faut corriger.. (Enfin je crois)
    *La version compressée du plateau et des explosions est sérialisée comme suit :
    *     un octet non signé, qui donne la taille, en octets, de la séquence compressés qui suit,
    *     tous les octets signés composant la séquence compressée.
    */
    private final static int BYTES_FOR_PLAYER = 16;
    private final static int BYTES_FOR_TIMER = 1;
    private final static int NOT_CODED_BYTES = BYTES_FOR_PLAYER
            + BYTES_FOR_TIMER;

    /**
     * This object can't be initiate
     */
    private GameStateDeserializer() {
    }

    public static GameState deserializeGameState(List<Byte> bytes) {
        List<Byte> decodedBytes = RunLengthEncoder
                .decode(bytes.subList(0, bytes.size() - NOT_CODED_BYTES));
        return new GameState(
                desearializePlayer(bytes.subList(decodedBytes.size(),
                        decodedBytes.size() + BYTES_FOR_PLAYER)),
                deserializeBlocks(decodedBytes.subList(0, Cell.COUNT)),
                deserializeExplosions(
                        decodedBytes.subList(Cell.COUNT, Cell.COUNT * 2)),
                deserializeScore(decodedBytes.subList(Cell.COUNT * 2,
                        decodedBytes.size())),
                deserializeTime(bytes.subList(bytes.size() - BYTES_FOR_TIMER,
                        bytes.size())));
    }

    private static List<Image> deserializeBlocks(List<Byte> bytes) {
        List<Image> output = new ArrayList<>();
        ImageCollection images = new ImageCollection("block");
        for (Byte mbyte : bytes) {
            output.add(images.image(mbyte));
        }
        return output;
    }

    private static List<Image> deserializeExplosions(List<Byte> bytes) {
        List<Image> output = new ArrayList<>();
        ImageCollection images = new ImageCollection("explosion");
        for (Byte mbyte : bytes) {
            output.add(images.image(mbyte));
        }
        return output;
    }

    private static List<Image> deserializeScore(List<Byte> bytes) {
        List<Image> output = new ArrayList<>();
        ImageCollection images = new ImageCollection("score");
        for (Byte mbyte : bytes) {
            output.add(images.image(mbyte));
        }
        return output;
    }

    private static List<Player> desearializePlayer(List<Byte> bytes) {
        List<Player> output = new ArrayList<>();
        ImageCollection images = new ImageCollection("player");
        for (int i = 0; i < bytes.size() / 4; ++i) {
            output.add(new Player(PlayerID.values()[i], bytes.get(4*i),
                    new SubCell(bytes.get(4*i + 1), bytes.get(4*i + 2)),
                    images.image(bytes.get(4*i + 3))));
        }
        return output;
    }

    private static List<Image> deserializeTime(List<Byte> bytes) {
        List<Image> output = new ArrayList<>();
        ImageCollection images = new ImageCollection("score");
        for (Byte mbyte : bytes) {
            for (int i = 0; i < 60; ++i) {
                if (i < mbyte)
                    output.add(images.image(21));
                else
                    output.add(images.image(20));
            }
        }
        return output;
    }
}
