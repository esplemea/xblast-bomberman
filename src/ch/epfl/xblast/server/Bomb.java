package ch.epfl.xblast.server;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 15, 2016
 *
 */

public class Bomb {
	private final PlayerID ownerId;
	private final Cell position;
	private final Sq<Integer> fuseLengths;
	private final int range;

	/**
	 * Constructor for a bomb
	 * 
	 * @throws NullPointerException
	 *             if either ownerId, position or fuseLengths is null
	 * @param ownerId
	 * @param position
	 * @param fuseLengths
	 * @param range
	 */
	public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths, int range) {
		if (fuseLengths.isEmpty()) {
			throw new IllegalArgumentException("fuseLengths cannot be empty");
		}
		this.ownerId = Objects.requireNonNull(ownerId);
		this.position = Objects.requireNonNull(position);
		this.fuseLengths = Objects.requireNonNull(fuseLengths);
		this.range = ArgumentChecker.requireNonNegative(range);
	}

	/**
	 * Constructor for a bomb using a integer number for the fuseLengths
	 * 
	 * @throws NullPointerException
	 *             if either ownerId, position or fuseLengths is null
	 * @param ownerId
	 * @param position
	 * @param fuseLengths
	 * @param range
	 */
	public Bomb(PlayerID ownerId, Cell position, int fuseLengths, int range) {
		this(ownerId, position, Sq.iterate(fuseLengths, u -> u - 1).limit(fuseLengths), range);
	}

	/**
	 * 
	 * @return the actual bomb owner ID
	 */
	public PlayerID ownerId() {
		return ownerId;
	}

	/**
	 * 
	 * @return the bomb's position
	 */
	public Cell position() {
		return position;
	}

	/**
	 * 
	 * @return the bomb sequence of fuseLengths
	 */
	public Sq<Integer> fuseLengths() {
		return fuseLengths;
	}

	/**
	 * 
	 * @return the actual bomb's fuseLengths
	 */
	public int fuseLength() {
		return fuseLengths.head();
	}

	/**
	 * 
	 * @return the bomb's range
	 */
	public int range() {
		return range;
	}

	/**
	 * Create the explosion of a Bomb in the 4 Direction (N, S, E, W)
	 * 
	 * @return the bomb's explosion
	 */
	public List<Sq<Sq<Cell>>> explosion() {
		return Arrays.asList(explosionArmTowards(Direction.E), explosionArmTowards(Direction.W),
				explosionArmTowards(Direction.N), explosionArmTowards(Direction.S));
	}

	private Sq<Sq<Cell>> explosionArmTowards(Direction dir) {
		return Sq.repeat(Ticks.EXPLOSION_TICKS, Sq.iterate(position, c -> c.neighbor(dir)).limit(range));
	}
}
