package com.github.alenfive.rocketapi.service;

import com.github.alenfive.rocketapi.config.QLRequestMappingFactory;
import com.github.alenfive.rocketapi.config.RocketApiProperties;
import com.github.alenfive.rocketapi.entity.ApiInfo;
import com.github.alenfive.rocketapi.entity.ApiType;
import com.github.alenfive.rocketapi.utils.MappingInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class RequestMappingService implements InitializingBean {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private RocketApiProperties rocketApiProperties;

    private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();

    @Autowired
    @Lazy
    private QLRequestMappingFactory mappingFactory;

    /**
     * 获取已注册的API地址
     */
    public List<ApiInfo> getPathListForCode() {

        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        List<ApiInfo> result = new ArrayList<>(map.size());
        for (RequestMappingInfo info : map.keySet()) {

            if (map.get(info).getMethod().getDeclaringClass() == QLRequestMappingFactory.class){
                continue;
            }

            String groupName = map.get(info).getBeanType().getSimpleName();
            for (String path : MappingInfoUtils.getPatterns(info)) {

                //过滤本身的类
                if (path.indexOf(rocketApiProperties.getBaseRegisterPath()) == 0 || path.equals("/error")) {
                    continue;
                }

                Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
                if (methods.isEmpty()) {
                    result.add(ApiInfo.builder()
                            .path(path)
                            .fullPath(path)
                            .method("All")
                            .type(ApiType.Code.name())
                            .service(rocketApiProperties.getServiceName())
                            .directoryId(groupName)
                            .editor("admin")
                            .name("")
                            .datasource("")
                            .script("")
                            .options("")
                            .build());
                } else {
                    for (RequestMethod method : methods) {
                        result.add(ApiInfo.builder()
                                .path(path)
                                .fullPath(path)
                                .method(method.name())
                                .type(ApiType.Code.name())
                                .service(rocketApiProperties.getServiceName())
                                .directoryId(groupName)
                                .editor("admin")
                                .name("")
                                .datasource("")
                                .script("")
                                .options("")
                                .build());
                    }
                }

            }
        }
        return result;
    }

    /**
     * 注册mapping
     *
     * @param apiInfo
     */
    public synchronized void registerMappingForApiInfo(ApiInfo apiInfo) throws NoSuchMethodException {
        if (ApiType.Code.name().equals(apiInfo.getType())) {
            return;
        }

        String pattern = apiInfo.getFullPath();

        if (StringUtils.isEmpty(pattern) || pattern.startsWith("TEMP-")) {
            return;
        }

        RequestMappingInfo mappingInfo = getRequestMappingInfo(pattern,apiInfo.getMethod());
        if (mappingInfo != null){
            return;
        }

        log.debug("Mapped [{}]{}", apiInfo.getMethod(), pattern);
        mappingInfo = RequestMappingInfo.paths(pattern)
                .methods(RequestMethod.valueOf(apiInfo.getMethod()))
                .options(this.config)
                .build();
        Method targetMethod = QLRequestMappingFactory.class.getDeclaredMethod("execute", Map.class, Map.class, HttpServletRequest.class, HttpServletResponse.class);
        requestMappingHandlerMapping.registerMapping(mappingInfo, mappingFactory, targetMethod);
    }



    /**
     * 取消注册mapping
     *
     * @param apiInfo
     */
    public synchronized void unregisterMappingForApiInfo(ApiInfo apiInfo) {
        if (ApiType.Code.name().equals(apiInfo.getType())) {
            return;
        }

        String pattern = apiInfo.getFullPath();

        if (StringUtils.isEmpty(pattern) || pattern.startsWith("TEMP-")) {
            return;
        }

        RequestMappingInfo mappingInfo = getRequestMappingInfo(pattern,apiInfo.getMethod());
        if (mappingInfo == null){
            return;
        }

        log.debug("Cancel Mapping [{}]{}", apiInfo.getMethod(), pattern);
        mappingInfo = RequestMappingInfo.paths(pattern)
                .methods(RequestMethod.valueOf(apiInfo.getMethod()))
                .options(this.config)
                .build();
        requestMappingHandlerMapping.unregisterMapping(mappingInfo);
    }

    private RequestMappingInfo getRequestMappingInfo(String pattern,String method){
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (RequestMappingInfo info : map.keySet()) {
            Set<String> patterns = MappingInfoUtils.getPatterns(info);
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            if (patterns.contains(pattern) && (methods.isEmpty() || methods.contains(RequestMethod.valueOf(method)))){
                return info;
            }
        }
        return null;
    }

    /**
     * 判断是否是原始代码注册的mapping
     * @param method
     * @param pattern
     */
    public Boolean isCodeMapping(String pattern,String method){
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (RequestMappingInfo info : map.keySet()) {
            if (map.get(info).getMethod().getDeclaringClass() == QLRequestMappingFactory.class){
                continue;
            }
            Set<String> patterns = MappingInfoUtils.getPatterns(info);
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            if (patterns.contains(pattern) && (methods.isEmpty() || methods.contains(RequestMethod.valueOf(method)))){
                return true;
            }
        }

        return false;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.config.setTrailingSlashMatch(requestMappingHandlerMapping.useTrailingSlashMatch());
        this.config.setContentNegotiationManager(requestMappingHandlerMapping.getContentNegotiationManager());

        if (requestMappingHandlerMapping.getPatternParser() != null) {
            this.config.setPatternParser(requestMappingHandlerMapping.getPatternParser());
            Assert.isTrue(!requestMappingHandlerMapping.useSuffixPatternMatch() && !requestMappingHandlerMapping.useRegisteredSuffixPatternMatch(),
                    "Suffix pattern matching not supported with PathPatternParser.");
        }
        else {
            this.config.setSuffixPatternMatch(requestMappingHandlerMapping.useSuffixPatternMatch());
            this.config.setRegisteredSuffixPatternMatch(requestMappingHandlerMapping.useRegisteredSuffixPatternMatch());
            this.config.setPathMatcher(requestMappingHandlerMapping.getPathMatcher());
        }
    }
}
