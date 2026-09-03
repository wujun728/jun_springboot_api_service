package com.github.alenfive.rocketapiplatform.rocketapiplatform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alenfive.rocketapi.config.QLRequestMappingFactory;
import com.github.alenfive.rocketapi.entity.ApiInfo;
import com.github.alenfive.rocketapi.entity.vo.RefreshMapping;
import com.github.alenfive.rocketapi.extend.IApiInfoCache;
import com.github.alenfive.rocketapi.utils.GenerateId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomApiInfoCache implements IApiInfoCache ,MessageListener{

    @Value("${spring.application.name}")
    private String service;

    @Autowired
    @Lazy
    private QLRequestMappingFactory mappingFactory;

    private String instanceId = GenerateId.get().toHexString();

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String buildPrefix(){
        return "rocket-api:"+service;
    }

    public String buildChannelName(){
        return "rocket-api:"+service+":channel";
    }

    private String buildApiInfoKey(ApiInfo apiInfo) {
        return buildPrefix()+":"+apiInfo.getMethod() +"-"+ apiInfo.getFullPath();
    }

    @Override
    public ApiInfo get(ApiInfo apiInfo) {
        String strValue = redisTemplate.opsForValue().get(buildApiInfoKey(apiInfo));
        try {
            return objectMapper.readValue(strValue,ApiInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void put(ApiInfo apiInfo) {
        try {
            String strValue = objectMapper.writeValueAsString(apiInfo);
            redisTemplate.opsForValue().set(buildApiInfoKey(apiInfo),strValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(ApiInfo apiInfo) {
        redisTemplate.delete(buildApiInfoKey(apiInfo));
    }

    @Override
    public void removeAll() {
        redisTemplate.delete(getKeys());
    }

    private List<String> getKeys(){
        String patternKey = buildPrefix()+":*";
        ScanOptions options = ScanOptions.scanOptions()
                .count(10000)
                .match(patternKey).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        Cursor cursor = (Cursor) redisTemplate.executeWithStickyConnection(redisConnection -> new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize));
        List<String> keys = new ArrayList<>();
        while(cursor.hasNext()){
            keys.add(cursor.next().toString());
        }
        try {
            cursor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keys;
    }

    @Override
    public Collection<ApiInfo> getAll() {
        return redisTemplate.opsForValue().multiGet(getKeys()).stream().map(item->{
            try {
                return objectMapper.readValue(item,ApiInfo.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    @Override
    public void refreshNotify(RefreshMapping refreshMapping) {
        NotifyEntity notifyEntity = new NotifyEntity();
        notifyEntity.setRefreshMapping(refreshMapping);
        notifyEntity.setIdentity(this.instanceId);
        String messageStr = null;
        try {
            messageStr = objectMapper.writeValueAsString(notifyEntity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        redisTemplate.convertAndSend(buildChannelName(), messageStr);
    }

    @Override
    public void receiveNotify(String instanceId, RefreshMapping refreshMapping) {
        //避免本实例重复初始化
        if (this.instanceId.equals(instanceId)){
            return;
        }

        //刷新单个接口
        if (refreshMapping != null){
            try {
                mappingFactory.refreshMapping(refreshMapping);
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }

        //全局刷新
        try {
            mappingFactory.buildInit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String messageStr = new String(message.getBody());
        NotifyEntity notifyEntity = null;
        try {
            notifyEntity = objectMapper.readValue(messageStr, NotifyEntity.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.receiveNotify(notifyEntity.getIdentity(),notifyEntity.getRefreshMapping());
    }

}
