package com.github.czy.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 08:54
 */
public class FastJsonEngine implements IJsonEngine{

    public FastJsonEngine(){
        JSON.DEFAULT_GENERATE_FEATURE|=SerializerFeature.DisableCircularReferenceDetect.getMask();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T jsonToObject(String content,Class<T> inputClass){
        if(inputClass==String.class){
            return (T)content;
        }
        return JSONObject.parseObject(content,inputClass);
    }

    @Override
    public <T> List<T> jsonToList(String content,Class<T> elementClass){
        return JSONArray.parseArray(content,elementClass);
    }

    @Override
    public <Key,Value> Map<Key,Value> jsonToMap(String content,Class<Key> keyClass,Class<Value> valueClass){
        return JSONObject.parseObject(content,new TypeReference<Map<Key,Value>>(keyClass,valueClass){});
    }

    @Override
    public <Key,Value> Map<Key,Value> objectToMap(Object object,Class<Key> keyClass,Class<Value> valueClass){
        String json=objectToJson(object);
        return jsonToMap(json,keyClass,valueClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T,Key,Value> T mapToObject(Map<Key,Value> map,Class<T> outputClass){
        if(map==null){
            return null;
        }
        if(outputClass.isAssignableFrom(map.getClass())){
            return (T)map;
        }
        String json=objectToJson(map);
        return jsonToObject(json,outputClass);
    }

    @Override
    public String objectToJson(Object object){
        return JSONObject.toJSONString(object);
    }

    @Override
    public <Body,DataVo> Body jsonToBodyWithDataVo(String content,Class<Body> bodyClass,Class<DataVo> dataVoClass){
        return JSONObject.parseObject(content,buildType(bodyClass,dataVoClass));
    }

    public static Type buildType(Type... types){
        if(types==null||types.length==0){
            return null;
        }
        ParameterizedTypeImpl beforeType=null;
        for(int i=types.length-1;i>0;i--){
            if(beforeType==null){
                beforeType=new ParameterizedTypeImpl(new Type[]{types[i]},null,types[i-1]);
            }else{
                beforeType=new ParameterizedTypeImpl(new Type[]{beforeType},null,types[i-1]);
            }
        }
        return beforeType;
    }
}
