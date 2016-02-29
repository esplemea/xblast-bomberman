package ch.epfl.xblast;

public final class SubCell {
	public final static int SUBCELL_SIZE = 16;
	public final static int X_MAX = Cell.COLUMNS * SUBCELL_SIZE;// largeur du
																// plateau
	public final static int Y_MAX = Cell.ROWS * SUBCELL_SIZE;// hauteur du
																// plateau
	private final int mx, my;

	public int x() {
		return mx;
	}

	public int y() {
		return my;
	}

	public static SubCell centralSubCellOf(Cell cell) {
		return new SubCell(SUBCELL_SIZE * cell.x() + 8, SUBCELL_SIZE * cell.y() + 8);
	}

	public SubCell(int x, int y) {
		this.mx = Math.floorMod(x, X_MAX);
		this.my = Math.floorMod(y, Y_MAX);
	}

	public int distanceToCentral() {
		int total = Math.abs(mx % SUBCELL_SIZE - 8);
		total += Math.abs(my % SUBCELL_SIZE - 8);
		return total;
	}

	public boolean isCentral() {
		return (mx % SUBCELL_SIZE == 8 && my % SUBCELL_SIZE == 8);
	}

	public SubCell neighbor(Direction dir) {
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
		return new SubCell(x, y);
	}

	public Cell containingCell() {
		int x = this.mx / SUBCELL_SIZE;
		int y = this.my / SUBCELL_SIZE;
		return new Cell(x, y);
	}

	public boolean equals(Object that) {
		if (that.getClass() == this.getClass()) {
			SubCell obj = (SubCell) that;
			return (obj.my == this.my && obj.mx == this.mx);
		}
		return false;
	}

	public String toString() {
		return "("+mx+","+my+")";
	}
}
