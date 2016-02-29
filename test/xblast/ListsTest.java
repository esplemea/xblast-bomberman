package xblast;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.Lists;

public class ListsTest {
    
    @Test
    public void mirroredWorksOnNonTrivialList()
    {
        List<String> a = Arrays.asList("k", "a", "y");
        a = Lists.mirrored(a);
        
        List<String> expected = Arrays.asList("k", "a", "y", "a", "k");
        
        assertEquals(expected, a);
    }

}
