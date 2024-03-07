import com.github.czy.common.ClassUtils;
import com.github.czy.common.json.JsonFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 17:42
 */
public class ClassUtilsTest{
    @SuppressWarnings("unchecked")
    @Test
    public void newObjectTest(){
        String s=ClassUtils.newObject(String.class,"abc");
        Assert.assertEquals("abc",s);

        List<String> list=ClassUtils.newObject(ArrayList.class);
        list.add("1");
        Assert.assertEquals("[\"1\"]",JsonFactory.getJsonEngine().objectToJson(list));
    }
}
