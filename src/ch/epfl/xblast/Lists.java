package ch.epfl.xblast;

import java.util.List;
import java.util.Collections;

public final class Lists {
    
    private Lists(){}
    
    public static <T> List<T> mirrored(List<T> l)
        throws IllegalArgumentException
    {
        int size = l.size();
        
        if(size==0)
        {
            throw new IllegalArgumentException();
        }
        else if(size==1)
        {
            return l;
        }
        else
        {
            List<T> subList = l.subList(0, size-2);
            Collections.reverse(subList);
            l.addAll(subList);
            return l;
        }
    }
        
    

}
