package com.soukuan.receiver;

import com.alibaba.fastjson.JSONObject;
import com.soukuan.domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //发送消息方法
    public void send() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        String messageStr = JSONObject.toJSONString(message);
        log.info("+++++++++++++++++++++  message = {}",messageStr);
        kafkaTemplate.send("test", messageStr);
    }
}