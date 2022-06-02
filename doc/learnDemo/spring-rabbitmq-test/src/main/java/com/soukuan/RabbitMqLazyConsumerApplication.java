package com.soukuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiebiao
 * @date  2018-07-11  10:22:49
 * @version v1.0
 * Description
 */
@SpringBootApplication
public class RabbitMqLazyConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqLazyConsumerApplication.class, args);
    }
}
