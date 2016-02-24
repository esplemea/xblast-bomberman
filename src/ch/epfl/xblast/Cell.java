package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Cell {
	public final static int COLUMNS = 15;
	public final static int ROWS = 13;
	public final static int COUNT = ROWS * COLUMNS;
	public final static List<Cell> ROW_MAJOR_ORDER = Collections.unmodifiableList(rowMajorOrder());
	public final static List<Cell> ROW_SPIRAL_ORDER = Collections.unmodifiableList(rowSpiralOrder());
	private final int mx, my;

	Cell(int x, int y) {
		mx = Math.floorMod(x, COLUMNS);
		my = Math.floorMod(y, ROWS);
	}

	public int x() {
		return mx;
	}

	public int y() {
		return my;
	}

	public int rowMajorIndex() {
		return COLUMNS * my + mx;
	}

	/**
	 * Crée la liste des cellules ligne par ligne.
	 * 
	 * @return ArrayList des Cell triées
	 */
	private static ArrayList<Cell> rowMajorOrder() {
		ArrayList<Cell> cellules = new ArrayList<Cell>();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				cellules.add(new Cell(i, j));
			}
		}
		return cellules;
	}

	private static ArrayList<Cell> rowSpiralOrder() {
		return null;
	}

	public Cell neighbor(Direction dir) {
		switch (dir) {
		case N:
			if (this.my == 0) {
				return new Cell(this.mx, ROWS - 1);
			} else {
				return new Cell(this.mx, this.my - 1);
			}
		case S:
			if (this.my == ROWS - 1) {
				return new Cell(this.mx, 0);
			} else {
				return new Cell(this.mx, this.my + 1);
			}
		case E:
			if (this.mx == COLUMNS - 1) {
				return new Cell(0, this.my);
			} else {
				return new Cell(this.mx + 1, this.my);
			}
		case W:
			if (this.mx == 0) {
				return new Cell(COLUMNS - 1, this.my);
			} else {
				return new Cell(this.mx - 1, this.my);
			}
		default:
			return null;
		}
	}

	public boolean equals(Object that) {
		if (that.getClass() == this.getClass()) {
			Cell obj = (Cell) that;
			return (obj.my == this.my && obj.mx == this.mx);
		}
		return false;
	}

	public String toString() {
		return "(" + this.mx + "," + this.my + ")";
	}
}
