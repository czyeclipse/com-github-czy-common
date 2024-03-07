package com.github.czy.common;

import com.github.czy.common.tuple.Tuple2;

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
    public static <Input,Output> Set<Output> elementsMap(Set<Input> inputSet,Function<Input,Output> mapper){
        return (Set<Output>)elementsMap((Collection<Input>)inputSet,mapper);
    }

    public static <Input,Output> List<Output> elementsMap(List<Input> inputList,Function<Input,Output> mapper){
        return (List<Output>)elementsMap((Collection<Input>)inputList,mapper);
    }

    public static <Input,Output> Queue<Output> elementsMap(Queue<Input> inputList,Function<Input,Output> mapper){
        return (Queue<Output>)elementsMap((Collection<Input>)inputList,mapper);
    }

    @SuppressWarnings("unchecked")
    public static <Input,Output> Collection<Output> elementsMap(Collection<Input> inputCollection,Function<Input,Output> mapper){
        if(isEmpty(inputCollection)){
            return null;
        }
        Collection<Output> outputCollection=ClassUtils.newObject(inputCollection.getClass());
        for(Input input:inputCollection){
            Output output=mapper.apply(input);
            if(output!=null){
                outputCollection.add(output);
            }
        }
        return outputCollection;
    }

    @SuppressWarnings("unchecked")
    public static <InputKey,InputValue,OutputKey,OutputValue> Map<OutputKey,OutputValue> keyValueMap(Map<InputKey,InputValue> map,Function<InputKey,OutputKey> keyMapper,Function<InputValue,OutputValue> valueMapper){
        Map<OutputKey,OutputValue> resultMap=ClassUtils.newObject(map.getClass());
        map.forEach((key,value)->{
            OutputKey outputKey=keyMapper.apply(key);
            if(outputKey==null){
                return;
            }
            OutputValue outputValue=valueMapper.apply(value);
            if(outputValue==null){
                return;
            }
            resultMap.put(outputKey,outputValue);
        });
        return resultMap;
    }

    public static <InputKey,InputValue,OutputKey,OutputValue> Map<OutputKey,OutputValue> entryMap(Map<InputKey,InputValue> map,Function<Map.Entry<InputKey,InputValue>,Tuple2<OutputKey,OutputValue>> entryMapper){
        Map<OutputKey,OutputValue> resultMap=new HashMap<>();
        map.entrySet().forEach(entry->{
            Tuple2<OutputKey,OutputValue> outputTuple=entryMapper.apply(entry);
            if(outputTuple!=null&&outputTuple.getT1()!=null){
                resultMap.put(outputTuple.getT1(),outputTuple.getT2());
            }
        });
        return resultMap;
    }
}
