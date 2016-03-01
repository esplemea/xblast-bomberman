package ch.epfl.xblast;

public enum Direction {
	N,E,S,W;
	
	/**
	 * 
	 * @return the opposite direction to the actual direction
	 */
	public Direction opposite(){
		switch(this.ordinal()){
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
	 * @return true is the direction is horizontal
	 */
	public boolean isHorizontal(){
		return this==E || this==W;
	}
	
	/**
	 * 
	 * @param a Direction that
	 * @return true if the direction that and the actual one are parallel.
	 */
	public boolean isParallelTo(Direction that){
		return (this==that||opposite()==that);
	}
	
	
}
