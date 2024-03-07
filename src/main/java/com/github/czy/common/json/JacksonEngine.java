package com.github.czy.common.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.czy.common.ClassUtils;
import com.github.czy.common.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 08:54
 */
public class JacksonEngine implements IJsonEngine{

    protected static final Logger logger=LoggerFactory.getLogger(JacksonEngine.class);

    public static ObjectMapper objectMapper=JacksonUtils.commonObjectMapper;

    @Override
    public <T> T jsonToObject(String content,Class<T> inputClass){
        try{
            return objectMapper.readValue(content,inputClass);
        }catch(IOException ex){
            logger.error("",ex);
            return null;
        }
    }

    @Override
    public <T> List<T> jsonToList(String content,Class<T> elementClass){
        try{
            JavaType javaType=objectMapper.getTypeFactory().constructParametricType(List.class,elementClass);
            return objectMapper.readValue(content,javaType);
        }catch(IOException ex){
            logger.error("",ex);
            return null;
        }
    }

    @Override
    public <Key,Value> Map<Key,Value> jsonToMap(String content,Class<Key> keyClass,Class<Value> valueClass){
        try{
            JavaType mapJavaType=objectMapper.getTypeFactory().constructParametricType(Map.class,keyClass,valueClass);
            return objectMapper.readValue(content,mapJavaType);
        }catch(IOException ex){
            logger.error("",ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Key,Value> Map<Key,Value> objectToMap(Object object,Class<Key> keyClass,Class<Value> valueClass){
        if(object==null){
            return null;
        }
        if(object instanceof Map){
            return (Map<Key,Value>)object;
        }
        if(object instanceof String){
            return jsonToMap((String)object,keyClass,valueClass);
        }
        String json=objectToJson(object);
        if(json==null){
            return null;
        }
        return jsonToMap(json,keyClass,valueClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T,Key,Value> T mapToObject(Map<Key,Value> map,Class<T> outputClass){
        if(map==null){
            return null;
        }
        if(ClassUtils.canCastTo(map.getClass(),outputClass)){
            return (T)map;
        }
        String json=objectToJson(map);
        if(json==null){
            return null;
        }
        return jsonToObject(json,outputClass);
    }

    @Override
    public String objectToJson(Object object){
        try{
            return objectMapper.writeValueAsString(object);
        }catch(Exception ex){
            logger.error("",ex);
            return null;
        }
    }

    @Override
    public <Body,DataVo> Body jsonToBodyWithDataVo(String content,Class<Body> bodyClass,Class<DataVo> dataVoClass){
        try{
            JavaType bodyJavaType=objectMapper.getTypeFactory().constructParametricType(bodyClass,dataVoClass);
            return objectMapper.readValue(content,bodyJavaType);
        }catch(Exception ex){
            logger.error("",ex);
            return null;
        }
    }
}
