package xblast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import ch.epfl.xblast.Lists;

public class ListsTest {

    @Test
    public void mirroredWorksOnNonTrivialList() {
        List<String> a = Arrays.asList("k", "a", "y");
        a = Lists.mirrored(a);

        List<String> expected = Arrays.asList("k", "a", "y", "a", "k");

        assertEquals(expected, a);
    }

    @Test
    public void mirroredWorksOnTrivialList() {
        List<String> a = Arrays.asList("k");
        a = Lists.mirrored(a);

        List<String> expected = Arrays.asList("k");

        assertEquals(expected, a);
    }

    @Test(expected = IllegalArgumentException.class)
    public void mirroredFailsOnEmptyList() {
        List<String> a = Arrays.asList();
        a = Lists.mirrored(a);
    }

    @Test
    public void permutationsWorksOnNonTrivialList() {
        List<Integer> a = Arrays.asList(1, 2, 3);
        List<List<Integer>> permuted = Lists.permutations(a);

        assertEquals(6, permuted.size());

        assertTrue(permuted.contains(Arrays.asList(1, 2, 3)));
        assertTrue(permuted.contains(Arrays.asList(1, 3, 2)));
        assertTrue(permuted.contains(Arrays.asList(2, 1, 3)));
        assertTrue(permuted.contains(Arrays.asList(2, 3, 1)));
        assertTrue(permuted.contains(Arrays.asList(3, 1, 2)));
        assertTrue(permuted.contains(Arrays.asList(3, 2, 1)));
    }

}
