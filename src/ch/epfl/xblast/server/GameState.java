package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Lists;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;

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
	private static final Random RANDOM = new Random(2016);
	private static List<List<PlayerID>> permutationsList = Lists.permutations(Arrays.asList(PlayerID.values()));

	/**
	 * Constructor for a GameState
	 * 
	 * @param ticks
	 * @param board
	 * @param players
	 *            (need exactly 4 players)
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
			this.players = Collections.unmodifiableList(players);
		}

		this.bombs = Objects.requireNonNull(Collections.unmodifiableList(bombs));
		this.explosions = Objects.requireNonNull(Collections.unmodifiableList(explosions));
		this.blasts = Objects.requireNonNull(Collections.unmodifiableList(blasts));
	}

	/**
	 * Constructor for a GameState
	 * 
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
	 * @return The PlayerID of the winner is there is one. Or no player if there
	 *         is no winner.
	 */
	public Optional<PlayerID> winner() {
		List<Player> playersAlive = alivePlayers();
		if (playersAlive.size() == 1) {
			return Optional.of(playersAlive.get(0).id());
		} else {
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

	/**
	 * 
	 * @return a table associating each Bomb to the Cell where it is
	 */
	public Map<Cell, Bomb> bombedCells() {
		Map<Cell, Bomb> output = new HashMap<>();
		for (Bomb bomb : bombs) {
			output.put(bomb.position(), bomb);
		}
		return output;
	}

	/**
	 * 
	 * @return the set of all blasted Cells
	 */
	public Set<Cell> blastedCells() {
		Set<Cell> output = new HashSet<>();
		for (Sq<Cell> cell : blasts) {
			output.add(cell.head());
		}
		return output;
	}

	/**
	 * The GameState goes 1 step forward
	 * 
	 * @param speedChangeEvents
	 * @param bombDropEvents
	 * @return the new GameState
	 */
	public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents, Set<PlayerID> bombDropEvents) {
		Set<Cell> blastedCells = blastedCells();
		List<Bomb> bombsOutput = new ArrayList<>();
		for (Bomb bomb : bombs) {
			// decreased the fuseLength by 1 when it won't become 0
			if (bomb.fuseLength() - 1 != 0) {
				bombsOutput.add(new Bomb(bomb.ownerId(), bomb.position(), bomb.fuseLength() - 1, bomb.range()));
			}
			// if the bomb is hit by a blast
			else if (blastedCells.contains(bomb.position())) {
				explosions.addAll(bomb.explosion());
			} else {// when the fuseLength become 0, the bomb explodes
				explosions.addAll(bomb.explosion());
			}
		}

		List<PlayerID> orderPlayerID = permutationsList.get(ticks % permutationsList.size());
		List<Player> orderPlayer = new ArrayList<>();
		for (PlayerID name : orderPlayerID) {
			for (Player player : players) {
				if (player.id() == name) {
					orderPlayer.add(player);
				}
			}
		}
		bombsOutput.addAll(newlyDroppedBombs(orderPlayer, bombDropEvents, bombsOutput));

		Set<Cell> consumedBonuses = new HashSet<>();
		Map<PlayerID, Bonus> playerBonuses = new HashMap<>();
		for (Player p : alivePlayers()) {
			Cell playerActualCell = p.position().containingCell();
			if (board.blockAt(playerActualCell) == Block.BONUS_BOMB) {
				consumedBonuses.add(playerActualCell);
				playerBonuses.put(p.id(), Bonus.INC_BOMB);
			} else if (board.blockAt(playerActualCell) == Block.BONUS_RANGE) {
				consumedBonuses.add(playerActualCell);
				playerBonuses.put(p.id(), Bonus.INC_RANGE);
			}
		}

		Set<Cell> bombedCells = new HashSet<>();
		for (Bomb bomb : bombsOutput) {
			bombedCells.add(bomb.position());
		}

		Board boardOutput = nextBoard(board, consumedBonuses, blastedCells);
		List<Player> playersOutput = nextPlayers(players, playerBonuses, bombedCells, boardOutput, blastedCells,
				speedChangeEvents);
		List<Sq<Sq<Cell>>> explosionsOutput = nextExplosions(explosions);
		return new GameState(ticks + 1, boardOutput, playersOutput, bombsOutput, explosionsOutput, blasts);
	}

	/**
	 * 
	 * @param board0
	 * @param consumedBonuses
	 * @param blastedCells1
	 * @return
	 */
	private static Board nextBoard(Board board0, Set<Cell> consumedBonuses, Set<Cell> blastedCells1) {
		List<Sq<Block>> blocks = new ArrayList<>();
		for (Cell c : Cell.ROW_MAJOR_ORDER) {
			Block b = board0.blockAt(c);
			switch (b) {
			case DESTRUCTIBLE_WALL:
				if (blastedCells1.contains(c)) {
					int random = RANDOM.nextInt(3);
					Block randomBlock = Block.FREE;
					if (random == 0) {
						randomBlock = Block.BONUS_BOMB;
					} else if (random == 1) {
						randomBlock = Block.BONUS_RANGE;
					}
					blocks.add(Sq.repeat(Ticks.WALL_CRUMBLING_TICKS, Block.CRUMBLING_WALL)
							.concat(Sq.constant(randomBlock)));
				}
				break;
			case BONUS_BOMB:
			case BONUS_RANGE:
				if (consumedBonuses.contains(c)) {
					blocks.add(Sq.constant(Block.FREE));
				} else if (blastedCells1.contains(c)) {
					Sq<Block> futureBlock = board0.blocksAt(c);
					for (int i = 0; i < Ticks.BONUS_DISAPPEARING_TICKS; ++i) {
						futureBlock = futureBlock.tail();
					}
					if (futureBlock.head() != Block.FREE) {
						blocks.add(Sq.repeat(Ticks.BONUS_DISAPPEARING_TICKS, b).concat(Sq.constant(Block.FREE)));
					} else {
						blocks.add(board0.blocksAt(c).tail());
					}
				} else {
					blocks.add(board0.blocksAt(c).tail());
				}
				break;
			default:
				blocks.add(board0.blocksAt(c).tail());
			}
		}
		return new Board(blocks);
	}

	/**
	 * TODO in step 6
	 * 
	 * @param players0
	 * @param playerBonuses
	 * @param bombedCells1
	 * @param board1
	 * @param blastedCells1
	 * @param speedChangeEvents
	 * @return
	 */
	private static List<Player> nextPlayers(List<Player> players0, Map<PlayerID, Bonus> playerBonuses,
			Set<Cell> bombedCells1, Board board1, Set<Cell> blastedCells1,
			Map<PlayerID, Optional<Direction>> speedChangeEvents) {

		List<Player> playerOutput = new ArrayList<>();
		Sq<LifeState> lifeStatesOutput;
		Sq<DirectedPosition> directedPosOutput;

		for (Player player : players0) {

			/*
			 * First step: Computation of the new sequence of directed position
			 */

			// if the player want to change direction
			if (speedChangeEvents.containsKey(player.id())) {
				Direction askedDir = speedChangeEvents.get(player.id()).get();
				// if the player wants to go backward, he can whenever he wants
				if (askedDir.isParallelTo(player.direction())) {
					directedPosOutput = DirectedPosition.moving(new DirectedPosition(player.position(), askedDir));
				}
				// if he wants to stop, he can on the next central SubCell
				else if (speedChangeEvents.get(player.id()).get() == null) {
					directedPosOutput = player.directedPositions().takeWhile(p -> !p.position().isCentral())
							.concat(DirectedPosition
									.stopped(player.directedPositions().findFirst(p -> p.position().isCentral())));
				}
				// otherwise, his new direction is taken on the next central
				// SubCell
				else {
					directedPosOutput = player.directedPositions().takeWhile(p -> !p.position().isCentral())
							.concat(DirectedPosition.moving(new DirectedPosition(
									player.directedPositions().findFirst(p -> p.position().isCentral()).position(),
									askedDir)));
				}
			}
			// if he does not want to change direction, the directed position
			// stays the same
			else
				directedPosOutput = player.directedPositions();

			/*
			 * Second step: Verification of the possible obstacles
			 */

			// if the player isn't blocked by a bomb, nor by a wall, he moves
			SubCell position = directedPosOutput.head().position();
			if ((!(position.distanceToCentral() == 6)
					&& !(directedPosOutput.tail().head().position().distanceToCentral() == 5)
					&& !bombedCells1.contains(position.containingCell()))
					&& ((position.isCentral()
							&& !(board1.blockAt(position.containingCell()
									.neighbor(player.direction())) == Block.INDESTRUCTIBLE_WALL)
							&& !(board1.blockAt(position.containingCell()
									.neighbor(player.direction())) == Block.DESTRUCTIBLE_WALL)))) {
				directedPosOutput = directedPosOutput.tail();
			}

			/*
			 * Third step: The player's LifeState move on
			 */

			// if the player is vulnerable and on a blasted cell, he loses a
			// life or die
			if (player.lifeState().state() == State.VULNERABLE
					&& blastedCells1.contains(player.position().containingCell())) {
				lifeStatesOutput = player.statesForNextLife();
			} else {
				lifeStatesOutput = player.lifeStates().tail();
			}

			/*
			 * Fourth step: The player gets new capacities if he ate a bonus
			 * (Include the creation of the fresh Players)
			 */

			switch (playerBonuses.get(player.id())) {
			case INC_RANGE:
				playerOutput.add(new Player(player.id(), lifeStatesOutput, directedPosOutput, player.maxBombs(),
						player.bombRange() + 1));
				break;
			case INC_BOMB:
				playerOutput.add(new Player(player.id(), lifeStatesOutput, directedPosOutput, player.maxBombs() + 1,
						player.bombRange()));
				;
				break;
			default:
				playerOutput.add(new Player(player.id(), lifeStatesOutput, directedPosOutput, player.maxBombs(),
						player.bombRange()));
				;
			}
		}
		return playerOutput;
	}

	/**
	 * The explosions go one step forward
	 *
	 * @param explosions0
	 * @return the new List of explosions
	 */
	private static List<Sq<Sq<Cell>>> nextExplosions(List<Sq<Sq<Cell>>> explosions0) {
		for (Sq<Sq<Cell>> explosion : explosions0) {
			explosion = explosion.tail();
		}
		return explosions0;
	}

	/**
	 * 
	 * @param players0
	 * @param bombDropEvents
	 * @param bombs0
	 * @return the list of newly dropped bombs
	 */
	private static List<Bomb> newlyDroppedBombs(List<Player> players0, Set<PlayerID> bombDropEvents,
			List<Bomb> bombs0) {
		List<Bomb> bombsDropped = new ArrayList<>();
		int totalBombs;
		boolean state;
		for (Player player : players0) {
			totalBombs = 0;
			state = true;
			for (Bomb bomb : bombs0) {
				if (bomb.ownerId() == player.id())
					++totalBombs;
				if (bomb.position() == player.position().containingCell())
					state = false;
			}
			if (bombDropEvents.contains(player.id()) && player.isAlive() && totalBombs < player.maxBombs() && state) {
				bombsDropped.add(new Bomb(player.id(), player.position().containingCell(), Ticks.BOMB_FUSE_TICKS - 1,
						player.bombRange()));
				bombs0.add(new Bomb(player.id(), player.position().containingCell(), Ticks.BOMB_FUSE_TICKS - 1,
						player.bombRange()));
			}
		}

		return bombsDropped;
	}
}
