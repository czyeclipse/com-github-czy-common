package com.github.czy.common;


import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-07 08:54
 */
public class StringUtils{

    /**
     * 下划线转驼峰
     * @param line
     * @param withFirst
     * @return
     */
    public static String lineToHumpCase(String line,boolean withFirst){
        String[] splits=line.toLowerCase(Locale.ROOT).split("_");
        if(splits.length==1){
            if(withFirst){
                return line.substring(0,1).toUpperCase(Locale.ROOT)+line.substring(1);
            }else{
                return line;
            }
        }
        StringBuilder stringBuilder=new StringBuilder();
        if(withFirst){
            for(String split:splits){
                stringBuilder.append(split.substring(0,1).toUpperCase(Locale.ROOT)).append(split.substring(1));
            }
        }else{
            stringBuilder.append(splits[0]);
            for(int i=1;i<splits.length;i++){
                String split=splits[i];
                stringBuilder.append(split.substring(0,1).toUpperCase(Locale.ROOT)).append(split.substring(1));
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 驼峰转下划线
     * @param hump
     * @param withFirst
     * @return
     */
    public static String humpToLineCase(String hump,boolean withFirst){
        if(StringUtils.isBlank(hump)){
            return hump;
        }
        StringBuilder stringBuilder=new StringBuilder();
        char c=hump.charAt(0);
        if(withFirst&&Character.isUpperCase(c)){
            stringBuilder.append("_");
            stringBuilder.append(Character.toLowerCase(c));
        }else{
            stringBuilder.append(Character.toLowerCase(c));
        }
        for (int i=1;i<hump.length();i++){
            c=hump.charAt(i);
            if(Character.isUpperCase(c)){
                stringBuilder.append("_").append(Character.toLowerCase(c));
            }else{
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }


    public static boolean isBlank(String value){
        return value==null||"".equals(value)||"".equals(value.replace(" ",""));
    }
    public static boolean notBlank(String value){
        return value!=null&&!"".equals(value.replace(" ",""));
    }

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }
    public static boolean hasText(CharSequence str) {
        return (str != null && str.length() > 0 && containsText(str));
    }
    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String replaceStringWithStartAndEndFlag(String source, Map<String,String> map, String startFlag, String endFlag){
        int len=startFlag.length();
        int index=source.indexOf(startFlag);
        while(index!=-1){
            int start=index+len;
            int end=source.indexOf(endFlag,start);
            if(end!=-1){
                String key=source.substring(start,end);
                if(map.containsKey(key)&&map.get(key)!=null){
                    source=source.replace(startFlag+key+endFlag,map.get(key));
                }else{
                    source=source.replace(startFlag+key+endFlag,"");
                }
                index=source.indexOf(startFlag);
            }else{
                break;
            }
        }
        return source;
    }
}
