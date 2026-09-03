package com.yatop.lambda.net.websocket.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.yatop.lambda.net.websocket.serialize.Serializer;
import com.yatop.lambda.net.websocket.serialize.SerializerAlgorithm;

/**
 * json 序列化器
 *
 * @author mm
 * @date 2019-04-20
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
