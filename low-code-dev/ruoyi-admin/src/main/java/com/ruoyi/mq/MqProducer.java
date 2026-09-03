package com.ruoyi.mq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.rocketmq.common.message.Message;
import javax.annotation.PreDestroy;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MqProducer
{

    private DefaultMQProducer producer;

    private static final Logger LOGGER = LoggerFactory.getLogger(MqProducer.class);


    public MqProducer init(RocketMQConfiguration configuration) throws MQClientException {
        DefaultMQProducer defaultProducer = new DefaultMQProducer();
        // Producer 组名， 多个 Producer 如果属于一 个应用，发送同样的消息，则应该将它们 归为同一组
        defaultProducer.setProducerGroup(configuration.getProducerGroupName());
        // Name Server 地址列表
        defaultProducer.setNamesrvAddr(configuration.getNamesrvAddr());
        // 生产者名称
        defaultProducer.setInstanceName(configuration.getInstanceName());
        // 超时时间
        defaultProducer.setSendMsgTimeout(configuration.getSendMsgTimeout());
        defaultProducer.start();
        producer = defaultProducer;
        return this;
    }

    @PreDestroy
    private void Destroy() {
        producer.shutdown();
    }

    /**
     * 发送数据到MQ方法    数据编码格式  默认UTF-8
     *
     * @param Topic 队列名称
     * @param Tags  标签名称
     * @param body  发送的数据  推荐 JSOM 或者 XML 结构
     * @return 响应信息进行了内部处理  确认已经保存到 MQ 并且    日志已经记录  只要值不是NULL 就是成功发送
     * @throws UnsupportedEncodingException 转换字符集出错  请检查是否可以转换
     */
    public SendResult send(String Topic, String Tags, String body)
            throws UnsupportedEncodingException {
        return this.send(Topic, Tags, body, null, null);
    }

    /**
     * 发送数据到MQ方法    数据编码格式  默认UTF-8
     *
     * @param Topic 队列名称
     * @param Tags  标签名称
     * @param body  发送的数据  推荐 JSOM 或者 XML 结构
     * @param keys  多个属性
     * @return 响应信息进行了内部处理  确认已经保存到 MQ 并且    日志已经记录  只要值不是NULL 就是成功发送
     * @throws UnsupportedEncodingException 转换字符集出错  请检查是否可以转换
     */
    public SendResult send(String Topic, String Tags, List<String> keys, String body)
            throws UnsupportedEncodingException {
        return this.send(Topic, Tags, body, keys, null);
    }

    /**
     * 发送数据到MQ方法    数据编码格式  默认UTF-8
     *
     * @param Topic 队列名称
     * @param Tags  标签名称
     * @param body  发送的数据  推荐 JSOM 或者 XML 结构
     * @param key   单个个属性
     * @return 响应信息进行了内部处理  确认已经保存到 MQ 并且    日志已经记录  只要值不是NULL 就是成功发送
     * @throws UnsupportedEncodingException 转换字符集出错  请检查是否可以转换
     */
    public SendResult send(String Topic, String Tags, String key, String body)
            throws UnsupportedEncodingException {
        List<String> keys = new ArrayList<>();
        keys.add(key);
        return this.send(Topic, Tags, body, keys, null);
    }

    /**
     * 发送数据到MQ方法
     *
     * @param Topic    队列名称
     * @param Tags     标签名称
     * @param body     发送的数据  推荐 JSOM 或者 XML 结构
     * @param Encoding 数据编码格式  默认UTF-8
     * @param keys     多个属性
     * @return 响应信息进行了内部处理  确认已经保存到 MQ 并且    日志已经记录  只要值不是NULL 就是成功发送
     * @throws UnsupportedEncodingException 转换字符集出错  请检查是否可以转换
     */
    public SendResult send(String Topic, String Tags, String body, List<String> keys, String Encoding)
            throws UnsupportedEncodingException {

        String loggerString = MessageFormat.format(
                "将要发送到Mq的数据    Topic={0}   Tags={1}   body={2}  Encoding={3} ", Topic, Tags, body,
                Encoding);

        if (Encoding == null || "".equals(Encoding)) {
            Encoding = "UTF-8";
        }

        if (Tags == null || "".equals(Tags)) {
            Tags = "*";
        }

        LOGGER.debug(loggerString);

        Message me = new Message();
        // 标示
        me.setTopic(Topic);
        // 标签
        me.setTags(Tags);

        if (keys != null) {
            me.setKeys(keys);
        }
        // 内容
        me.setBody(body.getBytes(Encoding));
        // 发送信息到MQ SendResult 是当前发送的状态 官方说 不出异常 就是成功
        SendResult sendResult = null;
        try {
            sendResult = producer.send(me);
        } catch (Exception e) {
            LOGGER.error(" 发送 数据给MQ出现异常  " + loggerString, e);
        }
        // 当消息发送失败时如何处理 getSendStatus 获取发送的状态
        if (sendResult == null || sendResult.getSendStatus() != SendStatus.SEND_OK) {
            LOGGER.debug(loggerString + "发送消息失败" + " MQ状态值  SendResult=" + sendResult);
            sendResult = null;
        }
        LOGGER.debug("发送到MQ成功" + sendResult);
        return sendResult;
    }

}
