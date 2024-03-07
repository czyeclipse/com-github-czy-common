package com.github.czy.common;


import com.github.czy.common.tuple.Tuple3;
import com.github.czy.common.vo.FieldVo;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author chenzhiyong
 * @project common
 * @description
 * @since 2024-03-06 17:41
 */
public class ClassUtils{


    @SuppressWarnings({"rawtypes","unchecked"})
    public static List<FieldVo> getFieldListFromClass(Class clazz) throws Exception{
        Field[] fields=clazz.getDeclaredFields();
        List<FieldVo> resultList=new ArrayList<>(fields.length);
        for(Field field:fields){
            FieldVo fieldVo=new FieldVo();
            String name=field.getName();
            fieldVo.setName(name);
            String temp=name.substring(0,1).toUpperCase()+name.substring(1);
            fieldVo.setGetMethod(clazz.getMethod("get"+temp));
            fieldVo.setSetMethod(clazz.getMethod("set"+temp,field.getType()));
            resultList.add(fieldVo);
        }
        return resultList;
    }

    public static List<FieldVo> getAllFieldListFromClass(Class<?> tClass)throws Exception{
        List<FieldVo> fieldList=getFieldListFromClass(tClass);
        Class<?> superClass=tClass.getSuperclass();
        if(superClass!=Object.class){
            List<FieldVo> superFieldList=getAllFieldListFromClass(superClass);
            if(!superFieldList.isEmpty()){
                fieldList.addAll(superFieldList);
            }
        }
        return fieldList;
    }

    public static Field findFieldWithFieldName(Class<?> tClass,String fieldName){
        try{
            return tClass.getDeclaredField(fieldName);
        }catch(NoSuchFieldException ex){
            if(tClass.getSuperclass()!=Object.class){
                return findFieldWithFieldName(tClass.getSuperclass(),fieldName);
            }
            return null;
        }
    }
    public static Method findMethodWithMethodName(Class<?> tClass,String methodName,Class<?>... parameterTypes){
        try{
            return tClass.getDeclaredMethod(methodName,parameterTypes);
        }catch(NoSuchMethodException ex){
            if(tClass.getSuperclass()!=Object.class){
                return findMethodWithMethodName(tClass.getSuperclass(),methodName,parameterTypes);
            }
            return null;
        }
    }

    public static Tuple3<Field,Method,Method> findGetterSetterMethodsWithFieldName(Class<?> clazz,String fieldName){
        Field field=findFieldWithFieldName(clazz,fieldName);
        if(field==null){
            return null;
        }
        String methodName=fieldName.substring(0,1).toUpperCase(Locale.ROOT)+fieldName.substring(1);
        Method getMethod=findMethodWithMethodName(clazz,"get"+methodName);
        Method setMethod=findMethodWithMethodName(clazz,"set"+methodName,field.getType());
        return new Tuple3<>(field,getMethod,setMethod);
    }

    @SafeVarargs
    public static List<Field> getFieldsWithAnnotate(Class<?> javaType,Class<? extends Annotation>... annotateTypes){
        Field[] fields=javaType.getDeclaredFields();
        List<Field> resultList=new ArrayList<>();
        for(Field field:fields){
            for(Class<? extends Annotation> annotateType:annotateTypes){
                if(field.isAnnotationPresent(annotateType)){
                    resultList.add(field);
                    break;
                }
            }
        }
        if(javaType.getSuperclass()!=Object.class){
            resultList.addAll(getFieldsWithAnnotate(javaType.getSuperclass(),annotateTypes));
        }
        return resultList;
    }

    @SafeVarargs
    public static List<Method> getMethodsWithAnnotate(Class<?> javaType,Class<? extends Annotation>... annotateTypes){
        Method[] methods=javaType.getDeclaredMethods();
        List<Method> resultList=new ArrayList<>();
        for(Method method:methods){
            for(Class<? extends Annotation> annotateType:annotateTypes){
                if(method.isAnnotationPresent(annotateType)){
                    resultList.add(method);
                    break;
                }
            }
        }
        if(javaType.getSuperclass()!=Object.class){
            resultList.addAll(getMethodsWithAnnotate(javaType.getSuperclass(),annotateTypes));
        }
        Class<?>[] interfaces=javaType.getInterfaces();
        if(interfaces.length>0){
            for(Class<?> interfaceClass:interfaces){
                resultList.addAll(getMethodsWithAnnotate(interfaceClass,annotateTypes));
            }
        }
        return resultList;
    }


    public static Set<Class<?>> getClasses(String pack) {
        Set<Class<?>> classes=new LinkedHashSet<>();
        String packageName=pack;
        String packageDirName=packageName.replace('.','/');
        Enumeration<URL> dirs;
        try{
            dirs=Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while(dirs.hasMoreElements()){
                URL url=dirs.nextElement();
                String protocol=url.getProtocol();
                if("file".equals(protocol)){
                    String filePath=URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName,filePath, true, classes);
                }else if("jar".equals(protocol)){
                    JarFile jar;
                    try{
                        jar=((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries=jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry=entries.nextElement();
                            String name=entry.getName();
                            if(name.charAt(0)=='/'){
                                name=name.substring(1);
                            }
                            if(name.startsWith(packageDirName)){
                                int idx=name.lastIndexOf('/');
                                if(idx!=-1){
                                    packageName=name.substring(0, idx).replace('/', '.');
                                }
                                if(name.endsWith(".class")&&!entry.isDirectory()){
                                    String className=name.substring(packageName.length()+1, name.length()-6);
                                    try{
                                        classes.add(Class.forName(packageName + '.'+ className));
                                    }catch(ClassNotFoundException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName,
                                                        String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles = dir.listFiles(file -> (recursive && file.isDirectory())||(file.getName().endsWith(".class")));
        if(dirFiles==null){
            return;
        }
        for(File file:dirFiles){
            if(file.isDirectory()){
                findAndAddClassesInPackageByFile(packageName+"."+file.getName(),file.getAbsolutePath(),recursive,classes);
            }else{
                String className=file.getName().substring(0,file.getName().length() - 6);
                try{
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                }catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static boolean isExists(String className){
        try{
            Class.forName(className);
            return true;
        }catch(ClassNotFoundException ex){
            return false;
        }
    }

    public static Type[] getActualTypeArgumentsFromClass(Class<?> clazz){
        Type type=clazz.getGenericSuperclass();
        ParameterizedType parameterizedType=(ParameterizedType)type;
        return parameterizedType.getActualTypeArguments();
    }

    public static Class<?> getFirstActualClassArgumentsFromClass(Class<?> clazz){
        Type type=getActualTypeArgumentsFromClass(clazz)[0];
        if(type instanceof Class){
            return (Class<?>) type;
        }else if(type instanceof ParameterizedType){
            ParameterizedType parameterizedType=(ParameterizedType)type;
            return (Class<?>)parameterizedType.getRawType();
        }else if(type instanceof TypeVariable){
            TypeVariable<?> typeVariable=(TypeVariable<?>) type;

        }else{
            return null;
        }
        return (Class<?>) type;
    }

    public static boolean canCastTo(Class<?> from,Class<?> to){
        return to.isAssignableFrom(from);
    }
}
