package com.soukuan.manyTomay;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class HelloSender {

    @Resource
    private AmqpTemplate rabbitTemplate;

    public void send(int index) {
        String context = "hello " + index;
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }

}