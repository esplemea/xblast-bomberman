
package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.client.GameState.Player;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.BlockImage;

public final class GameStateDeserializer {
	private final static int BYTES_FOR_PLAYER = 16;
	private final static int BYTES_FOR_TIMER = 1;
	private final static int NOT_CODED_BYTES = BYTES_FOR_PLAYER + BYTES_FOR_TIMER;

	/**
	 * This object can't be initiate
	 */
	private GameStateDeserializer() {
	}

	public static GameState deserializeGameState(List<Byte> bytes) {
		List<Byte> decodedBytes = RunLengthEncoder.decode(bytes.subList(0, bytes.size() - NOT_CODED_BYTES));
		return new GameState(
				desearializePlayer(bytes.subList(decodedBytes.size(), decodedBytes.size() + BYTES_FOR_PLAYER)),
				deserializeBlocks(decodedBytes.subList(0, Cell.COUNT)),
				deserializeExplosions(decodedBytes.subList(Cell.COUNT, Cell.COUNT * 2)),
				deserializeScore(decodedBytes.subList(Cell.COUNT * 2, decodedBytes.size())), images);
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
		return null;
	}
}
