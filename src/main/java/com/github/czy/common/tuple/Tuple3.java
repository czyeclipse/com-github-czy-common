package com.github.czy.common.tuple;

import java.io.Serializable;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 11:35
 */
public class Tuple3<T1,T2,T3> implements Serializable{
    private static final long serialVersionUID=1408263750679053225L;

    private T1 t1;
    private T2 t2;
    private T3 t3;

    public Tuple3(){
    }

    public Tuple3(T1 t1,T2 t2,T3 t3){
        this.t1=t1;
        this.t2=t2;
        this.t3=t3;
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

    public T3 getT3(){
        return t3;
    }

    public void setT3(T3 t3){
        this.t3=t3;
    }

    public static <T1,T2,T3> Tuple3<T1,T2,T3> newTuple(T1 t1,T2 t2,T3 t3){
        return new Tuple3<>(t1,t2,t3);
    }
}
