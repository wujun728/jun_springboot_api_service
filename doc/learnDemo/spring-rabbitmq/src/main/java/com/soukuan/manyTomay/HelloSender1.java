package com.soukuan.manyTomay;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class HelloSender1 {

    @Resource
    private AmqpTemplate rabbitTemplate;

    public void send(int index) {
        String context = "hello " + index;
        System.out.println("Sender1 : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }

}