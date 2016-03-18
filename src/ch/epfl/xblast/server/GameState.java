package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date March 4, 2016
 *
 */

public final class GameState {

	private final int ticks;
	private final Board board;
	private final List<Player> players;
	private final List<Bomb> bombs;
	private final List<Sq<Sq<Cell>>> explosions;
	private final List<Sq<Cell>> blasts;

	/**
	 * Constructor for a GameState
	 * @param ticks
	 * @param board
	 * @param players (need exactly 4 players)
	 * @param bombs
	 * @param explosions
	 * @param blasts
	 */
	public GameState(int ticks, Board board, List<Player> players, List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions,
			List<Sq<Cell>> blasts) {
		this.ticks = ArgumentChecker.requireNonNegative(ticks);
		this.board = Objects.requireNonNull(board);

		if (players.size() != 4) {
			throw new IllegalArgumentException("You need 4 players");
		} else {
			this.players = Objects.requireNonNull(Collections.unmodifiableList(players));
		}

		this.bombs = Objects.requireNonNull(Collections.unmodifiableList(bombs));
		this.explosions = Objects.requireNonNull(Collections.unmodifiableList(explosions));
		this.blasts = Objects.requireNonNull(Collections.unmodifiableList(blasts));
	}

	/**
	 * Constructor for a GameState
	 * @param board
	 * @param players
	 */
	public GameState(Board board, List<Player> players) {
		this(0, board, players, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
	}

	/**
	 * 
	 * @return The actual number of ticks
	 */
	public int ticks() {
		return ticks;
	}

	/**
	 * 
	 * @return true if the game is over
	 */
	public boolean isGameOver() {
		for (Player player : players) {
			if (player.isAlive()) {
				return true;
			}
		}
		return ticks >= Ticks.TOTAL_TICKS;
	}

	/**
	 * 
	 * @return Time left for the game
	 */
	public double remainingTime() {
		return (Ticks.TOTAL_TICKS - ticks) / Ticks.TICKS_PER_SECOND;
	}

	/**
	 * 
	 * @return The PlayerID of the winner is there is one. Or no player if there is no winner.
	 */
	public Optional<PlayerID> winner() {
		List<Player> playersAlive = alivePlayers();
		if(playersAlive.size()==1){
			return Optional.of(playersAlive.get(0).id());
		}
		else{
			return Optional.empty();
		}
	}

	/**
	 * 
	 * @return The board
	 */
	public Board board() {
		return board;
	}

	/**
	 * 
	 * @return The List of every Players (alive and dead)
	 */
	public List<Player> players() {
		return players;
	}

	/**
	 * 
	 * @return The List of every Player still alive
	 */
	public List<Player> alivePlayers() {
		List<Player> output = new ArrayList<>();
		for (Player player : players) {
			if (player.isAlive()) {
				output.add(player);
			}
		}
		return output;
	}
}
