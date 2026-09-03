package com.yatop.lambda.net.websocket.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class JsonUtil
{

    //谷歌 GsonBuilder 构造器
    static GsonBuilder gb = new GsonBuilder();

    private static final Gson gson;

    static
    {
        //不需要html escape
        gb.disableHtmlEscaping();
        gson = gb.create();
    }

    //Object对象转成JSON字符串后，进一步转成字节数组
    public static byte[] object2JsonBytes(Object obj)
    {

        //把对象转换成JSON

        String json = pojoToJson(obj);
        try
        {
            return json.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    //使用谷歌 Gson 将 POJO 转成字符串
    public static String pojoToJson(Object obj)
    {
        //String json = new Gson().toJson(obj);
//        String json = JSON.toJSONString(obj);
        String json = gson.toJson(obj);
        return json;
    }


    public static <T> T jsonBytes2Object(byte[] bytes, Class<T> tClass)
    {

        //尽量把对象转换成JSON保存更稳妥
        try
        {
            String json = new String(bytes, "UTF-8");
            T t = jsonToPojo(json, tClass);
            return t;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //使用谷歌 Gson  将字符串转成 POJO对象
    public static <T> T jsonToPojo(String json, Class<T> tClass)
    {
//        T t = JSON.parseObject(json, tClass);
        T t = gson.fromJson(json, tClass);
        return t;
    }

    //使用阿里 Fastjson 将字符串转成 POJO对象
    public static <T> T jsonToPojo(String json, TypeReference<T> type)
    {
        T t = JSON.parseObject(json, type);
        return t;
    }


    //使用 谷歌 json 将字符串转成 POJO对象
    public static <T> T jsonToPojo(String json, Type type)
    {
        T t = gson.fromJson(json, type);
        return t;
    }


}
