package ch.epfl.xblast;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 15, 2016
 *
 */

public class ArgumentChecker {

	/**
	 * This object can't be initiate
	 */
	private ArgumentChecker() {
	}

	/**
	 * Check whether the value is positive or equal to 0.
	 * 
	 * @throws IllegalArgumentException
	 *             if the value is negative
	 * @param value
	 * @return value
	 */
	public static int requireNonNegative(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("The value must be positive or 0");
		}
		return value;
	}
}
