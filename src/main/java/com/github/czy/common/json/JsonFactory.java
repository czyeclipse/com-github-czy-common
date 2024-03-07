package com.github.czy.common.json;


import com.github.czy.common.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 08:54
 */
public class JsonFactory{
    protected static final Logger logger=LoggerFactory.getLogger(JsonFactory.class);


    public static final String JSON_PLUGIN_FASTJSON="com.alibaba.fastjson.JSONObject";
    public static final String JSON_PLUGIN_JACKSON="com.fasterxml.jackson.databind.ObjectMapper";


    public static String DEFAULT_JSON_PLUGIN=JSON_PLUGIN_FASTJSON;

    public static void useJacksonPlugin(){
        DEFAULT_JSON_PLUGIN=JSON_PLUGIN_JACKSON;
    }

    private static final class JsonEngineHolder{
        static final IJsonEngine jsonEngine=createEngineWithClassCheck();
    }

    private static IJsonEngine createEngineWithClassCheck(){
        //如果default的class存在，直接使用default指定的plugin
        if(StringUtils.hasText(DEFAULT_JSON_PLUGIN)&&ClassUtils.isExists(DEFAULT_JSON_PLUGIN)){
            logger.info("JsonFactory plugin init.default config plugin:{}",DEFAULT_JSON_PLUGIN);
            if(DEFAULT_JSON_PLUGIN.equals(JSON_PLUGIN_FASTJSON)){
                return new FastJsonEngine();
            }else if(DEFAULT_JSON_PLUGIN.equals(JSON_PLUGIN_JACKSON)){
                return new JacksonEngine();
            }
        }else{
            if(ClassUtils.isExists(JSON_PLUGIN_FASTJSON)){
                logger.info("JsonFactory plugin init.check has plugin:{}",JSON_PLUGIN_FASTJSON);
                return new FastJsonEngine();
            }else if(ClassUtils.isExists(JSON_PLUGIN_JACKSON)){
                logger.info("JsonFactory plugin init.check has plugin:{}",JSON_PLUGIN_JACKSON);
                return new JacksonEngine();
            }
        }
        throw new RuntimeException("json plugin not found.");
    }

    public static IJsonEngine getJsonEngine(){
        return JsonEngineHolder.jsonEngine;
    }
}
