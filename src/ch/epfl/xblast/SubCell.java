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
}
