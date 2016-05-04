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
 * @date April 15, 2016
 *
 */

public final class GameState {

    private static final Random RANDOM = new Random(2016);
    private static List<List<PlayerID>> permutationsList = Lists
            .permutations(Arrays.asList(PlayerID.values()));
    private static final int BOMB_RADIUS = 5;
    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;

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
    public GameState(int ticks, Board board, List<Player> players,
            List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions,
            List<Sq<Cell>> blasts) {
        this.ticks = ArgumentChecker.requireNonNegative(ticks);
        this.board = Objects.requireNonNull(board);

        if (Objects.requireNonNull(players).size() != 4) {
            throw new IllegalArgumentException("You need 4 players");
        } else {
            this.players = Collections
                    .unmodifiableList(new ArrayList<>(players));
        }

        this.bombs = Objects.requireNonNull(
                Collections.unmodifiableList(new ArrayList<>(bombs)));
        this.explosions = Objects.requireNonNull(
                Collections.unmodifiableList(new ArrayList<>(explosions)));
        this.blasts = Objects.requireNonNull(
                Collections.unmodifiableList(new ArrayList<>(blasts)));
    }

    /**
     * Constructor for a GameState
     * 
     * @param board
     * @param players
     */
    public GameState(Board board, List<Player> players) {
        this(0, board, players, new ArrayList<Bomb>(),
                new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
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
        return ticks >= Ticks.TOTAL_TICKS || alivePlayers().size()<=1;
    }

    /**
     * 
     * @return Time left for the game in seconds
     */
    public double remainingTime() {
        return (Ticks.TOTAL_TICKS - ticks) / (double)Ticks.TICKS_PER_SECOND;
    }

    /**
     * 
     * @return The PlayerID of the winner if there is one. Or no player if there
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
        return blastedCells(blasts);
    }

    /**
     * 
     * @param blasts
     * @return the set of blasted Cells from a List of sequence of Cells
     */
    private Set<Cell> blastedCells(List<Sq<Cell>> blasts) {
        Set<Cell> output = new HashSet<>();
        for (Sq<Cell> cell : blasts) {
            if (!cell.isEmpty()) {
                output.add(cell.head());
            }
        }
        return output;
    }

    /**
     * The GameState goes 1 step forward with the Players, Bombs, Bonus, Blasts,
     * etc.
     * 
     * @param speedChangeEvents
     *            the optional direction changes of the players
     * @param bombDropEvents
     *            the PlayerID of the players that want to drop a bomb
     * @return the new GameState
     */
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents,
            Set<PlayerID> bombDropEvents) {
        List<Sq<Cell>> blasts1 = nextBlasts(blasts, board, explosions);
        Set<Cell> blastedCells = blastedCells(blasts1);
        List<Bomb> bombsOutput = new ArrayList<>();
        List<Sq<Sq<Cell>>> explosions1 = new ArrayList<>(explosions);

        for (Bomb bomb : bombs) {
            if (bomb.fuseLengths().tail().isEmpty()
                    || blastedCells.contains(bomb.position())) {
                explosions1.addAll(bomb.explosion());
            } else {
                bombsOutput.add(new Bomb(bomb.ownerId(), bomb.position(),
                        bomb.fuseLength() - 1, bomb.range()));
            }
        }

        List<PlayerID> orderPlayerID = permutationsList
                .get(ticks % permutationsList.size());
        List<Player> orderPlayer = new ArrayList<>();
        for (PlayerID name : orderPlayerID) {
            for (Player player : players) {
                if (player.id() == name) {
                    orderPlayer.add(player);
                }
            }
        }
        bombsOutput
                .addAll(newlyDroppedBombs(orderPlayer, bombDropEvents, bombs));

        Set<Cell> consumedBonuses = new HashSet<>();
        Map<PlayerID, Bonus> playerBonuses = new HashMap<>();
        for (Player p : alivePlayers()) {
            Cell playerActualCell = p.position().containingCell();
            if (p.position().isCentral()
                    && board.blockAt(playerActualCell).isBonus()) {
                consumedBonuses.add(playerActualCell);
                Bonus b = board.blockAt(playerActualCell) == Block.BONUS_BOMB
                        ? Bonus.INC_BOMB : Bonus.INC_RANGE;
                playerBonuses.put(p.id(), b);
            }
        }

        Set<Cell> bombedCells = new HashSet<>();
        for (Bomb bomb : bombsOutput) {
            bombedCells.add(bomb.position());
        }

        Board boardOutput = nextBoard(board, consumedBonuses, blastedCells);
        List<Player> playersOutput = nextPlayers(players, playerBonuses,
                bombedCells, boardOutput, blastedCells, speedChangeEvents);
        List<Sq<Sq<Cell>>> explosionsOutput = nextExplosions(explosions1);
        return new GameState(ticks + 1, boardOutput, playersOutput, bombsOutput,
                explosionsOutput, blasts1);
    }

