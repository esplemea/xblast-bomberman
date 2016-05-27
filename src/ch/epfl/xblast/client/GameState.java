
package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date Mai 25, 2016
 *
 */

public final class GameState {

    private final List<Player> players;
    private final List<Image> board;
    private final List<Image> explosions;
    private final List<Image> score;
    private final List<Image> time;

    /**
     * Constructor for a GameState (client version)
     * 
     * @param players
     * @param board
     * @param explosions
     * @param score
     * @param time
     */
    public GameState(List<Player> players, List<Image> board,
            List<Image> bombs_blasts, List<Image> score, List<Image> time) {
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.board = Collections.unmodifiableList(new ArrayList<>(board));
        this.explosions = Collections
                .unmodifiableList(new ArrayList<>(bombs_blasts));
        this.score = Collections.unmodifiableList(new ArrayList<>(score));
        this.time = Collections.unmodifiableList(new ArrayList<>(time));
    }

    /**
     * @return The list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return The list of image representing the board
     */
    public List<Image> getBoard() {
        return board;
    }

    /**
     * @return The list of image representing the bombs and blasts
     */
    public List<Image> getExplosions() {
        return explosions;
    }

    /**
     * @return The list of image representing the score area
     */
    public List<Image> getScore() {
        return score;
    }

    /**
     * @return The list of image representing the time
     */
    public List<Image> getTime() {
        return time;
    }

    public static final class Player {

        private final PlayerID id;
        private final int lives;
        private final SubCell position;
        private final Image image;

        /**
         * Constructor for a player (client version)
         * 
         * @param id
         * @param lives
         * @param position
         * @param image
         */
        public Player(PlayerID id, int lives, SubCell position, Image image) {
            this.id = id;
            this.lives = lives;
            this.position = position;
            this.image = image;
        }

        /**
         * @return The player's number of lives
         */
        public int getLives() {
            return lives;
        }

        /**
         * @return The SubCell where the player stands
         */
        public SubCell getPosition() {
            return position;
        }

        /**
         * @return The player's ID
         */
        public PlayerID getID() {
            return id;
        }

        /**
         * @return The image representing the player
         */
        public Image getImage() {
            return image;
        }
    }
}
