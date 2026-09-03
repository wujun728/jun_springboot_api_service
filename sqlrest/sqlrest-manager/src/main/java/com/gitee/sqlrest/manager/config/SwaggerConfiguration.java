package com.gitee.sqlrest.manager.config;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  private static final String API_CONTROLLER_PACKAGE = "com.gitee.sqlrest.manager.controller";

  @Bean
  public Docket managerApi() {
    RequestParameterBuilder ticketPar = new RequestParameterBuilder();
    List<RequestParameter> pars = new ArrayList<>();
    ticketPar.name("token")
        .description("认证所用的Token")
        .in(ParameterType.QUERY)
        .required(false)
        .build();
    pars.add(ticketPar.build());

    return new Docket(DocumentationType.SWAGGER_2)
        .enable(true)
        .groupName("Manager的接口")
        .apiInfo(new ApiInfoBuilder()
            .title("Manager服务API文档")
            .description("在线API文档")
            .version("1.0")
            .build())
        .select()
        .apis(RequestHandlerSelectors.basePackage(API_CONTROLLER_PACKAGE))
        .paths(PathSelectors.any())
        .build()
        .globalRequestParameters(pars)
        .ignoredParameterTypes(HttpServletResponse.class, HttpServletRequest.class);
  }

}
