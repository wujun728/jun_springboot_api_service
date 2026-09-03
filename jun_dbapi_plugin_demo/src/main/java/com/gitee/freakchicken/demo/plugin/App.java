package com.gitee.freakchicken.demo.plugin;

public class App {
    /**
     * 测试redis连接
     * @param args
     */
    public static void main(String[] args) {
        RedisCachePlugin plugin = new RedisCachePlugin();
        plugin.init();
        plugin.testCollection();
    }
}
