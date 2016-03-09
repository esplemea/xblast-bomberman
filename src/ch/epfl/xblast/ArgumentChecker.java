package ch.epfl.xblast;

public class ArgumentChecker {
	
	/**
	 * Check whether the value is positive or equal to 0. 
	 * @throws IllegalArgumentException if the value is negative
	 * @param value
	 * @return value
	 */
	int requireNonNegative(int value){
		if(value<0){
			throw new IllegalArgumentException("The value must be positive or 0");
		}
		return value;
	}
}
