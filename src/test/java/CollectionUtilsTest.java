import com.github.czy.common.CollectionUtils;
import com.github.czy.common.json.JsonFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 19:41
 */
public class CollectionUtilsTest{
    @Test
    public void elementsMapTest(){
        List<String> list=new ArrayList<>(Arrays.asList("2","1","3"));
        List<Integer> newList=CollectionUtils.elementsMap(list,Integer::parseInt);
        Assert.assertEquals(ArrayList.class.getName(),newList.getClass().getName());

        Set<String> set=new LinkedHashSet<>(Arrays.asList("2","1","3"));
        Set<Integer> newSet=CollectionUtils.elementsMap(set,Integer::parseInt);
        Assert.assertEquals(LinkedHashSet.class.getName(),newSet.getClass().getName());
        Assert.assertEquals("[2,1,3]",JsonFactory.getJsonEngine().objectToJson(newSet));

        set=new HashSet<>(Arrays.asList("2","1","3"));
        newSet=CollectionUtils.elementsMap(set,Integer::parseInt);
        Assert.assertEquals(HashSet.class.getName(),newSet.getClass().getName());

        Queue<String> queue=new ArrayDeque<>(Arrays.asList("2","1","3"));
        Queue<Integer> newQueue=CollectionUtils.elementsMap(queue,Integer::parseInt);
        Assert.assertEquals(ArrayDeque.class.getName(),newQueue.getClass().getName());
    }
}
