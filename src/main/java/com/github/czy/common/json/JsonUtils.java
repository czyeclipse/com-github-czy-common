package com.github.czy.common.json;

import java.util.List;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 08:54
 */
public class JsonUtils{
    @SuppressWarnings("unchecked")
    public static <T> T objectTypeTransform(Object value,Class<T> tClass){
        if(value==null){
            return null;
        }
        if(value.getClass()==tClass||tClass.isAssignableFrom(value.getClass())){
            return (T)value;
        }else{
            if(value instanceof String){
                return JsonFactory.getJsonEngine().jsonToObject((String)value,tClass);
            }else{
                String json=JsonFactory.getJsonEngine().objectToJson(value);
                if(json==null){
                    return null;
                }
                return JsonFactory.getJsonEngine().jsonToObject(json,tClass);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> objectTypeTransformToList(Object value,Class<T> elementClass){
        if(value==null){
            return null;
        }
        if(value instanceof List){
            List<?> tempList=(List<?>)value;
            if(tempList.size()>0){
                Class<?> valueClass=tempList.get(0).getClass();
                if(valueClass==elementClass||elementClass.isAssignableFrom(valueClass)){
                    return (List<T>)tempList;
                }
            }
        }else if(value instanceof String){
            return JsonFactory.getJsonEngine().jsonToList((String)value,elementClass);
        }
        return JsonFactory.getJsonEngine().jsonToList(JsonFactory.getJsonEngine().objectToJson(value),elementClass);
    }
}
