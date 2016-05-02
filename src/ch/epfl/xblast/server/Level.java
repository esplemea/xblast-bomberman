package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 26, 2016
 *
 */

public final class Level {
    

    
	private final BoardPainter initialBoard;
	private final GameState initialGame;
	public final static Level DEFAULT_LEVEL = defaultLevel();

	/**
	 * Constructor for a level
	 * 
	 * @param initialBoard
	 * @param initialGame
	 */
	public Level(BoardPainter initialBoard, GameState initialGame) {
		this.initialBoard = initialBoard;
		this.initialGame = initialGame;
	}

	/**
	 * Getter for the BoardPainter
	 * 
	 * @return
	 */
	public BoardPainter getBoardPainter() {
		return initialBoard;
	}

	/**
	 * Getter for the GameState
	 * 
	 * @return
	 */
	public GameState getGameState() {
		return initialGame;
	}
	
	private static Level defaultLevel() {
	    
	    List<List<Block>> defaultInnerBlocks = new ArrayList<>();
	    
	    Map<Block,BlockImage> defaultPalette = new HashMap<>();
	    defaultPalette.put(Block.FREE, BlockImage.IRON_FLOOR);
	    defaultPalette.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
	    defaultPalette.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
	    defaultPalette.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
	    defaultPalette.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
	    defaultPalette.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
	    
	    Board defaultBoard = Board.ofInnerBlocksWalled(defaultInnerBlocks);
	    
	    List<Player> defaultPlayers = new ArrayList<>();
	    defaultPlayers.add(new Player(PlayerID.PLAYER_1,3,new Cell(1,1),2,3));
	    defaultPlayers.add(new Player(PlayerID.PLAYER_2,3,new Cell(13,1),2,3));
	    defaultPlayers.add(new Player(PlayerID.PLAYER_3,3,new Cell(13,11),2,3));
	    defaultPlayers.add(new Player(PlayerID.PLAYER_4,3,new Cell(1,11),2,3));
	    
	    BoardPainter defaultBoardPainter = new BoardPainter(defaultPalette, BlockImage.IRON_FLOOR_S);
	    GameState defaultGame = new GameState(defaultBoard, defaultPlayers);
	    
	    return new Level(defaultBoardPainter,defaultGame);
	}
}
