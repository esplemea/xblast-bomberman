package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Cell {
	public final static int COLUMNS = 15;
	public final static int ROWS = 13;
	public final static int COUNT = ROWS * COLUMNS;
	public final static List<Cell> ROW_MAJOR_ORDER = Collections.unmodifiableList(rowMajorOrder());
	public final static List<Cell> SPIRAL_ORDER = Collections.unmodifiableList(rowSpiralOrder());
	private final int mx, my;

	public Cell(int x, int y) {
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
				cellules.add(new Cell(j, i));
			}
		}
		return cellules;
	}

	private static ArrayList<Cell> rowSpiralOrder() {
		int x = 0, y = 0;
		ArrayList<Cell> cellules = new ArrayList<Cell>();
		do {
			for (int i = x; i < COLUMNS - x; i++) {
				cellules.add(new Cell(i, y));
			}
			for (int j = y + 1; j < ROWS - y; j++) {
				cellules.add(new Cell(COLUMNS - x - 1, j));
			}
			if (x < COLUMNS - x - 1 && y < ROWS - y - 1) {
				for (int i = COLUMNS - x - 2; i > x; i--) {
					cellules.add(new Cell(i, ROWS - y - 1));
				}
				for (int j = ROWS - y - 1; j > y; j--) {
					cellules.add(new Cell(x, j));
				}
			}
			x++;
			y++;
		} while (x < COLUMNS - x && y < ROWS - y);
		return cellules;
	}

	public Cell neighbor(Direction dir) {
		int y = my;
		int x = mx;
		switch (dir) {
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
		return new Cell(x, y);
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
