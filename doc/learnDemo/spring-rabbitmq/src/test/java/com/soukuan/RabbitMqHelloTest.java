package com.soukuan;

import com.soukuan.callback.CallBackSender;
import com.soukuan.domain.UserSender;
import com.soukuan.fanout.FanoutSender;
import com.soukuan.manyTomay.HelloSender;
import com.soukuan.manyTomay.HelloSender1;
import com.soukuan.topic.TopicSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqHelloTest {

    @Resource
    private HelloSender helloSender;

    @Resource
    private HelloSender1 helloSender1;

    @Resource
    private UserSender userSender;

    @Resource
    private FanoutSender fanoutSender;

    @Resource
    private TopicSender topicSender;

    @Resource
    private CallBackSender callBackSender;

    @Test
    public void manyToMany() {
        for (int i=0;i<100;i++){
            helloSender.send(i);
            helloSender1.send(i);
        }
    }

    @Test
    public void sendObject() {
        userSender.send();
    }

    @Test
    public void sendFanout() {
        fanoutSender.send();
    }

    @Test
    public void sendTopic() {
        topicSender.send();
    }

    @Test
    public void sendCallBack() {
        callBackSender.send();
    }

}