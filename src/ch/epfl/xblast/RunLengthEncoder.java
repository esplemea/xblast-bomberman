package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 26, 2016
 *
 */

public final class RunLengthEncoder {

	/**
	 * RunLengthEncoder can't be initiated
	 */
	private RunLengthEncoder() {
	};

	/**
	 * Encode a list a byte into another one, coded by groups
	 * 
	 * @param bytes
	 * @return
	 */
	public static List<Byte> encode(List<Byte> bytes) {
		int counter = 0;
		byte lastB = 0;
		List<Byte> output = new ArrayList<>();
		for (Byte b : bytes) {
			if (!(lastB == b && ++counter < 130)) {
				if (counter < 3)
					output.addAll(Collections.nCopies(counter, lastB));
				else {
					output.add((byte) (-counter + 2));
					output.add(lastB);
				}
				counter = counter == 130 ? 0 : 1;
			}
			lastB = b;
		}
		if (counter < 3)
			output.addAll(Collections.nCopies(counter, lastB));
		else {
			output.add((byte) (-counter + 2));
			output.add(lastB);
		}
		return output;
	}


	/**
	 * Decode a list of grouped bytes into a normal list of bytes
	 * 
	 * @param bytes
	 * @return
	 */
	public static List<Byte> decode(List<Byte> bytes) {
		List<Byte> output = new ArrayList<>();
		int byteRepetition = 1;
		for (byte currentByte : bytes) {
			if (currentByte < 0) {
				byteRepetition = -currentByte + 2;
			} else {
				output.addAll(Collections.nCopies(byteRepetition, currentByte));
				byteRepetition = 1;
			}
		}
		return output;
	}
}
