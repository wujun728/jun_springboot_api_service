package com.soukuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.soukuan.demo.Demo3;
/**
 * @author xiebiao
 * @date  2018-07-11  10:22:49
 * @version v1.0
 * Description
 */
@SpringBootApplication
public class ExceptionApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ExceptionApplication.class, args);

        Demo3 bean = run.getBean(Demo3.class);
        System.out.println(1);
    }
}
