package ch.epfl.xblast.server.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

/**
 *@author Alexandre MICHEL
 *@date 28 févr. 2016
 *
 *@version 1.0
 */
public final class Boards {

	private final static Block __ = Block.FREE;
	private final static Block XX = Block.INDESTRUCTIBLE_WALL;
	private final static Block xx = Block.DESTRUCTIBLE_WALL;
	public final static Block zz = Block.CRUMBLING_WALL;
	
	public final static Board STEP2_SAMPLE = Board.ofQuadrantNWBlocksWalled(
	  Arrays.asList(
	    Arrays.asList(__, __, __, __, __, xx, __),
	    Arrays.asList(__, XX, xx, XX, xx, XX, xx),
	    Arrays.asList(__, xx, __, __, __, xx, __),
	    Arrays.asList(xx, XX, __, XX, XX, XX, XX),
	    Arrays.asList(__, xx, __, xx, __, __, __),
	    Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
	
	public final static Board EMPTY = Board.ofRows(initEmptyBoard(Cell.ROWS, Cell.COLUMNS));
	public final static Board EMPTY_WALLED = Board.ofInnerBlocksWalled(initEmptyBoard(Cell.ROWS-2, Cell.COLUMNS-2));
	
	/**
	 * <code>private static List<List<Block>> initEmptyBoard()</code>
	 * 
	 * <p>Create a full board of Free cells</p>
	 * 
	 * @param pRows Number of rows desired.
	 * @param pCols Number of Columns desired.
	 * 
	 * @return a new Board.
	 */
	private static List<List<Block>> initEmptyBoard(int pRows, int pCols){
		List<List<Block>> tmp = new ArrayList<>(pRows);
		
		for(int i = 0; i < pRows; ++i){
			tmp.add(new ArrayList<>(pCols));
			for(int j = 0; j < pCols; ++j)
				tmp.get(i).add(__);
		}
		return tmp;
	}
	
}
