package com.soukuan.factory.singleton;

/**
 * 懒汉式
 * 是否 Lazy 初始化：是
 * 是否多线程安全：是
 * 实现难度：易
 * 必须加锁 synchronized 才能保证单例，但加锁会影响效率。
 */
public class Singleton {
    private static Singleton instance;

    private Singleton() {
    }

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}