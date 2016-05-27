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
     * Serialize the whole game, in this order : blocks, bombs/blasts, players,
     * ticks.
     * 
     * @param painter
     * @param game
     * @return the encoded GameState
     */
    public static List<Byte> serialize(BoardPainter painter, GameState game) {
        List<Byte> output = new ArrayList<>();
        List<Byte> explosionOutput = new ArrayList<>();
        for (Cell c : Cell.SPIRAL_ORDER) {
            output.add(painter.byteForCell(game.board(), c));
        }
        output = RunLengthEncoder.encode(output);
        output.add(0, (byte) output.size());

        Set<Cell> blastedCells = game.blastedCells();
        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            if (game.board().blockAt(c).isFree() && blastedCells.contains(c)) {
                explosionOutput.add(ExplosionPainter.byteForBlast(
                        blastedCells.contains(c.neighbor(Direction.N)),
                        blastedCells.contains(c.neighbor(Direction.E)),
                        blastedCells.contains(c.neighbor(Direction.S)),
                        blastedCells.contains(c.neighbor(Direction.W))));
            } else if (game.bombedCells().containsKey(c)) {
                explosionOutput.add(ExplosionPainter
                        .byteForBomb(game.bombedCells().get(c)));
            } else {
                explosionOutput.add(ExplosionPainter.BYTE_FOR_EMPTY);
            }
        }
        explosionOutput = RunLengthEncoder.encode(explosionOutput);
        explosionOutput.add(0, (byte) explosionOutput.size());

        output.addAll(explosionOutput);

        for (Player player : game.players()) {
            output.add((byte) player.lives());
            output.add((byte) player.position().x());
            output.add((byte) player.position().y());
            output.add(PlayerPainter.byteForPlayer(game.ticks(), player));
        }

        output.add((byte) (Math.ceil(game.remainingTime() / 2)));

        return output;
    }
}
