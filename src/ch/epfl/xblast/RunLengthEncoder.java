package ch.epfl.xblast;

import java.util.ArrayList;
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
		List<Byte> output = new ArrayList<>();
		byte byteRepetition = 0;
		byte actualByte = bytes.get(0);
		for (byte currentByte : bytes) {
			if (actualByte == currentByte) {
				byteRepetition++;
				if (byteRepetition == 130) {
					output.add((byte) -128);
					output.add(actualByte);
					byteRepetition = 0;
				}
			} else {
				switch (byteRepetition) {
				case 2:
					output.add(actualByte);
				case 1:
					output.add(actualByte);
					break;
				case 0:
					break;
				default:
					output.add((byte) (-byteRepetition + 2));
					output.add(actualByte);
				}
				byteRepetition = 1;
				actualByte = currentByte;
			}
		}
		switch (byteRepetition) {
		case 2:
			output.add(actualByte);
		case 1:
			output.add(actualByte);
			break;
		case 0:
			break;
		default:
			output.add((byte) (-byteRepetition + 2));
			output.add(actualByte);
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
		byte byteRepetition = 0;
		for (byte currentByte : bytes) {
			if (currentByte < 0) {
				byteRepetition = (byte)(-currentByte + 2);
			} else{
				for (int i = 0; i <= byteRepetition; ++i) {
					output.add(currentByte);
				}
				byteRepetition=0;
			}
		}
		return output;
	}
}
