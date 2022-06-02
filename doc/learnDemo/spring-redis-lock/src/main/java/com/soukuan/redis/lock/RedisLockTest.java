package com.soukuan.redis.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class RedisLockTest {

    @Resource
    DistributedLockHandler distributedLockHandler;


    @GetMapping("/redis/lock")
    public void test() {
        Lock lock = new Lock("redis.lock", "test");
        if (distributedLockHandler.tryLock(lock)) {
            doSomething();
            distributedLockHandler.releaseLock(lock);
        }
    }

    private void doSomething() {
        System.out.println("应该做点什么");
    }
}
