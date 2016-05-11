
package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;
import ch.epfl.xblast.client.ImageCollection;

public final class GameStateDeserializer {

    /** 
     * This object can't be initiate
     */
    private GameStateDeserializer() {
    }

    public static GameState deserializeGameState(List<Byte> bytes) {
        List<Byte> blocksBytes = new ArrayList<>(
                bytes.subList(1, Byte.toUnsignedInt(bytes.get(0))));
        bytes.removeAll(bytes.subList(0, Byte.toUnsignedInt(bytes.get(0))));
        List<Byte> explosionsBytes = new ArrayList<>(
                bytes.subList(1, Byte.toUnsignedInt(bytes.get(0))));
        bytes.removeAll(bytes.subList(0, Byte.toUnsignedInt(bytes.get(0))));
        blocksBytes = RunLengthEncoder.decode(blocksBytes);
        explosionsBytes = RunLengthEncoder.decode(explosionsBytes);
        List<Byte> playerBytes = bytes.subList(0, bytes.size()-1);
        return new GameState(desearializePlayer(playerBytes),
                deserializeBlocks(blocksBytes),
                deserializeExplosions(explosionsBytes),
                deserializeScore(playerBytes),
                deserializeTime(bytes.get(bytes.size() - 1)));
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
        
        output.addAll(imagesForOnePlayer(bytes.get(0),0));
        output.addAll(imagesForOnePlayer(bytes.get(4),1));
        output.addAll(Collections.nCopies(8, images.image(12)));
        output.addAll(imagesForOnePlayer(bytes.get(8),2));
        output.addAll(imagesForOnePlayer(bytes.get(12),3));
        
        return output;
    }
    
    private static List<Image> imagesForOnePlayer(byte state, int ordinal) {
        List<Image> output = new ArrayList<>();
        ImageCollection images = new ImageCollection("score");
        
        if(!(Byte.toUnsignedInt(state)>0)) 
            output.add(images.image(ordinal*2));
        else
            output.add(images.image((ordinal*2)+1));
        
        output.add(images.image(10));
        output.add(images.image(11));
        
        return output;
    }

    private static List<Player> desearializePlayer(List<Byte> bytes) {
        List<Player> output = new ArrayList<>();
        ImageCollection images = new ImageCollection("player");
        for (int i = 0; i < bytes.size() / 4; ++i) {
            output.add(new Player(PlayerID.values()[i], bytes.get(4 * i),
                    new SubCell(bytes.get(4 * i + 1), bytes.get(4 * i + 2)),
                    images.image(bytes.get(4 * i + 3))));
        }
        return output;
    }

    private static List<Image> deserializeTime(Byte mbyte) {
        List<Image> output = new ArrayList<>();
        ImageCollection images = new ImageCollection("score");
        for (int i = 0; i < 60; ++i) {
            if (i < mbyte)
                output.add(images.image(21));
            else
                output.add(images.image(20));
        }
        return output;
    }
}
