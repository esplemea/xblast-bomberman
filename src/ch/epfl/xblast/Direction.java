package ch.epfl.xblast;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 15, 2016
 *
 */

public enum Direction {
    N, E, S, W;

    /**
     * 
     * @return the opposite direction to the actual direction
     */
    public Direction opposite() {
        switch (this.ordinal()) {
        case 0:
            return S;
        case 1:
            return W;
        case 2:
            return N;
        case 3:
            return E;
        default:
            return null;
        }
    }

    /**
     * 
     * @return true if the direction is horizontal
     */
    public boolean isHorizontal() {
        return this == E || this == W;
    }

    /**
     * 
     * @param that
     *            a Direction
     * @return true if the direction that and the actual one are parallel.
     */
    public boolean isParallelTo(Direction that) {
        return (this == that || opposite() == that);
    }

}
