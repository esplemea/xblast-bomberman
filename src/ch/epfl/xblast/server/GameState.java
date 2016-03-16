package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date March 4, 2016
 *
 */

public final class GameState {

    private int ticks;
    private Board board;
    private List<Player> players;
    private List<Bomb> bombs;
    private List<Sq<Sq<Cell>>> explosions;
    private List<Sq<Cell>> blasts;

    public GameState(int ticks, Board board, List<Player> players,
            List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions,
            List<Sq<Cell>> blasts) {
        this.ticks = ArgumentChecker.requireNonNegative(ticks);
        this.board = Objects.requireNonNull(board);

        if (players.size() != 4) {
            throw new IllegalArgumentException("You need 4 players");
        } else {
            this.players = players;
        }

        this.bombs = Objects.requireNonNull(bombs);
        this.explosions = Objects.requireNonNull(explosions);
        this.blasts = Objects.requireNonNull(blasts);
    }
    
    GameState(Board board, List<Player> players) {
        this(0, board, players, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
    }

}
