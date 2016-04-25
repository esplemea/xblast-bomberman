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
	 * regarding to the PlayerID, LifeState, Direction, the actual Tick and his
	 * position, returns the right image of player.
	 * 
	 * @param tick
	 *            current tick
	 * @param player
	 * @return
	 */
	public static byte byteForPlayer(int tick, Player player) {
		byte output = (byte) (player.id().ordinal() * 20);
		State state = player.lifeState().state();
		if (state == State.DYING) {
			if (player.lives() < 2)
				output += 13;
			else
				output += 12;
		} else if (state == State.DEAD) {
			output += 15;
		} else {
			if (state == State.INVULNERABLE && tick % 2 == 1)
				output = 80;
			output += player.direction().ordinal() * 3;
			int comp = player.direction().isHorizontal() ? player.position().x() : player.position().y();
			if (comp % 4 == 1)
				output += 1;
			else if (comp % 4 == 3)
				output += 2;
		}
		return output;
	}
}
