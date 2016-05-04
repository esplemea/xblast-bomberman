package ch.epfl.xblast.server;

/**
 * 
 * @author Nicolas ZIMMERMANN and Clara DI MARCO
 * @date April 15, 2016
 *
 */

public enum Bonus {

	INC_BOMB {
		/**
		 * Apply a Bomb Bonus to a Player
		 * 
		 * @return the new Player
		 */
		@Override
		public Player applyTo(Player player) {
			if (player.maxBombs() < MAX_BOMB) {
				return player.withMaxBombs(player.maxBombs() + 1);
			}
			return player;
		}
	},

	INC_RANGE {
		/**
		 * Apply a Range Bonus to a Player
		 * 
		 * @return the new Player
		 */
		@Override
		public Player applyTo(Player player) {
			if (player.bombRange() < MAX_RANGE) {
				return player.withBombRange(player.bombRange() + 1);
			}
			return player;
		}
	};

	private static final int MAX_BOMB = 9;
	private static final int MAX_RANGE = 9;

	abstract public Player applyTo(Player player);
}
