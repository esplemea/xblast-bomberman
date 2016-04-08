package ch.epfl.xblast.server.debug.geometry;

/**
 *@author Alexandre MICHEL
 *@date 9 mars 2016
 *
 *@version 1.0
 */
public final class Vector2D {
	private final int mX, mY;
	
	public Vector2D(int x, int y) {
		mX =x;
		mY = y;
	}
	
	public int x() {
		return mX;
	}
	
	public int y() {
		return mY;
	}
	
	@Override
	public String toString() {
		return "("+mX+", "+mY+")";
	}
}
