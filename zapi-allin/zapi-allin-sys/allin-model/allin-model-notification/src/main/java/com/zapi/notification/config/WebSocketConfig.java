package com.zapi.notification.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置类
 * 用于实时消息推送
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private NotificationConfig notificationConfig;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册STOMP端点
        registry.addEndpoint(notificationConfig.getRealTimeMessage().getEndpoint())
                .setAllowedOriginPatterns("*")  // 允许跨域
                .withSockJS();  // 启用SockJS支持
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 配置消息代理
        registry.enableSimpleBroker("/topic", "/queue", "/user");
        
        // 配置应用程序前缀
        registry.setApplicationDestinationPrefixes("/app");
        
        // 配置用户目的地前缀
        registry.setUserDestinationPrefix("/user");
    }
} 