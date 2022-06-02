package com.github.alenfive.rocketopenapi.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisListenerConfig {

    @Autowired
    private CustomApiInfoCache apiInfoCache;

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        String channelName = apiInfoCache.buildChannelName();
        container.addMessageListener(listenerAdapter, new PatternTopic(channelName));
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(CustomApiInfoCache receiver) {
        return new MessageListenerAdapter(receiver, "onMessage");
    }
}