package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;
import ch.epfl.xblast.client.ImageCollection;

public final class GameStateDeserializer {

	private static ImageCollection BLOCK_IMAGES = new ImageCollection("block");
	private static ImageCollection EXPLOSION_IMAGES = new ImageCollection("explosion");
	private static ImageCollection SCORE_IMAGES = new ImageCollection("score");
	private static ImageCollection PLAYER_IMAGES = new ImageCollection("player");

	/**
	 * This object can't be initiate
	 */
	private GameStateDeserializer() {
	}

	public static GameState deserializeGameState(List<Byte> bytes) {
		int start = 1, end = Byte.toUnsignedInt(bytes.get(0)) + 1;
		List<Byte> blocksBytes = bytes.subList(start, end);
		start = end + 1;
		end += Byte.toUnsignedInt(bytes.get(end)) + 1;
		List<Byte> explosionsBytes = bytes.subList(start, end);
		start = end;
		end += 16;
		List<Byte> playerBytes = bytes.subList(start, end);
		blocksBytes = RunLengthEncoder.decode(blocksBytes);
		explosionsBytes = RunLengthEncoder.decode(explosionsBytes);
		return new GameState(desearializePlayer(playerBytes), deserializeBlocks(blocksBytes),
				deserializeExplosions(explosionsBytes), deserializeScore(playerBytes), deserializeTime(bytes.get(end)));
	}

	private static List<Image> deserializeBlocks(List<Byte> bytes) {
		List<Image> output = new ArrayList<>(Collections.nCopies(Cell.COUNT, null));
		List<Image> spiralOrder = new ArrayList<>();
		for (Byte mbyte : bytes) {
			spiralOrder.add(BLOCK_IMAGES.image(mbyte));
		}
		int counter = 0;
		for (Cell c : Cell.SPIRAL_ORDER) {
			output.set(c.rowMajorIndex(), spiralOrder.get(counter++));
		}
		return output;
	}

	private static List<Image> deserializeExplosions(List<Byte> bytes) {
		List<Image> output = new ArrayList<>();
		for (Byte mbyte : bytes) {
			output.add(EXPLOSION_IMAGES.imageOrNull(mbyte));
		}
		return output;
	}

	private static List<Image> deserializeScore(List<Byte> bytes) {
		List<Image> output = new ArrayList<>();

		output.addAll(imagesForOnePlayer(bytes.get(0), 0));
		output.addAll(imagesForOnePlayer(bytes.get(4), 1));
		output.addAll(Collections.nCopies(8, SCORE_IMAGES.image(12)));
		output.addAll(imagesForOnePlayer(bytes.get(8), 2));
		output.addAll(imagesForOnePlayer(bytes.get(12), 3));

		return output;
	}

	private static List<Image> imagesForOnePlayer(byte state, int ordinal) {
		List<Image> output = new ArrayList<>();

		if (Byte.toUnsignedInt(state) > 0)
			output.add(SCORE_IMAGES.image(ordinal * 2));
		else
			output.add(SCORE_IMAGES.image((ordinal * 2) + 1));

		output.add(SCORE_IMAGES.image(10));
		output.add(SCORE_IMAGES.image(11));

		return output;
	}

	private static List<Player> desearializePlayer(List<Byte> bytes) {
		List<Player> output = new ArrayList<>();
		for (int i = 0; i < bytes.size() / 4; ++i) {
			output.add(new Player(PlayerID.values()[i], bytes.get(4 * i),
					new SubCell(Byte.toUnsignedInt(bytes.get(4 * i + 1)), Byte.toUnsignedInt(bytes.get(4 * i + 2))),
					PLAYER_IMAGES.imageOrNull(bytes.get(4 * i + 3))));
		}
		return output;
	}

	private static List<Image> deserializeTime(Byte mbyte) {
		List<Image> output = new ArrayList<>();
		for (int i = 0; i < 60; ++i) {
			if (i < mbyte)
				output.add(SCORE_IMAGES.image(21));
			else
				output.add(SCORE_IMAGES.image(20));
		}
		return output;
	}
}
