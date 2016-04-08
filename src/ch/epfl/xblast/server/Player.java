package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date March 4, 2016
 *
 */

public class Player {
	private final PlayerID id;
	private final Sq<LifeState> lifeState;
	private final Sq<DirectedPosition> directedPos;
	private final int bombRange;
	private final int maxBombs;

	/**
	 * Constructor for a Player
	 * 
	 * @param id
	 * @param lifeStates
	 * @param directedPos
	 * @param maxBombs
	 * @param bombRange
	 */
	public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs,
			int bombRange) {
		this.id = Objects.requireNonNull(id);
		this.lifeState = Objects.requireNonNull(lifeStates);
		this.directedPos = Objects.requireNonNull(directedPos);
		this.maxBombs = ArgumentChecker.requireNonNegative(maxBombs);
		this.bombRange = ArgumentChecker.requireNonNegative(bombRange);
	}

	/**
	 * Constructor for a Player using a int for the lives and a Cell for the
	 * positions
	 * 
	 * @param id
	 * @param lives
	 * @param position
	 * @param maxBombs
	 * @param bombRange
	 */
	public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange) {
		this(id, Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(lives, State.INVULNERABLE))
				.concat(Sq.constant(new LifeState(lives, State.VULNERABLE))),
				DirectedPosition.stopped(
						new DirectedPosition(SubCell.centralSubCellOf(Objects.requireNonNull(position)), Direction.S)),
				maxBombs, bombRange);
	}

	/**
	 * 
	 * @return the Player's id
	 */
	public PlayerID id() {
		return id;
	}

	/**
	 * 
	 * @return the Player's sequence of LifeStates
	 */
	public Sq<LifeState> lifeStates() {
		return lifeState;
	}

	/**
	 * 
	 * @return the actual lifeState (number of lives, state) of the Player
	 */
	public LifeState lifeState() {
		return lifeState.head();
	}

	/**
	 * 
	 * @return the sequence of LifeStates after being hit (after a death)
	 */
	public Sq<LifeState> statesForNextLife() {
	    Sq<LifeState> lives ;//= Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(), State.DYING));
		if (lives() <= 1) {
			lives = Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(), State.DYING))
			        .concat(Sq.constant(new LifeState(0, State.DEAD)));
		} else {
			lives = Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(), State.DYING)).concat(Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(lives() - 1, State.INVULNERABLE)))
					.concat(Sq.constant(new LifeState(lives() - 1, State.VULNERABLE)));

		}
		return lives;
	}

	/**
	 * 
	 * @return the Player's number of lives
	 */
	public int lives() {
	    return lifeState().lives;
	}

	/**
	 * 
	 * @return true if the player is alive
	 */
	public boolean isAlive() {
		return lives() > 0;
	}

	/**
	 * 
	 * @return the sequence of positions directed of the Player
	 */
	public Sq<DirectedPosition> directedPositions() {
		return directedPos;
	}

	/**
	 * 
	 * @return the Player's position
	 */
	public SubCell position() {
		return directedPos.head().position();
	}

	/**
	 * 
	 * @return the Player's direction
	 */
	public Direction direction() {
		return directedPos.head().direction();
	}

	/**
	 * 
	 * @return the maximum of bombs the Player can have simultaneously
	 */
	public int maxBombs() {
		return maxBombs;
	}

	/**
	 * Change the the maximum of bombs
	 * 
	 * @param newMaxBombs
	 * @return a new Player with a new maximum of bombs
	 */
	public Player withMaxBombs(int newMaxBombs) {
		return new Player(id, lifeState, directedPos, newMaxBombs, bombRange);
	}

	/**
	 * 
	 * @return the actual bombs' range
	 */
	public int bombRange() {
		return bombRange;
	}

	/**
	 * Change the the bombs' range
	 * 
	 * @param newMaxBombs
	 * @return a new Player with a new bomb range
	 */
	public Player withBombRange(int newBombRange) {
		return new Player(id, lifeState, directedPos, maxBombs, newBombRange);
	}

	/**
	 * Drop a bomb on the Player's position
	 * 
	 * @return a new Bomb, having the Player's bombRange
	 */
	public Bomb newBomb() {
		return new Bomb(id, position().containingCell(), Ticks.BOMB_FUSE_TICKS, bombRange);
	}

	public static class LifeState {
		int lives;
		State state;

		/**
		 * Constructor for a LifeState
		 * 
		 * @param lives
		 * @param state
		 */
		public LifeState(int lives, State state) {
			this.lives = ArgumentChecker.requireNonNegative(lives);
			this.state = Objects.requireNonNull(state);
		}

		/**
		 * 
		 * @return the lives
		 */
		public int lives() {
			return lives;
		}

		/**
		 * 
		 * @return the state
		 */
		public State state() {
			return state;
		}

		/**
		 * 
		 * @return true if the player can move
		 */
		public boolean canMove() {
			return (state == State.INVULNERABLE || state == State.VULNERABLE);
		}

		/**
		 * The enumeration with every possible state for a player
		 *
		 */
		public enum State {
			INVULNERABLE, VULNERABLE, DYING, DEAD;
		}
	}

	public static final class DirectedPosition {
		Direction direction;
		SubCell position;

		/**
		 * Constructor for a DirectedPosition
		 * 
		 * @param position
		 * @param direction
		 */
		public DirectedPosition(SubCell position, Direction direction) {
			this.position = Objects.requireNonNull(position);
			this.direction = Objects.requireNonNull(direction);
		}

		/**
		 * 
		 * @param p
		 * @return a infinite Sq<DirectedPosition> of DirectedPosition p
		 */
		public static Sq<DirectedPosition> stopped(DirectedPosition p) {
			return Sq.constant(p);
		}

		/**
		 * 
		 * @param p
		 * @return a infinite Sq<DirectedPosition> of DirectedPosition in the
		 *         direction of p and each the neighbour of the last one in the
		 *         same direction of p
		 */
		public static Sq<DirectedPosition> moving(DirectedPosition p) {
			return Sq.iterate(p, c -> new DirectedPosition(c.position.neighbor(c.direction), c.direction));
		}

		/**
		 * 
		 * @return the direction
		 */
		public Direction direction() {
			return direction;
		}

		/**
		 * 
		 * @return the position
		 */
		public SubCell position() {
			return position;
		}

		/**
		 * 
		 * @param newPosition
		 * @return a new DirectedPosition with the param position and the actual
		 *         DirectedPosition's direction
		 */
		public DirectedPosition withPosition(SubCell newPosition) {
			return new DirectedPosition(newPosition, direction);
		}

		/**
		 * 
		 * @param newDirection
		 * @return a new DirectedPosition with the param direction and the
		 *         actual DirectedPosition's position
		 */
		public DirectedPosition withDirection(Direction newDirection) {
			return new DirectedPosition(position, newDirection);
		}
	}
}
