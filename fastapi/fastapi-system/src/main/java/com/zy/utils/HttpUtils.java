package com.zy.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

import java.util.Map;

public class HttpUtils {

    /**
     * get请求工具类
     *
     * @param url     请求url
     * @param charset 字符编码,默认未UTF-8
     */
    public static String sendGet(String url, String charset) {
        String str = HttpUtil
                .createGet(url)
                //超时，毫秒
                .timeout(20000)
                .execute()
                .charset(charset)
                .body();
        return str;
    }


    /**
     * post请求工具类
     *
     * @param url     请求url
     * @param headMap head请求体
     * @param json    body请求体
     * @param charset 字符编码,默认未UTF-8
     */
    public static String sendPost(String url, Map<String, String> headMap, String json, String charset) {
        String str = HttpRequest.post(url)
                .addHeaders(headMap)
                .body(json)//表单内容
                .charset(charset)
                .timeout(20000)//超时，毫秒
                .execute().body();
        return str;
    }

}
