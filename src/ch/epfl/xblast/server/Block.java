package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 15, 2016
 *
 */

public enum Block {
    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL, BONUS_BOMB(
            Bonus.INC_BOMB), BONUS_RANGE(Bonus.INC_RANGE);

    private Bonus maybeAssociatedBonus;

    /**
     * Construct a Block with a bonus contained inside of it
     * 
     * @param maybeAssociatedBonus
     */
    private Block(Bonus maybeAssociatedBonus) {
        this.maybeAssociatedBonus = maybeAssociatedBonus;
    }

    /**
     * Construct a Block without any Bonus
     */
    private Block() {
        this.maybeAssociatedBonus = null;
    }

    /**
     * 
     * @return true if the block if free
     */
    public boolean isFree() {
        return this == FREE;
    }

    /**
     * 
     * @return true if the Block is a Bonus
     */
    public boolean isBonus() {
        return this == BONUS_BOMB || this == BONUS_RANGE;
    }

    /**
     * 
     * @return true if it can host a Player (hence if it's free or a Bonus)
     */
    public boolean canHostPlayer() {
        return isFree() || isBonus();
    }

    /**
     * 
     * @return true if the Block is a wall
     */
    public boolean castsShadow() {
        return this == INDESTRUCTIBLE_WALL || this == DESTRUCTIBLE_WALL
                || this == CRUMBLING_WALL;
    }

    /**
     * 
     * @throws NoSuchElementException
     *             if the block isn't associated to a Bonus
     * @return the type of Bonus the Block contains
     */
    public Bonus associatedBonus() {
        if (this.maybeAssociatedBonus == null) {
            throw new NoSuchElementException(
                    "There isn't any bonus on this block");
        }
        return this.maybeAssociatedBonus;
    }
}
