package com.yushu.flume.util;


import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import java.util.Map;

/**
 * map和object转换工具类
 *
 * @author yushu.zhao
 * @create 2020-09-16 09:44
 */
public class MapObjectTrans {


    /**
     * 将map装成实体类
     *
     * @param map
     * @param beanClass
     * @return
     * @throws ReflectiveOperationException
     */
    public static  Object map2object(Map<String, String> map, Class<?> beanClass) throws ReflectiveOperationException{
        if(map==null){
            return null;
        }
        Object object=beanClass.newInstance();
        BeanUtils.populate(object,map);
        return object;
    }



    /**
     * 实体类对象转成map
     *
     * @param object
     * @return
     */
    public static Map<?,?> object2map(Object object){
        if(object==null){
            return null;
        }
        return new BeanMap(object);
    }



    public static void main(String[] args){


        Map<String,Object> map = null;



    }



}
