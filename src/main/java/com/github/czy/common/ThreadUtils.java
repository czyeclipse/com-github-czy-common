package com.github.czy.common;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 11:52
 */
public class ThreadUtils{
    public static void trySleep(long milliSeconds){
        try{
            Thread.sleep(milliSeconds);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
