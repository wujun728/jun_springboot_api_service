package com.jun.plugin.system.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * ClassName: Swagger2AutoConfiguration create: 2020-04-28 12:24
 *
 * @author: Wujun @since： JDK1.8
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	/**
	 * 在IOC容器里面创建可以对象可以扫描Controller里面的是否有Swagger相关的注解 如果，swagger会生成相关的文档
	 * RestController.class 表示扫描这个注解所有的代码 生成对应的文档
	 *
	 * @return
	 */
	@Bean
	public Docket swaggerSpringMvcPlugin() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
//                  .apis(RequestHandlerSelectors.basePackage("com.xqnode.controller"))
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().description("系统Swagger配置自动渲染api文档")
				.contact(new Contact("Wujun", "http://nb666.net/", "wujun728@163.com")).version("1.0").license("Wujun呀")
				.build();
	}

}