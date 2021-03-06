package ch.epfl.xblast.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState.Player;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date Mai 25, 2016
 *
 */

@SuppressWarnings("serial")
public final class XblastComponent extends JComponent {

    private final int BLOCK_WIDTH = 64;
    private final int BLOCK_HEIGHT = 48;
    private final int SCORE_WIDTH = 48;
    private final int TIME_SIZE = 16;
    private final int WIDTH = BLOCK_WIDTH * Cell.COLUMNS;
    private final int HEIGHT = 688;
    
    private GameState gameState = null;
    private PlayerID playerID = null;

    /**
     * Give the preferred dimension for the game
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    /**
     * Paint the game in this order : block, bombs/explosions, score, timer,
     * lives and players
     */
    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;

        if (gameState != null) {
            int counter = 0;
            int x = 0;
            int y = 0;
            for (Image block : gameState.getBoard()) {
                g.drawImage(block, x, y, null);
                x = (x + BLOCK_WIDTH) % WIDTH;
                y = ++counter / 15 * BLOCK_HEIGHT;
            }
            y = 0;
            counter = 0;
            for (Image bombOrBlast : gameState.getExplosions()) {
                g.drawImage(bombOrBlast, x, y, null);
                x = (x + BLOCK_WIDTH) % WIDTH;
                y = ++counter / 15 * BLOCK_HEIGHT;
            }
            x = 0;
            for (Image score : gameState.getScore()) {
                g.drawImage(score, x, y, null);
                x += SCORE_WIDTH;
            }

            x = 0;
            y += BLOCK_HEIGHT;
            for (Image time : gameState.getTime()) {
                g.drawImage(time, x, y, null);
                x += TIME_SIZE;
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 25));

            int[] positionLivesX = { 96, 240, 768, 912 };
            for (int i = 0; i < 4; ++i)
                g.drawString("" + gameState.getPlayers().get(i).getLives(),
                        positionLivesX[i], 659);

            Comparator<Player> yComparator = (p1, p2) -> Integer
                    .compare(p1.getPosition().y(), p2.getPosition().y());
            int ordinal = playerID.ordinal() + 1;
            Comparator<Player> iDComparator = (p1, p2) -> Integer.compare(
                    Math.floorMod(p1.getID().ordinal() - ordinal,
                            PlayerID.values().length),
                    Math.floorMod(p2.getID().ordinal() - ordinal,
                            PlayerID.values().length));
            List<Player> sortedPlayers = new ArrayList<>(
                    gameState.getPlayers());
            sortedPlayers.sort(yComparator.thenComparing(iDComparator));
            for (Player p : sortedPlayers) {
                g.drawImage(p.getImage(), p.getPosition().x() * 4 - 24,
                        p.getPosition().y() * 3 - 52, null);
            }
        } else {
            g.drawString("Waiting for connection!", 50, 50);
        }
    }

    /**
     * Set and paint the new game
     */
    public void setGameState(GameState game, PlayerID player) {
        gameState = game;
        playerID = player;
        repaint();
    }
}
