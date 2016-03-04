package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Lists {

	private Lists() {
	}

	public static <T> List<T> mirrored(List<T> l){

		if (l.isEmpty()) {
			throw new IllegalArgumentException("The list to mirror cannot be empty");
		}
		
		List<T> subList = new ArrayList<T>(l.subList(0, l.size() - 1));
		Collections.reverse(subList);
		List<T> output = new ArrayList<T>(l);
		output.addAll(subList);
		return output;
	}
}
