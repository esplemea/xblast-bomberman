package ch.epfl.xblast.server;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date March 4, 2016
 *
 */

public enum Block {
    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL, BONUS_BOMB, BONUS_RANGE;

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
        return this.isFree();
    }

    public boolean castsShadow() {
        return this == INDESTRUCTIBLE_WALL || this == DESTRUCTIBLE_WALL
                || this == CRUMBLING_WALL;
    }

    public Bonus associatedBonus() {
        return this.maybeAssociatedBonus;
    }
}
