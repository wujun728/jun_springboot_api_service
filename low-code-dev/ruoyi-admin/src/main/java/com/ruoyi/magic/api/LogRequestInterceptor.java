package com.ruoyi.magic.api;


import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessStatus;
import com.ruoyi.common.enums.HttpMethod;
import com.ruoyi.common.filter.PropertyPreExcludeFilter;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.SysOperLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.ssssssss.magicapi.core.interceptor.RequestInterceptor;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.script.MagicScriptContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * magic-api 接口操作日志
 */

@Component
@Order(2)
public class LogRequestInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CustomRequestInterceptor.class);



    /** 排除敏感属性字段 */
    public static final String[] EXCLUDE_PROPERTIES = { "password", "oldPassword", "newPassword", "confirmPassword" };

    //api 地址接口规范
    public static final String APILIST = "list";
    public static final String APIUPDATE = "update";
    public static final String APIDELETE = "delete";
    public static final String APIADD = "add";

    /**
     * 接口执行之后
     *
     * @param info    接口信息
     * @param context 变量信息
     * @param value   即将要返回到页面的值
     */
    @Override
    public Object postHandle(ApiInfo info, MagicScriptContext context, Object value, HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("{} LogRequestInterceptor执行完毕，LogRequestInterceptor返回结果:{}", info.getName(), value);
        logger.info("执行完毕记录操作日志");
        handleLog(info,context,value,request,response);
        return null;
    }






    protected void handleLog(ApiInfo info, MagicScriptContext context, Object value, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            // 获取当前的用户
            LoginUser loginUser = SecurityUtils.getLoginUser();
            // *======数据库日志=======*//
            SysOperLog operLog = new SysOperLog();
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
            operLog.setOperUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
            operLog.setOperIp(ip);
            if (loginUser != null)
            {
                operLog.setOperName(loginUser.getUsername());
            }

            // 设置方法名称
            /*String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");*/
            //magic 操作方法
            operLog.setMethod(context.getScriptName());

            // 设置请求方式
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(info, context, value,request,response,operLog);
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            logger.error("异常信息:{}", exp);

        }
    }




    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     */
    public void getControllerMethodDescription(ApiInfo info, MagicScriptContext context, Object value, HttpServletRequest request, HttpServletResponse response, SysOperLog operLog) throws Exception
    {
        //设置action动作   urlName 包含的接口规范判断 修改 新增 还是 删除
        String urlName = request.getRequestURI().replace("/prod-api", "");
        //操作类型
        String[] apiString = urlName.split("/");
        if(!ObjectUtils.isEmpty(apiString)){
            for (int i = 0; i < apiString.length; i++){
                if(apiString[i].equals(APIADD)){
                    operLog.setBusinessType(1);
                    break;
                }
                if(apiString[i].equals(APIUPDATE)){
                    operLog.setBusinessType(2);
                    break;
                }
                if(apiString[i].equals(APIDELETE)){
                    operLog.setBusinessType(3);
                    break;
                }
            }
        }
        if (ObjectUtils.isEmpty(operLog.getBusinessType())){
            operLog.setBusinessType(0);
        }

        // 设置标题 菜单名称
        String scriptName = context.getScriptName();
        String titleName = "";
        if(!ObjectUtils.isEmpty(scriptName)){
            String[] split = scriptName.split("/");
            for (int i = 0; i < split.length; i++) {
                if(!ObjectUtils.isEmpty(split[i])){
                    titleName =  split[i];
                    break;
                }
            }
        }
        operLog.setTitle(titleName);
        // 设置操作人类别
        operLog.setOperatorType(1);
        // 获取参数的信息，传入到数据库中。
        setRequestValue(request, operLog);
        // 保存response，参数和值
        if ( StringUtils.isNotNull(response))
        {
            operLog.setJsonResult(StringUtils.substring(info.getResponseBody(), 0, 2000));
        }
    }



    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(HttpServletRequest request, SysOperLog operLog) throws Exception
    {
        String requestMethod = operLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))
        {
            //String params = argsArrayToString(joinPoint.getArgs());
            String params = request.getParameterMap().toString();
            operLog.setOperParam(StringUtils.substring(params, 0, 2000));
        }
        else
        {
            Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
            operLog.setOperParam(StringUtils.substring(JSON.toJSONString(paramsMap, excludePropertyPreFilter()), 0, 2000));
        }
    }



    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray)
    {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0)
        {
            for (Object o : paramsArray)
            {
                if (StringUtils.isNotNull(o) && !isFilterObject(o))
                {
                    try
                    {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter());
                        params += jsonObj.toString() + " ";
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }
        return params.trim();
    }



    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter()
    {
        return new PropertyPreExcludeFilter().addExcludes(EXCLUDE_PROPERTIES);
    }


    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o)
    {
        Class<?> clazz = o.getClass();
        if (clazz.isArray())
        {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        }
        else if (Collection.class.isAssignableFrom(clazz))
        {
            Collection collection = (Collection) o;
            for (Object value : collection)
            {
                return value instanceof MultipartFile;
            }
        }
        else if (Map.class.isAssignableFrom(clazz))
        {
            Map map = (Map) o;
            for (Object value : map.entrySet())
            {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }








}
