package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 15, 2016
 *
 */

public final class Cell {

    public final static int COLUMNS = 15;
    public final static int ROWS = 13;
    public final static int COUNT = ROWS * COLUMNS;
    public final static List<Cell> ROW_MAJOR_ORDER = Collections
            .unmodifiableList(rowMajorOrder());
    public final static List<Cell> SPIRAL_ORDER = Collections
            .unmodifiableList(rowSpiralOrder());
    private final int mx, my;

    /**
     * Constructor for a cell. The numbers x and y are normalized
     * 
     * @param x
     *            position of the Cell in the rows
     * @param y
     *            position of the Cell in the columns
     */
    public Cell(int x, int y) {
        mx = Math.floorMod(x, COLUMNS);
        my = Math.floorMod(y, ROWS);
    }

    /**
     * 
     * @return the x normalized position of the cell
     */
    public int x() {
        return mx;
    }

    /**
     * 
     * @return the y normalized position of the cell
     */
    public int y() {
        return my;
    }

    /**
     * 
     * @return the index position of the cell in the reading order
     */
    public int rowMajorIndex() {
        return COLUMNS * my + mx;
    }

    /**
     * 
     * @return an ArrayList of the cells in the reading order
     */
    private static ArrayList<Cell> rowMajorOrder() {
        ArrayList<Cell> cellules = new ArrayList<Cell>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                cellules.add(new Cell(j, i));
            }
        }
        return cellules;
    }

    /**
     * 
     * @return an ArrayList of the cells in the spiral order, starting from the
     *         top left corner to the right.
     */
    private static ArrayList<Cell> rowSpiralOrder() {

        ArrayList<Cell> output = new ArrayList<>();

        ArrayList<Integer> ix = new ArrayList<>();
        ArrayList<Integer> iy = new ArrayList<>();
        for (int i = 0; i < COLUMNS; ++i)
            ix.add(i);
        for (int i = 0; i < ROWS; ++i)
            iy.add(i);
        boolean horizontal = true;

        do {
            ArrayList<Integer> i1, i2;
            i1 = horizontal ? ix : iy;
            i2 = horizontal ? iy : ix;
            int c2 = i2.remove(0);
            for (int c1 : i1) {
                Cell c;
                c = horizontal ? new Cell(c1, c2) : new Cell(c2, c1);
                output.add(c);
            }
            Collections.reverse(i1);
            horizontal = !horizontal;

        } while (!ix.isEmpty() && !iy.isEmpty());

        return output;
    }

    /**
     * 
     * @param that
     *            Direction of the neighbor
     * @return the cell next to the current one in the direction that.
     */
    public Cell neighbor(Direction that) {
        int y = my;
        int x = mx;
        switch (that) {
        case N:
            --y;
            break;
        case S:
            ++y;
            break;
        case E:
            ++x;
            break;
        case W:
            --x;
            break;
        default:
            return null;
        }
        return new Cell(x, y);
    }

    /**
     * @param that
     *            the Object to check
     * @return true if the object that is a cell with the same position as the
     *         current one.
     */
    public boolean equals(Object that) {
        if (that.getClass() == this.getClass()) {
            Cell obj = (Cell) that;
            return (obj.my == this.my && obj.mx == this.mx);
        }
        return false;
    }

    /**
     * @return a description of the cell on a String : "(x,y)"
     */
    public String toString() {
        return "(" + this.mx + "," + this.my + ")";
    }

    /**
     * hash using the rowMajorIndex
     */
    @Override
    public int hashCode() {
        return Objects.hash(rowMajorIndex());
    }
}
