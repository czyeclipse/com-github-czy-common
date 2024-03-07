package com.github.czy.common.json;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 08:54
 */
public interface IJsonEngine{
    <T> T jsonToObject(String content,Class<T> inputClass);
    <T> List<T> jsonToList(String content,Class<T> elementClass);
    <Key,Value> Map<Key,Value> jsonToMap(String content,Class<Key> keyClass,Class<Value> valueClass);
    <Key,Value> Map<Key,Value> objectToMap(Object object,Class<Key> keyClass,Class<Value> valueClass);
    <T,Key,Value> T mapToObject(Map<Key,Value> map,Class<T> outputClass);
    String objectToJson(Object object);
    <Body,DataVo> Body jsonToBodyWithDataVo(String content,Class<Body> bodyClass,Class<DataVo> dataVoClass);
}
