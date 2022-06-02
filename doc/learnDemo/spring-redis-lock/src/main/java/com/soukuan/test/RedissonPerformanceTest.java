package com.soukuan.test;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.CountDownLatch;

/**
 * redis的分布式锁测试
 * @author zhaoyq
 */
public class RedissonPerformanceTest {
    private static int TOTAL_PRE_THREADS = 10;
    private static long SECONDS = 10;
    protected static boolean isSkipLock = false;

    public static void main(String[] args) {
        Config config = new Config();
        // for single server
        config.useSingleServer()
        .setAddress("127.0.0.1:6379").setPassword("123456")
        .setConnectionPoolSize(10);
        final RedissonClient redissonCli = Redisson.create(config);

        final RLock lock = redissonCli.getLock("testLock");
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch endLatch = new CountDownLatch(TOTAL_PRE_THREADS);
        Thread[]  threads = new Thread[TOTAL_PRE_THREADS];
        setZero(redissonCli);

        for (int i = 0; i < TOTAL_PRE_THREADS; i++) {
            PerformanceThread runningThread = new PerformanceThread(startLatch,endLatch,lock,redissonCli);
            runningThread.start();
            threads[i]=runningThread;
        }

        //开始执行
        startLatch.countDown();
        try {
            Thread.sleep(SECONDS*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < threads.length; i++) {
            PerformanceThread runningThread =(PerformanceThread)threads[i];
            runningThread.stopThrad();
        }

        try {
            endLatch.await();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        printCount(redissonCli);
        redissonCli.shutdown();
    }


    public static class  PerformanceThread extends Thread{
        private volatile boolean isRunning = true;
        private CountDownLatch startLatch ;
        private CountDownLatch endLatch;
        private RLock lock;
        private RedissonClient redissonCli;
        private PerformanceThread(CountDownLatch startLatch ,CountDownLatch endLatch,RLock lock ,RedissonClient redissonCli){
            this.startLatch = startLatch;
            this.endLatch = endLatch;
            this.lock = lock;
            this.redissonCli =redissonCli;
        }

        @Override
        public void run() {
            try {
                startLatch.await();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            while (isRunning) {
                try {
                    if (!isSkipLock) {
                        lock.lock();
                    }
                    testUpdate(redissonCli);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (!isSkipLock) {
                            lock.unlock();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            endLatch.countDown();
        }

        public void stopThrad(){
            isRunning = false;
        }
    }

    /**
     * 更新对应的记录
     */
    public static void testUpdate(RedissonClient redissonCli) {
        RAtomicLong addLong = redissonCli.getAtomicLong("test_performance");
        long totalUsed = addLong.addAndGet(1);
    }

    /**
     * 最后获得总数
     * @param redissonCli
     */
    public static void printCount(RedissonClient redissonCli){
        RAtomicLong addLong = redissonCli.getAtomicLong("test_performance");
        System.out.println("count tps is:"+(addLong.get()/SECONDS));
    }

    /**
     * 初始化
     * @param redissonCli
     */
    public static void setZero(RedissonClient redissonCli){
        RAtomicLong addLong = redissonCli.getAtomicLong("test_performance");
        addLong.set(0);
    }
}