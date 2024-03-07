package com.github.czy.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 08:54
 */
@SuppressWarnings("unchecked")
public class JacksonUtils{
    public static ObjectMapper createCommonObjectMapper(){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }
    public static ObjectWriter createCommonObjectWriterWithClassName(){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writerWithDefaultPrettyPrinter();
    }
    public static ObjectMapper createCommonObjectMapperWithSnakeCase(){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    public static ObjectMapper createCommonObjectMapperWithUpperCamelCase(){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    public static ObjectMapper commonObjectMapper=createCommonObjectMapper();
    public static ObjectMapper commonObjectMapperWithSnakeCase=createCommonObjectMapperWithSnakeCase();


    public static ObjectWriter commonObjectWriterWithClassName=createCommonObjectWriterWithClassName();



    public static <T> T jsonToEntity(String json,Class<T> clazz) throws IOException {
        return commonObjectMapper.readValue(json,clazz);
    }

    public static <T> T jsonToEntityWithSnakeCase(String json,Class<T> clazz) throws IOException {
        return commonObjectMapperWithSnakeCase.readValue(json,clazz);
    }

    public static <T> T jsonToEntityQuiet(String json,Class<T> clazz){
        try{
            return commonObjectMapper.readValue(json,clazz);
        }catch(Exception ex){
            return null;
        }
    }
    public static <T> T jsonToEntityWithSnakeCaseQuiet(String json,Class<T> clazz){
        try{
            return commonObjectMapperWithSnakeCase.readValue(json,clazz);
        }catch(Exception ex){
            return null;
        }
    }


    public static String entityToJson(Object obj) throws IOException{
        return commonObjectMapper.writeValueAsString(obj);
    }

    /**
     * 不包括null的字段
     * @param obj
     * @return
     */
    public static String entityToJsonQuiet(Object obj){
        try{
            return commonObjectMapper.writeValueAsString(obj);
        }catch(Exception ex){
            return null;
        }
    }

    public static String entityToJsonNonNull(Object obj) throws IOException{
        return commonObjectMapper.writeValueAsString(obj);
    }

    public static <T> T jsonToEntity(String json, TypeReference<T> typeReference) throws IOException{
        return commonObjectMapper.readValue(json,typeReference);
    }
    public static <T> T jsonToEntityQuiet(String json, TypeReference<T> typeReference) {
        try{
            return commonObjectMapper.readValue(json,typeReference);
        }catch(Exception ex){
            return null;
        }
    }

    public static <T> T mapToEntity(Map<String,Object> map,Class<T> clazz){
        if(map==null){
            return null;
        }
        if(ClassUtils.canCastTo(map.getClass(),clazz)){
            return (T)map;
        }

        String json=JacksonUtils.entityToJsonQuiet(map);
        if(json==null){
            return null;
        }
        return JacksonUtils.jsonToEntityQuiet(json,clazz);
    }


    public static <T> List<T> jsonToEntityList(String json, Class<T> clazz)throws IOException{
        JavaType javaType=commonObjectMapper.getTypeFactory().constructParametricType(List.class,clazz);
        return commonObjectMapper.readValue(json,javaType);
    }

    public static <T> List<T> jsonToEntityListQuiet(String json,Class<T> clazz){
        try{
            return jsonToEntityList(json,clazz);
        }catch(Exception ex){
            return null;
        }
    }

    public static <T> Map<String,List<T>> jsonToMapEntityList(String json, Class<T> clazz) throws IOException{
        TypeFactory typeFactory=commonObjectMapper.getTypeFactory();
        JavaType listJavaType=typeFactory.constructParametricType(List.class,clazz);
        JavaType mapJavaType=typeFactory.constructParametricType(Map.class,typeFactory.constructType(String.class),listJavaType);
        return commonObjectMapper.readValue(json,mapJavaType);
    }

    public static <Key,Value> Map<Key,Value> jsonToMap(String json,Class<Key> keyClass,Class<Value> valueClass)throws IOException{
        JavaType mapJavaType=commonObjectMapper.getTypeFactory().constructParametricType(Map.class,keyClass,valueClass);
        return commonObjectMapper.readValue(json,mapJavaType);
    }
    public static <Key,Value> Map<Key,Value> jsonToMapQuiet(String json,Class<Key> keyClass,Class<Value> valueClass){
        JavaType mapJavaType=commonObjectMapper.getTypeFactory().constructParametricType(Map.class,keyClass,valueClass);
        try{
            return commonObjectMapper.readValue(json,mapJavaType);
        }catch(Exception ignore){
            return null;
        }
    }

    public static <Key,Value> Map<Key,Value> entityToMap(Object entity,Class<Key> keyClass,Class<Value> valueClass){
        if(entity instanceof Map){
            return (Map<Key,Value>)entity;
        }
        JavaType mapJavaType=commonObjectMapper.getTypeFactory().constructParametricType(Map.class,keyClass,valueClass);
        try{
            return commonObjectMapper.readValue(entityToJson(entity),mapJavaType);
        }catch(Exception ignore){
            return null;
        }
    }

    public static <U,V> Object jsonToComplexEntity(String json, Class<U> pClazz, Class<V> clazz)throws IOException{
        JavaType javaType=commonObjectMapper.getTypeFactory().constructParametricType(pClazz,clazz);
        return commonObjectMapper.readValue(json,javaType);
    }

    public static String dealWithJSON(String json) throws IOException{
        Object obj=commonObjectMapper.readValue(json,Object.class);
        return commonObjectMapper.writeValueAsString(obj);
    }

    public static String optStringFromJsonNode(JsonNode jsonNode, String fieldName, String defaultValue){
        JsonNode valueNode=jsonNode.findValue(fieldName);
        if(valueNode==null){
            return defaultValue;
        }
        return valueNode.asText();
    }


    public static Long optLongFromJsonNode(JsonNode jsonNode,String fieldName,Long defaultValue){
        JsonNode valueNode=jsonNode.findValue(fieldName);
        if(valueNode==null){
            return defaultValue;
        }
        return valueNode.asLong();
    }
    public static Integer optIntFromJsonNode(JsonNode jsonNode,String fieldName,Integer defaultValue){
        JsonNode valueNode=jsonNode.findValue(fieldName);
        if(valueNode==null){
            return defaultValue;
        }
        return valueNode.asInt();
    }
    public static Boolean optBooleanFromJsonNode(JsonNode jsonNode,String fieldName,Boolean defaultValue){
        JsonNode valueNode=jsonNode.findValue(fieldName);
        if(valueNode==null){
            return defaultValue;
        }
        return valueNode.asBoolean();
    }

    public static <T> List<T> optListFromJsonNode(JsonNode jsonNode,String fieldName,Class<T> elementType,List<T> defaultList){
        JsonNode valueNode=jsonNode.get(fieldName);
        if(valueNode==null){
            return defaultList;
        }
        List<T> resultList=jsonToEntityListQuiet(valueNode.asText(),elementType);
        if(resultList==null){
            return defaultList;
        }
        return resultList;
    }

    public static void writeStringToJsonGenerator(JsonGenerator gen, String fieldName, String value)throws IOException{
        if(value!=null){
            gen.writeStringField(fieldName,value);
        }
    }
    public static void writeNumberToJsonGenerator(JsonGenerator gen,String fieldName,Long value)throws IOException{
        if(value!=null){
            gen.writeNumberField(fieldName,value);
        }
    }
    public static void writeNumberToJsonGenerator(JsonGenerator gen,String fieldName,Integer value)throws IOException{
        if(value!=null){
            gen.writeNumberField(fieldName,value);
        }
    }
    public static void writeNumberToJsonGenerator(JsonGenerator gen,String fieldName,Double value)throws IOException{
        if(value!=null){
            gen.writeNumberField(fieldName,value);
        }
    }
    public static void writeBooleanToJsonGenerator(JsonGenerator gen,String fieldName,Boolean value)throws IOException{
        if(value!=null){
            gen.writeBooleanField(fieldName,value);
        }
    }
    public static ObjectNode newObjectNode(){
        return commonObjectMapper.createObjectNode();
    }
    public static ArrayNode newArrayNode(){
        return commonObjectMapper.createArrayNode();
    }

    public static ObjectNode parseObjectNode(String json)throws IOException{
        return (ObjectNode)commonObjectMapper.readTree(json);
    }
    public static ArrayNode parseArrayNode(String json)throws IOException{
        return (ArrayNode)commonObjectMapper.readTree(json);
    }

    public static <T> T jsonNodeToJavaObject(JsonNode jsonNode,Class<T> javaType){
        return jsonToEntityQuiet(jsonNode.asText(),javaType);
    }
}
