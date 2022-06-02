package com.soukuan.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CallBackRabbitConfig {

    private final static String messages = "callback.test";

    @Bean
    public Queue callback() {
        return new Queue(CallBackRabbitConfig.messages);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("exchange");
    }

    @Bean
    Binding bindingExchangeMessages(Queue callback, TopicExchange exchange) {
        return BindingBuilder.bind(callback).to(exchange).with("callback.test");
    }
}