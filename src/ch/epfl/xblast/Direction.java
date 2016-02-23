package ch.epfl.xblast;

public enum Direction {
	N,E,S,W;
	
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
	
	public boolean isHorizontal(){
		return this==E || this==W;
	}
	
	public boolean isParallelTo(Direction that){
		return (this==that||opposite()==that);
	}
	
	
}
