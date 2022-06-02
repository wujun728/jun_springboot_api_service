package com.gameloft9.demo.controllers;

import com.gameloft9.demo.beans.ApiBean;
import com.gameloft9.demo.mgrframework.beans.response.IResult;
import com.gameloft9.demo.mgrframework.beans.response.ResultBean;
import com.gameloft9.demo.request.ApiRegisterRequest;
import com.gameloft9.demo.request.ApiUnregisterRequest;
import com.gameloft9.demo.service.api.IRequestMappingService;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;
import javax.validation.Valid;



/**
 * 入口
 * Created by gameloft9 on 2018/8/7.
 */
@Controller
@EnableAutoConfiguration
@Slf4j
@ComponentScan(basePackages="com.gameloft9.demo")
public class PortalController {

    @Autowired
    IRequestMappingService requestMappingService;

    /**
     * 入口
     * */
    @Deprecated
    @RequestMapping(value="/index")
    public String goHome() {
       return "home";
    }

    /**
     * 注册api
     * */
    @RequestMapping(value = "/registerApi.do", method = RequestMethod.POST)
    @ResponseBody
    public IResult registerApi(@Valid ApiRegisterRequest request, BindingResult result) {
        return new ResultBean<ApiBean>(requestMappingService.registerApi(request.getIndex(),request.getApi(),request.getRequestMethod(),request.getMsg()));
    }

    /**
     * 注销api
     * */
    @RequestMapping(value = "/unregisterApi.do", method = RequestMethod.POST)
    @ResponseBody
    public IResult unregisterApi(@Valid ApiUnregisterRequest request, BindingResult result) {
        return new ResultBean<Boolean>(requestMappingService.unregisterApi(request.getIndex(),request.getApi(),request.getRequestMethod()));
    }

    /**
     * 检查接口序号是否已经占用
     * */
    @RequestMapping(value = "/getApiInfoByIndex.do", method = RequestMethod.POST)
    @ResponseBody
    public IResult getApiInfoByIndex(String index) {
        return new ResultBean<ApiBean>(requestMappingService.getApiInfoByIndex(index));
    }

    /**
     * 跨域过滤器
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());

        return new CorsFilter(source);
    }

    /**
     * 生成跨域配置
     * @return
     */
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        return corsConfiguration;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PortalController.class, args);
    }
}
