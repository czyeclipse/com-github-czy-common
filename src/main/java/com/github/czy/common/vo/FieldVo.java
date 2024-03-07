package com.github.czy.common.vo;

import java.lang.reflect.Method;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 08:54
 */
public class FieldVo{
    private String name;
    private Method getMethod;
    private Method setMethod;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(Method getMethod) {
        this.getMethod = getMethod;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public void setSetMethod(Method setMethod) {
        this.setMethod = setMethod;
    }
}
