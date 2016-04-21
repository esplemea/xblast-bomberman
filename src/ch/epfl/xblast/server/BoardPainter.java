package ch.epfl.xblast.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 21, 2016
 *
 */

public final class BoardPainter {
	private final Map<Block, BlockImage> palette;
	private final BlockImage shadow;

	/**
	 * Constructor for a BoardPainter
	 * 
	 * @param palette,
	 *            linking each kind of block to its image
	 * @param shadow,
	 *            the image used when a wall casts shadow on a free block
	 */
	public BoardPainter(Map<Block, BlockImage> palette, BlockImage shadow) {
		this.palette = Collections.unmodifiableMap(new HashMap<>(palette));
		this.shadow = shadow;
	}

	/**
	 * if the Block is FREE and its west neighbor casts shadow, it returns the image "shadow".</p>
	 * Otherwise, the normal image for the block.
	 * @param board
	 * @param position
	 * @return the byte used to identify the Block in a Cell of the Board
	 */
	public byte byteForCell(Board board, Cell position) {
		return (((board.blockAt(position) == Block.FREE) && board.blockAt(position.neighbor(Direction.W)).castsShadow())
				? (byte) shadow.ordinal() : (byte) palette.get(board.blockAt(position)).ordinal());
	}
}
