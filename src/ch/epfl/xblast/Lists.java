package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date March 4, 2016
 *
 */

public final class Lists {

	private Lists() {
	}

	/**
	 * Mirror the list on the middle element of the list
	 * </p>Example : ("k","a","y") --> ("k","a","y","a","k")
	 * @param l
	 * @return A mirrored List<T>
	 */
	public static <T> List<T> mirrored(List<T> l) {

		if (l.isEmpty()) {
			throw new IllegalArgumentException("The list to mirror cannot be empty");
		}

		List<T> subList = new ArrayList<T>(l.subList(0, l.size() - 1));
		Collections.reverse(subList);
		List<T> output = new ArrayList<T>(l);
		output.addAll(subList);
		return output;
	}

	/**
	 * 
	 * @param l
	 * @return the List of every possible permutation of the T in l
	 */
	public static <T> List<List<T>> permutations(List<T> l) {
		if (l.isEmpty()) {
			return new ArrayList<List<T>>();
		} else {
			List<T> subList = new ArrayList<T>(l.subList(1, l.size()));
			List<List<T>> temp = permutations(subList);
			List<List<T>> output = new ArrayList<List<T>>();
			if (temp.isEmpty())
				output.add(Arrays.asList(l.get(0)));
			else {
				List<T> list;
				for (List<T> a : temp) {
					for (int i = 0; i <= a.size(); ++i) {
						list = new ArrayList<T>(a);
						list.add(i, l.get(0));
						output.add(list);
					}
				}
			}

			return output;
		}
	}
}
