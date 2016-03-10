package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

public class Bomb {
	PlayerID ownerId;
	Cell position;
	Sq<Integer> fuseLengths;
	int range;

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
		if (ownerId == null || position == null || fuseLengths == null) {
			throw new NullPointerException("ownerId, position and fuseLengths must be not null");
		}
		this.ownerId = ownerId;
		this.position = position;
		this.fuseLengths = fuseLengths;
		this.range = range;
	}

	/**
	 * Constructor for a bomb using a int number for the fuseLengths
	 * 
	 * @throws NullPointerException
	 *             if either ownerId, position or fuseLengths is null
	 * @param ownerId
	 * @param position
	 * @param fuseLengths
	 * @param range
	 */
	public Bomb(PlayerID ownerId, Cell position, int fuseLengths, int range) {
//		if (ownerId == null || position == null || fuseLengths == 0) {
//			throw new NullPointerException("ownerId, position, fuseLengths must be not null");
//		}
//		this.ownerId = ownerId;
//		this.position = position;
//		this.fuseLengths = Sq.iterate(fuseLengths, u -> u - 1).limit(fuseLengths);
//		this.range = range;
	    this(ownerId, position, Sq.iterate(fuseLengths, u -> u - 1).limit(fuseLengths), range);
	}
	
	/**
	 * 
	 * @return the actual bomb owner ID
	 */
	public PlayerID ownerId(){
		return ownerId;
	}
	
	/**
	 * 
	 * @return the bomb's position
	 */
	public Cell position(){
		return position;
	}
	
	/**
	 * 
	 * @return the bomb sequence of fuseLengths 
	 */
	public Sq<Integer> fuseLengths(){
		return fuseLengths;
	}
	
	/**
	 * 
	 * @return the actual bomb's fuseLengths
	 */
	public int fuseLength(){
		return fuseLengths.head();
	}
	/**
	 * 
	 * @return the bomb's range
	 */
	public int range(){
		return range;
	}
	
	public List<Sq<Sq<Cell>>> explosion(){
	    List<Sq<Sq<Cell>>> explosion = 
	            Arrays.asList(explosionArmTowards(Direction.E),explosionArmTowards(Direction.W),explosionArmTowards(Direction.N),explosionArmTowards(Direction.S));
	    return explosion;
	}
	
	private Sq<Sq<Cell>> explosionArmTowards(Direction dir){
	    return Sq.repeat(3, Sq.iterate(position, c -> c.neighbor(dir)).limit(range));
	}
}
