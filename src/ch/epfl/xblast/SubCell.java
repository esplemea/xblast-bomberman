package ch.epfl.xblast;

import java.util.Objects;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 15, 2016
 *
 */

public final class SubCell {

    private final static int SUBCELL_SIZE = 16;
    private final static int SUBCELL_MIDDLE = (int) Math.ceil(SUBCELL_SIZE / 2);
    private final static int X_MAX = Cell.COLUMNS * SUBCELL_SIZE;
    private final static int Y_MAX = Cell.ROWS * SUBCELL_SIZE;
    private final int mx, my;

    /**
     * 
     * @return the x normalized position of the SubCell
     */
    public int x() {
        return mx;
    }

    /**
     * 
     * @return the y normalized position of the SubCell
     */
    public int y() {
        return my;
    }

    /**
     * 
     * @param that
     *            a Cell
     * @return the central SubCell of the cell that
     */
    public static SubCell centralSubCellOf(Cell that) {
        return new SubCell(SUBCELL_SIZE * that.x() + SUBCELL_MIDDLE,
                SUBCELL_SIZE * that.y() + SUBCELL_MIDDLE);
    }

    /**
     * Constructor for a cell. The numbers x and y are normalized
     * 
     * @param the
     *            x position of the SubCell
     * @param the
     *            y position of the SubCell
     */
    public SubCell(int x, int y) {
        this.mx = Math.floorMod(x, X_MAX);
        this.my = Math.floorMod(y, Y_MAX);
    }

    /**
     * 
     * @return the Manhattan distance from the SubCell to the closest central
     *         SubCell
     */
    public int distanceToCentral() {
        return Math.abs(mx % SUBCELL_SIZE - SUBCELL_MIDDLE)
                + Math.abs(my % SUBCELL_SIZE - SUBCELL_MIDDLE);
    }

    /**
     * 
     * @return true is the current SubCell is a central SubCell
     */
    public boolean isCentral() {
        return (mx % SUBCELL_SIZE == SUBCELL_MIDDLE
                && my % SUBCELL_SIZE == SUBCELL_MIDDLE);
    }

    /**
     * 
     * @param a
     *            Direction that
     * @return the SubCell next to the current one in the direction that.
     */
    public SubCell neighbor(Direction that) {
        int y = my;
        int x = mx;
        switch (that) {
        case N:
            y--;
            break;
        case S:
            y++;
            break;
        case E:
            x++;
            break;
        case W:
            x--;
            break;
        default:
            return null;
        }
        return new SubCell(x, y);
    }

    /**
     * 
     * @return the cell containing the current SubCell
     */
    public Cell containingCell() {
        int x = this.mx / SUBCELL_SIZE;
        int y = this.my / SUBCELL_SIZE;
        return new Cell(x, y);
    }

    /**
     * @param an
     *            Object that
     * @return true if the object that is a SubCell with the same position as
     *         the current one
     */
    public boolean equals(Object that) {
        if (that instanceof SubCell) {
            SubCell obj = (SubCell) that;
            return (obj.my == this.my && obj.mx == this.mx);
        }
        return false;
    }

    /**
     * @return a description of the SubCell on a String : "(x,y)"
     */
    public String toString() {
        return "(" + mx + "," + my + ")";
    }

    /**
     * hash using the the position of the containing Cell and the position of
     * the SubCell in it
     */
    @Override
    public int hashCode() {
        return my*Cell.COLUMNS*SUBCELL_SIZE+mx;
    }
}
