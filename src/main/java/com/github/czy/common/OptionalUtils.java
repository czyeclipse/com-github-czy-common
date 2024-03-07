package com.github.czy.common;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 08:54
 */
public class OptionalUtils{
    public static <T> void doIfNotNull(T object,Consumer<T> consumer){
        if(object!=null){
            consumer.accept(object);
        }
    }
    public static void doHasText(String value,Consumer<String> consumer){
        if(StringUtils.hasText(value)){
            consumer.accept(value);
        }
    }

    public static <T> void doHasElement(Collection<T> elements,Consumer<Collection<T>> consumer){
        if(!CollectionUtils.isEmpty(elements)){
            consumer.accept(elements);
        }
    }

    public static void doIfTrue(boolean flag,Runnable runnable){
        if(flag){
            runnable.run();
        }
    }

    public static <Key,Value> void doScanHasElement(Map<Key,Value> map,BiConsumer<Key,Value> biConsumer){
        if(!CollectionUtils.isEmpty(map)){
            map.forEach(biConsumer);
        }
    }

    public static <Input,Output> Output optIfNotNull(Input input,Function<Input,Output> outputGetter){
        return optIfNotNull(input,outputGetter,null);
    }

    public static <Input,Output> Output optIfNotNull(Input input,Function<Input,Output> outputGetter,Output defaultOutput){
        if(input==null){
            return defaultOutput;
        }
        Output output=outputGetter.apply(input);
        if(output==null){
            return defaultOutput;
        }
        return output;
    }
}
