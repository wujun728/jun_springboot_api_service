package com.ruoyi.mq;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author 李文
 * @create 2020-06-15 8:57
 **/

@Configuration
@Component
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQConfiguration
{
    /**
     * MQ地址
     */
    //@Value("${rocketmq.NamesrvAddr}")
    private String namesrvAddr;

    /**
     * Producer 组名， 多个 Producer 如果属于一 个应用，发送同样的消息，则应该将它们 归为同一组
     */
    //@Value("${rocketmq.ProducerGroupName}")
    private String producerGroupName;

    /**
     * 生产者名称
     */
    //@Value("${rocketmq.InstanceName}")
    private String instanceName;
    /**
     * 超时时间 默认 20000
     */
    //@Value("${rocketmq.SendMsgTimeout:20000}")
    private int sendMsgTimeout;

    /**
     * 消费端组名
     */
    //@Value("${rocketmq.consumerGroup}")
    private String consumerGroup;


    private List<Map<String, String>> topics;

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public RocketMQConfiguration setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
        return this;
    }

    public String getProducerGroupName() {
        return producerGroupName;
    }

    public RocketMQConfiguration setProducerGroupName(String producerGroupName) {
        this.producerGroupName = producerGroupName;
        return this;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public RocketMQConfiguration setInstanceName(String instanceName) {
        this.instanceName = instanceName;
        return this;
    }

    public int getSendMsgTimeout() {
        return sendMsgTimeout;
    }

    public RocketMQConfiguration setSendMsgTimeout(int sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
        return this;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public RocketMQConfiguration setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
        return this;
    }

    public List<Map<String, String>> getTopics() {
        return topics;
    }

    public RocketMQConfiguration setTopics(List<Map<String, String>> topics) {
        this.topics = topics;
        return this;
    }
}
