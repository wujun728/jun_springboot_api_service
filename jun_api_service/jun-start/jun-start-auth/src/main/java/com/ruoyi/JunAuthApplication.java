package com.ruoyi;

import com.ruoyi.common.cloud.annotation.AutoCloud;
import org.springframework.boot.SpringApplication;

/**
 * 认证微服务启动类
 *
 * @author ruoyi
 */
@AutoCloud
public class JunAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(JunAuthApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Jun Auth 微服务启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