    /**
     * Generate the next Blasts to appear on the Board
     * 
     * @param blasts0
     * @param board0
     * @param explosions0
     * @return the new Blasts
     */
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0,
            Board board0, List<Sq<Sq<Cell>>> explosions0) {

        List<Sq<Cell>> blasts1 = new ArrayList<>();

        for (Sq<Cell> blast : blasts0) {
            if (board0.blockAt(blast.head()).isFree()
                    && !blast.tail().isEmpty()) {
                blasts1.add(blast.tail());
            }
        }
        for (Sq<Sq<Cell>> explosion : explosions0) {
            if (!explosion.isEmpty()) {
                blasts1.add(explosion.head());
            }
        }
        return blasts1;
    }

    /**
     * Generate the next Board, dealing with the consumed Bonuses and the blasts
     * on the blocks
     * 
     * @param board0
     * @param consumedBonuses
     * @param blastedCells1
     * @return the new Board
     */
    private static Board nextBoard(Board board0, Set<Cell> consumedBonuses,
            Set<Cell> blastedCells1) {
        List<Sq<Block>> blocks = new ArrayList<>();
        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            Block b = board0.blockAt(c);

            if (b == Block.DESTRUCTIBLE_WALL && blastedCells1.contains(c)) {
                int random = RANDOM.nextInt(3);
                Block randomBlock;
                switch (random) {
                case 0:
                    randomBlock = Block.BONUS_BOMB;
                    break;
                case 1:
                    randomBlock = Block.BONUS_RANGE;
                    break;
                default:
                    randomBlock = Block.FREE;
                }
                blocks.add(Sq
                        .repeat(Ticks.WALL_CRUMBLING_TICKS,
                                Block.CRUMBLING_WALL)
                        .concat(Sq.constant(randomBlock)));
            } else if (consumedBonuses.contains(c)) {
                blocks.add(Sq.constant(Block.FREE));
            } else if (blastedCells1.contains(c) && b.isBonus()) {
                blocks.add(board0.blocksAt(c).tail()
                        .limit(Ticks.BONUS_DISAPPEARING_TICKS)
                        .concat(Sq.constant(Block.FREE)));
            } else {
                blocks.add(board0.blocksAt(c).tail());
            }
        }

        return new Board(blocks);
    }

    /**
     * Generate the new Players (updating their LifeState, Bonus, Direction and
     * Position)
     * 
     * @param players0
     * @param playerBonuses
     * @param bombedCells1
     * @param board1
     * @param blastedCells1
     * @param speedChangeEvents
     * @return
     */
    private static List<Player> nextPlayers(List<Player> players0,
            Map<PlayerID, Bonus> playerBonuses, Set<Cell> bombedCells1,
            Board board1, Set<Cell> blastedCells1,
            Map<PlayerID, Optional<Direction>> speedChangeEvents) {

        List<Player> playerOutput = new ArrayList<>();
        Sq<LifeState> lifeStatesOutput;
        Sq<DirectedPosition> directedPosOutput;

        for (Player player : players0) {

            /*
             * First step: Computation of the new sequence of directed position
             */

            Direction askedDir = player.direction();
            if (speedChangeEvents.containsKey(player.id())) {
                askedDir = speedChangeEvents.get(player.id())
                        .orElse(player.direction());
                if (!speedChangeEvents.get(player.id()).isPresent()) {
                    directedPosOutput = player.directedPositions()
                            .takeWhile(p -> !p.position().isCentral())
                            .concat(DirectedPosition.stopped(
                                    player.directedPositions().findFirst(
                                            p -> p.position().isCentral())));
                } else if (askedDir.isParallelTo(player.direction())) {
                    directedPosOutput = DirectedPosition.moving(
                            new DirectedPosition(player.position(), askedDir));
                } else {
                    directedPosOutput = player.directedPositions()
                            .takeWhile(p -> !p.position().isCentral())
                            .concat(DirectedPosition
                                    .moving(new DirectedPosition(player
                                            .directedPositions()
                                            .findFirst(p -> p.position()
                                                    .isCentral())
                                            .position(), askedDir)));
                }
            } else
                directedPosOutput = player.directedPositions();

            /*
             * Second step: Verification of the possible obstacles
             */

            SubCell position = directedPosOutput.head().position();
            if (!((position.distanceToCentral() == BOMB_RADIUS + 1)
                    && (directedPosOutput.tail().head().position()
                            .distanceToCentral() == BOMB_RADIUS)
                    && bombedCells1.contains(position.containingCell()))) {
                if (position.isCentral()) {
                    if (board1.blockAt(
                            position.containingCell().neighbor(askedDir))
                            .canHostPlayer()) {
                        directedPosOutput = directedPosOutput.tail();
                    }
                } else {
                    directedPosOutput = directedPosOutput.tail();
                }
            }

            /*
             * Third step: The player's LifeState move on
             */
            if (player.lifeState().state() == State.VULNERABLE && blastedCells1
                    .contains(player.position().containingCell())) {
                lifeStatesOutput = player.statesForNextLife();
            } else {
                lifeStatesOutput = player.lifeStates().tail();
            }

            /*
             * Fourth step: The player gets new capacities if he ate a bonus
             * (Include the creation of the fresh Players)
             */
            Player p = new Player(player.id(), lifeStatesOutput,
                    directedPosOutput, player.maxBombs(), player.bombRange());
            if (playerBonuses.containsKey(player.id())) {
                p = playerBonuses.get(player.id()).applyTo(p);
            }
            playerOutput.add(p);
        }
        return playerOutput;
    }

    /**
     * The explosions go one step forward
     *
     * @param explosions0
     * @return the new List of explosions
     */
    private static List<Sq<Sq<Cell>>> nextExplosions(
            List<Sq<Sq<Cell>>> explosions0) {

        List<Sq<Sq<Cell>>> explosions1 = new ArrayList<>();

        for (Sq<Sq<Cell>> explosion : explosions0) {
            if (!explosion.tail().isEmpty()) {
                explosions1.add(explosion.tail());
            }
        }
        return explosions1;
    }

    /**
     * Generate the new Bombs
     * 
     * @param players0
     * @param bombDropEvents
     * @param bombs0
     * @return the list of newly dropped bombs
     */
    private static List<Bomb> newlyDroppedBombs(List<Player> players0,
            Set<PlayerID> bombDropEvents, List<Bomb> bombs0) {
        List<Bomb> bombsDropped = new ArrayList<>();
        int totalBombs;
        boolean state;
        for (Player player : players0) {
            totalBombs = 0;
            state = true;
            for (Bomb bomb : bombs0) {
                if (bomb.ownerId() == player.id())
                    ++totalBombs;
                if (bomb.position().equals(player.position().containingCell()))
                    state = false;

            }
            if (bombDropEvents.contains(player.id()) && player.isAlive()
                    && player.maxBombs() > totalBombs && state) {
                bombsDropped.add(new Bomb(player.id(),
                        player.position().containingCell(),
                        Ticks.BOMB_FUSE_TICKS - 1, player.bombRange()));
            }
        }

        return bombsDropped;
    }

}
