package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.RunLengthEncoder;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 26, 2016
 *
 */

public final class GameStateSerializer {
	/**
	 * This class can't be initialized
	 */
	private GameStateSerializer() {
	};

	/**
	 * serialize the whole game, in this order : blocks, bombs/blasts, players, ticks.
	 * @param painter
	 * @param game
	 * @return the encoded GameState
	 */
	public static List<Byte> serialize(BoardPainter painter, GameState game) {
		List<Byte> output = new ArrayList<>();
		for (Cell c : Cell.SPIRAL_ORDER) {
			output.add(painter.byteForCell(game.board(), c));
		}
		Set<Cell> blastedCells = game.blastedCells();
		for (Cell c : Cell.ROW_MAJOR_ORDER) {
			if (blastedCells.contains(c)) {
				output.add(ExplosionPainter.byteForBlast(blastedCells.contains(c.neighbor(Direction.N)),
						blastedCells.contains(c.neighbor(Direction.E)), blastedCells.contains(c.neighbor(Direction.S)),
						blastedCells.contains(c.neighbor(Direction.W))));
			} else if (game.bombedCells().containsKey(c)) {
				output.add(ExplosionPainter.byteForBomb(game.bombedCells().get(c)));
			} else {
				output.add(ExplosionPainter.BYTE_FOR_EMPTY);
			}
		}
		for(Player player : game.players()){
			output.add(PlayerPainter.byteForPlayer(game.ticks(), player));
		}
		output.add((byte)(Math.ceil(game.ticks()/2)));
		output = RunLengthEncoder.encode(output);
		return output;
	}
}
