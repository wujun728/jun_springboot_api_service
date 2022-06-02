package com.soukuan.redisson.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by fangzhipeng on 2017/4/5.
 * 获取RedissonClient连接类
 */
@Component
public class RedissonConnector {

    RedissonClient redisson;

    @PostConstruct
    public void init(){
        Config config = new Config();
        config.useSingleServer().setAddress("localhost:6379")
                .setPassword("123456")
                //同任何节点建立连接时的等待超时。时间单位是毫秒。默认：10000
                .setConnectTimeout(30000)
                //等待节点回复命令的时间。该时间从命令发送成功时开始计时。默认:3000
                .setTimeout(10000)
                //如果尝试达到 retryAttempts（命令失败重试次数） 仍然不能将命令发送至某个指定的节点时，将抛出错误。如果尝试在此限制之内发送成功，则开始启用 timeout（命令等待超时） 计时。默认值：3
                .setRetryAttempts(5)
                //在一条命令发送失败以后，等待重试发送的时间间隔。时间单位是毫秒。     默认值：1500
                .setRetryInterval(3000);
        redisson = Redisson.create(config);
    }

    public RedissonClient getClient(){
        return redisson;
    }

}