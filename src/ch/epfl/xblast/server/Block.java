package ch.epfl.xblast.server;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date March 4, 2016
 *
 */


public enum Block {
	FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL;
	
	public boolean isFree() {
		return this == FREE;
	}

	public boolean canHostPlayer() {
		return this.isFree();
	}

	public boolean castsShadow() {
		return this == INDESTRUCTIBLE_WALL || this == DESTRUCTIBLE_WALL || this == CRUMBLING_WALL;
	}
}
