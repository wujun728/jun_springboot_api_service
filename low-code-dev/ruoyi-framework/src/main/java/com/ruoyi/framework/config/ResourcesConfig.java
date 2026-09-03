package com.ruoyi.framework.config;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.framework.interceptor.RepeatSubmitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 通用配置
 *
 * @author ruoyi
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;
    //@Override
    //public void addCorsMappings(CorsRegistry registry) {
    //    //registry.addMapping("/**")
    //    //        //是否发送Cookie
    //    //        .allowCredentials(true)
    //    //        //放行哪些原始域
    //    //        .allowedOriginPatterns("*")
    //    //        .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"})
    //    //        .allowedHeaders("*")
    //    //        .exposedHeaders("*");
    //    //registry.addMapping("/**")
    //    //        .allowedOriginPatterns("*")
    //    //        //.allowedOrigins("*")
    //    //        .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
    //    //        .allowedHeaders("*")
    //    //        .maxAge(3600 * 24);
    //}
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 本地文件上传路径 */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + RuoYiConfig.getProfile() + "/");

        /** swagger配置 */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());

    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/prod-api/**");
    }
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/prod-api", c -> c.isAnnotationPresent(RestController.class));
        //configurer.addPathPrefix("/prod-api", aClass -> aClass.getSuperclass().equals(RestController.class));
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }
}
