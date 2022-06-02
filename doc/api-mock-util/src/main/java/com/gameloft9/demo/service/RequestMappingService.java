package com.gameloft9.demo.service;

import com.gameloft9.demo.beans.ApiBean;
import com.gameloft9.demo.controllers.ApiController;
import com.gameloft9.demo.controllers.PortalController;
import com.gameloft9.demo.service.api.IRequestMappingService;
import com.gameloft9.demo.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static com.gameloft9.demo.mgrframework.utils.CheckUtil.check;
import static com.gameloft9.demo.mgrframework.utils.CheckUtil.notBlank;

/**
 * Created by gameloft9 on 2018/8/9.
 */
@Service
@Slf4j
public class RequestMappingService implements IRequestMappingService{

    @Autowired
    WebApplicationContext webApplicationContext;

    public ApiBean getApiInfoByIndex(String index){
        notBlank(index,"index cant not be null");

        if(Constants.requestMappings.containsKey(index)){
            return Constants.requestMappings.get(index);
        }

        return null;
    }

    public boolean hasMethodOccupied(String index){
        notBlank(index,"index cant not be null");

        return Constants.requestMappings.containsKey(index);
    }

    public boolean hasApiRegistered(String api,String requestMethod){
        notBlank(api,"api cant not be null");
        notBlank(requestMethod,"requestMethod cant not be null");

        RequestMappingHandlerMapping requestMappingHandlerMapping = webApplicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (RequestMappingInfo info : map.keySet()) {
            for(String pattern :info.getPatternsCondition().getPatterns()){
                if(pattern.equalsIgnoreCase(api)){ // 匹配url
                    if(info.getMethodsCondition().getMethods().contains(getRequestMethod(requestMethod))){ // 匹配requestMethod
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public ApiBean registerApi(String index,String api,String requestMethod,String msg){
        check(!hasMethodOccupied(index),"该序号已经被占用，请先注销api。");
        check(!hasApiRegistered(api,requestMethod),"该api已经注册过了");

        RequestMappingHandlerMapping requestMappingHandlerMapping = webApplicationContext.getBean(RequestMappingHandlerMapping.class);
        Method targetMethod = ReflectionUtils.findMethod(ApiController.class, getHandlerMethodName(index)); // 找到处理该路由的方法

        PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(api);
        RequestMethodsRequestCondition requestMethodsRequestCondition = new RequestMethodsRequestCondition(getRequestMethod(requestMethod));

        RequestMappingInfo mapping_info = new RequestMappingInfo(patternsRequestCondition, requestMethodsRequestCondition, null, null, null, null, null);
        requestMappingHandlerMapping.registerMapping(mapping_info, "apiController", targetMethod); // 注册映射处理

        // 保存注册信息到本地
        ApiBean apiInfo = new ApiBean();
        apiInfo.setApi(api);
        apiInfo.setRequestMethod(requestMethod);
        apiInfo.setMsg(msg);
        Constants.requestMappings.put(index,apiInfo);

        return apiInfo;
    }

    public boolean unregisterApi(String index,String api,String requestMethod){
        check(hasApiRegistered(api,requestMethod),"该api未注册过");

        RequestMappingHandlerMapping requestMappingHandlerMapping = webApplicationContext.getBean(RequestMappingHandlerMapping.class);
        Method targetMethod = ReflectionUtils.findMethod(ApiController.class, getHandlerMethodName(index));

        PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(api);
        RequestMethodsRequestCondition requestMethodsRequestCondition = new RequestMethodsRequestCondition(getRequestMethod(requestMethod));
        RequestMappingInfo mapping_info = new RequestMappingInfo(patternsRequestCondition, requestMethodsRequestCondition, null, null, null, null, null);

        requestMappingHandlerMapping.unregisterMapping(mapping_info); // 注销

        if(Constants.requestMappings.containsKey(index)){
            Constants.requestMappings.remove(index);
        }

        return true;
    }


    /************************私有方法区*****************************/
    /**
     * 获取请求方式，默认GET
     *
     * @param method
     * @return
     */
    private RequestMethod getRequestMethod(String method) {
        if ("get".equalsIgnoreCase(method)) {
            return RequestMethod.GET;
        } else if ("post".equalsIgnoreCase(method)) {
            return RequestMethod.POST;
        } else {
            return RequestMethod.GET;
        }
    }

    /**
     * 获取处理映射的方法名称
     *
     * @param index
     * @return
     */
    private String getHandlerMethodName(String index) {
        return "index" + index;
    }

}
