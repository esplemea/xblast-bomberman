package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;

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
	
	public static List<Byte> serialize(BoardPainter painter, GameState game){
		List<Byte> output = new ArrayList<>();
		return output;
	}
}
