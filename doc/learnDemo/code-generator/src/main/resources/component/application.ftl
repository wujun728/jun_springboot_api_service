package ${basePackage};

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
* Title
* Author ${author}
* DateTime  ${date}.
* Version V1.0.0
*/
@SpringBootApplication
@MapperScan("${basePackage}.mapper")
@EnableTransactionManagement
@EnableFeignClients
public class ${projectNameUapper}Application  {

    public static void main(String[] args) {
        SpringApplication.run(${projectNameUapper}Application.class, args);
    }
}
