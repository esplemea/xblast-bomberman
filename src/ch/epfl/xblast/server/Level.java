package ch.epfl.xblast.server;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 26, 2016
 *
 */

public final class Level {
	private final BoardPainter initialBoard;
	private final GameState initialGame;
	public final static Level DEFAULT_LEVEL;

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
}
