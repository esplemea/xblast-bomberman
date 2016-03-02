package ch.epfl.xblast;

public final class SubCell {
	public final static int SUBCELL_SIZE = 16;
	public final static int X_MAX = Cell.COLUMNS * SUBCELL_SIZE;// largeur du
																// plateau
	public final static int Y_MAX = Cell.ROWS * SUBCELL_SIZE;// hauteur du
																// plateau
	private final int mx, my;

	/**
	 * 
	 * @return the x normalized position of the subcell
	 */
	public int x() {
		return mx;
	}

	/**
	 * 
	 * @return the y normalized position of the subcell
	 */
	public int y() {
		return my;
	}

	/**
	 * 
	 * @param a Cell that
	 * @return the central subcell of the cell that
	 */
	public static SubCell centralSubCellOf(Cell that) {
		return new SubCell(SUBCELL_SIZE * that.x() + 8, SUBCELL_SIZE * that.y() + 8);
	}

	/**
	 * Constructor for a cell. The numbers x and y are normalized
	 * @param the x position of the SubCell
	 * @param the y position of the SubCell
	 */
	public SubCell(int x, int y) {
		this.mx = Math.floorMod(x, X_MAX);
		this.my = Math.floorMod(y, Y_MAX);
	}

	/**
	 * 
	 * @return the Manhattan distance from the subcell to the closest central subcell
	 */
	public int distanceToCentral() {
		int total = Math.abs(mx % SUBCELL_SIZE - 8);
		total += Math.abs(my % SUBCELL_SIZE - 8);
		return total;
	}

	/**
	 * 
	 * @return true is the current subcell is a central subcell
	 */
	public boolean isCentral() {
		return (mx % SUBCELL_SIZE == 8 && my % SUBCELL_SIZE == 8);
	}

	/**
	 * 
	 * @param a Direction that
	 * @return the subcell next to the current one in the direction that.
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
	 * @return the cell containing the current subcell
	 */
	public Cell containingCell() {
		int x = this.mx / SUBCELL_SIZE;
		int y = this.my / SUBCELL_SIZE;
		return new Cell(x, y);
	}

	/**
	 * @param an Object that
	 * @return true if the object that is a subcell with the same position as the current one
	 */
	public boolean equals(Object that) {
		if (that.getClass() == this.getClass()) {
			SubCell obj = (SubCell) that;
			return (obj.my == this.my && obj.mx == this.mx);
		}
		return false;
	}

	/**
	 * @return a description of the subcell on a String : "(x,y)"
	 */
	public String toString() {
		return "("+mx+","+my+")";
	}
}
