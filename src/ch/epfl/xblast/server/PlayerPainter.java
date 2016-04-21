package ch.epfl.xblast.server;

import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 21, 2016
 *
 */

public final class PlayerPainter {

	/**
	 * private constructor, not to be instanced
	 */
	private PlayerPainter() {
	}

	/**
	 * regarding to the PlayerID, LifeState, Direction and his position, returns the right image of player.
	 * @param tick
	 * @param player
	 * @return
	 */
	//TODO des erreurs, j'avais pas tout bien lu encore
	public static byte byteForPlayer(int tick, Player player) {
		byte output = (byte) (player.id().ordinal() * 20);
		State state = player.lifeState().state();
		if (state == State.INVULNERABLE || state == State.VULNERABLE) {
			output += player.direction().ordinal() * 3;
			output += tick % 3;
		} else if (state == State.DEAD) {
			output += 14;
		} else if (!player.isAlive()) {
			output += 13;
		} else {
			output += 12;
		}
		return output;
	}
}
