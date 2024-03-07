package com.github.czy.common;

import com.github.czy.common.json.JsonFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 11:58
 */
public class TransformUtils{

    @SuppressWarnings("unchecked")
    public static <T> T objectTypeTransform(Object value,Class<T> tClass){
        if(value==null){
            return null;
        }
        Class<?> valueClass=value.getClass();
        if(valueClass==tClass||tClass.isAssignableFrom(valueClass)){
            return (T)value;
        }
        if(tClass==String.class){
            if(valueClass==Long.class
                    ||valueClass==Double.class
                    ||valueClass==Integer.class
                    ||valueClass==Float.class
                    ||valueClass==Boolean.class
                    ||valueClass==Byte.class
                    ||valueClass==Short.class){
                return (T)value.toString();
            }
            return (T)JsonFactory.getJsonEngine().objectToJson(value);
        }
        if(value instanceof String){
            return JsonFactory.getJsonEngine().jsonToObject((String)value,tClass);
        }
        if(value instanceof Number){
            Number valueNumber=(Number)value;
            if(tClass==Long.class){
                return (T)(new Long(valueNumber.longValue()));
            }else if(tClass==Integer.class){
                return (T)(new Integer(valueNumber.intValue()));
            }else if(tClass==Double.class){
                return (T)(new Double(valueNumber.doubleValue()));
            }else if(tClass==Float.class){
                return (T)(new Float(valueNumber.floatValue()));
            }else if(tClass==Byte.class){
                return (T)(new Byte(valueNumber.byteValue()));
            }
        }
        String json=JsonFactory.getJsonEngine().objectToJson(value);
        if(json==null){
            return null;
        }
        return JsonFactory.getJsonEngine().jsonToObject(json,tClass);
    }

    @SuppressWarnings("unchecked")
    public static <Value> Map<String,Value> objectTypeTransformToMap(Object object,Class<Value> valueType){
        if(object==null){
            return null;
        }
        if(object instanceof Map){
            Map<String,Object> map=(Map<String,Object>)object;
            if(map.size()==0){
                return new HashMap<>();
            }
            if(valueType.isAssignableFrom(map.values().iterator().next().getClass())){
                return (Map<String,Value>)map;
            }
        }else if(object instanceof String){
            return JsonFactory.getJsonEngine().jsonToMap((String)object,String.class,valueType);
        }
        return JsonFactory.getJsonEngine().objectToMap(object,String.class,valueType);
    }


    @SuppressWarnings("unchecked")
    public static <T> List<T> objectTypeTransformToList(Object value,Class<T> elementClass){
        if(value==null){
            return null;
        }
        if(value instanceof List){
            List<?> tempList=(List<?>)value;
            if(tempList.size()>0){
                Object oneValue=tempList.get(0);
                if(oneValue!=null&&elementClass.isAssignableFrom(oneValue.getClass())){
                    return (List<T>)tempList;
                }
            }
        }else if(value instanceof String){
            return JsonFactory.getJsonEngine().jsonToList((String)value,elementClass);
        }
        return JsonFactory.getJsonEngine().jsonToList(JsonFactory.getJsonEngine().objectToJson(value),elementClass);
    }

}
