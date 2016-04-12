package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date March 4, 2016
 *
 */

public enum Block {
    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL, BONUS_BOMB(Bonus.INC_BOMB), BONUS_RANGE(Bonus.INC_RANGE);

    private Bonus maybeAssociatedBonus;

    private Block(Bonus maybeAssociatedBonus) {
        this.maybeAssociatedBonus = maybeAssociatedBonus;
    }

    private Block() {
        this.maybeAssociatedBonus = null;
    }

    public boolean isFree() {
        return this == FREE;
    }

    public boolean isBonus() {
        return this == BONUS_BOMB || this == BONUS_RANGE;
    }

    public boolean canHostPlayer() {
        return isFree() || isBonus();
    }

    public boolean castsShadow() {
        return this == INDESTRUCTIBLE_WALL || this == DESTRUCTIBLE_WALL
                || this == CRUMBLING_WALL;
    }

    public Bonus associatedBonus() {
    	if(this.maybeAssociatedBonus==null){
    		throw new NoSuchElementException("There isn't any bonus on this block");
    	}
        return this.maybeAssociatedBonus;
    }
}
