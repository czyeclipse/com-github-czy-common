package com.github.czy.common;

import java.util.*;
import java.util.function.Function;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-06 17:41
 */
public class CollectionUtils{
    public static boolean isEmpty(Map<?,?> map){
        return map==null||map.isEmpty();
    }
    public static boolean isEmpty(Collection<?> collection){
        return collection==null||collection.isEmpty();
    }
    public static void trySleep(int milliSeconds){
        try{
            Thread.sleep(milliSeconds);
        }catch(Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static <Input,Output> Set<Output> elementsMap(Set<Input> inputSet,Function<Input,Output> mapper){
        if(isEmpty(inputSet)){
            return Collections.emptySet();
        }
        Set<Output> outputSet=new HashSet<>(inputSet.size());
        for(Input input:inputSet){
            Output output=mapper.apply(input);
            if(output!=null){
                outputSet.add(output);
            }
        }
        return outputSet;
    }
}
