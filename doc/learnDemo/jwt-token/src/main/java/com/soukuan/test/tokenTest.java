package com.soukuan.test;

import com.soukuan.util.JwtUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class tokenTest {

    public static void main(String[] args) {
        Map<String, Object> payload = new HashMap<String, Object>();
        Date date = new Date();
        payload.put("uid", "291969452");//用户id
        payload.put("iat", date.getTime());//生成时间
        payload.put("ext", date.getTime() + 1000 * 60 * 60);//过期时间1小时
        String token = null;
        token = JwtUtil.createToken(payload);
        System.out.println(token);
        System.out.println(JwtUtil.validToken(token));

    }
}
