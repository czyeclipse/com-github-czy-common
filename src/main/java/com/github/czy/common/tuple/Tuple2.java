package com.github.czy.common.tuple;

import java.io.Serializable;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 08:54
 */
public class Tuple2<T1,T2> implements Serializable{
    private static final long serialVersionUID=-917179070440311759L;

    private T1 t1;
    private T2 t2;

    public Tuple2(){
    }

    public Tuple2(T1 t1,T2 t2){
        this.t1=t1;
        this.t2=t2;
    }

    public T1 getT1(){
        return t1;
    }

    public void setT1(T1 t1){
        this.t1=t1;
    }

    public T2 getT2(){
        return t2;
    }

    public void setT2(T2 t2){
        this.t2=t2;
    }

    public static <T1,T2> Tuple2<T1,T2> newTuple(T1 t1,T2 t2){
        return new Tuple2<>(t1,t2);
    }
}
