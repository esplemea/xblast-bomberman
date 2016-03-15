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

    public static <T> List<T> mirrored(List<T> l) {

        if (l.isEmpty()) {
            throw new IllegalArgumentException(
                    "The list to mirror cannot be empty");
        }

        List<T> subList = new ArrayList<T>(l.subList(0, l.size() - 1));
        Collections.reverse(subList);
        List<T> output = new ArrayList<T>(l);
        output.addAll(subList);
        return output;
    }

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
                for (List<T> a : temp) {
                    for (int i = 0; i <= a.size(); ++i) {
                        List<T> list = new ArrayList<T>(a);
                        list.add(i, l.get(0));
                        output.add(list);
                    }
                }
            }

            return output;
        }
    }
}
