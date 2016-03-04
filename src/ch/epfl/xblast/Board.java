package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs108.Sq;

public final class Board {

	private final List<Sq<Block>> blocks;

	/**
	 * Construct a Board from the blocks sequences
	 * 
	 * @param blocks
	 *            List of the Board's sequences
	 * @throws IllegalArgumentException
	 *             whether the list does not contain 195 sequences
	 */
	public Board(List<Sq<Block>> blocks) {
		if (blocks.size() != Cell.COLUMNS * Cell.ROWS) {
			throw new IllegalArgumentException("The list must contain exactly 195 sequences");
		}
		ArrayList<Sq<Block>> liste = new ArrayList<>(blocks);
		this.blocks = Collections.unmodifiableList(liste);

	}

	/**
	 * Construct a board based on the given matrix
	 * 
	 * @param rows
	 *            the matrix of Block
	 * @throws IllegalArgumentException
	 *             if the matrix isn't 13x15
	 * @return the board
	 */

	public static Board ofRows(List<List<Block>> rows) {
		checkBlockMatrix(rows, Cell.ROWS, Cell.COLUMNS);

		List<Sq<Block>> board = new ArrayList<Sq<Block>>();
		
		for (List<Block> list : rows)
			for (Block block : list)
				board.add(Sq.constant(block));

		return new Board(board);
	}

	/**
	 * Construct a walled board with the matrix of the inner Blocks
	 * 
	 * @param innerBlocks
	 *            the matrix of the inner Blocks
	 * @throws IllegalArgumentException
	 *             if the matrix isn't 11x13
	 * @return the board
	 */

	public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) {
		checkBlockMatrix(innerBlocks, Cell.ROWS - 2, Cell.COLUMNS - 2);

		List<List<Block>> board = new ArrayList<>();
		
		board.add(Collections.nCopies(Cell.COLUMNS, Block.INDESTRUCTIBLE_WALL));
		
		List<Block> centralline;
		for (List<Block> list : innerBlocks) {
			centralline = new ArrayList<>();
			centralline.add(Block.INDESTRUCTIBLE_WALL);
			centralline.addAll(list);
			centralline.add(Block.INDESTRUCTIBLE_WALL);
			board.add(centralline);
		}
		board.add(Collections.nCopies(Cell.COLUMNS, Block.INDESTRUCTIBLE_WALL));
		
		return ofRows(board);
	}

	/**
	 * Construct a walled symmetric board based on the Northwest quadrant
	 * 
	 * @param quadrantNWBlocks
	 *            the matrix of the Northwest quadrant
	 * @throws IllegalArgumentException
	 *             if the matrix isn't 6x7
	 * @return the board
	 */

	public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBlocks) {
		checkBlockMatrix(quadrantNWBlocks, Cell.ROWS / 2, Cell.COLUMNS / 2);
		List<List<Block>> board = new ArrayList<>();
		List<Block> line;
		for (List<Block> list : quadrantNWBlocks) {
			line = new ArrayList<>();
			line.addAll(Lists.mirrored(list));
			board.add(line);
		}
		System.out.println(board.size());
		System.out.println(board.get(0).size());
		return ofInnerBlocksWalled(Lists.mirrored(board));
	}

	/**
	 * Check whether the matrix placed in parameter does contain "rows" rows and
	 * "columns" columns
	 * 
	 * @param a
	 *            matrix
	 * @param number
	 *            of rows expected
	 * @param number
	 *            of columns expected
	 * @throws IllegalArgumentException
	 *             if the parameters does not match
	 */

	private static void checkBlockMatrix(List<List<Block>> matrix, int rows, int columns) {
		if (matrix.size() != rows || matrix.get(0).size() != columns) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @param c
	 *            The cell from which to return the sequence of Block
	 * @return The sequence of Block in the cell c.
	 */

	public Sq<Block> blocksAt(Cell c) {
		return blocks.get(c.rowMajorIndex());
	}

	/**
	 * @param c
	 *            The cell from which to return the Block
	 * @return The head Block from the cell c.
	 */

	public Block blockAt(Cell c) {
		return blocks.get(c.rowMajorIndex()).head();
	}

}
