package com.itboy.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.StringUtils;

/**
 * @ClassName PasswordUitl
 * @Description 可自行抽离更改，颜值登录在MyShiroRealm62行。
 * @Author 超
 * @Date 2019/7/5 0005 12:05
 **/
public class PasswordUitl {

    public static  String queryPassword(String password,String salt) {
        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(salt)){
            throw new NullPointerException("生成密码参数不能为空");
        }
        SimpleHash hash = new SimpleHash("MD5",password, ByteSource.Util.bytes(salt),1024);
        return hash.toString();
    }
}
