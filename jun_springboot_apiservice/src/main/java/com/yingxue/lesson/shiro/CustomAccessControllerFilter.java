package com.yingxue.lesson.shiro;

import com.alibaba.fastjson.JSON;
import com.yingxue.lesson.constants.Constant;
import com.yingxue.lesson.exception.BusinessException;
import com.yingxue.lesson.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: CustomAccessControllerFilter
 * TODO:类文件简单描述
 * @Author: 小霍
 * @UpdateUser: 小霍
 * @Version: 0.0.1
 */
@Slf4j
public class CustomAccessControllerFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        try {
            log.info(request.getMethod());
            log.info(request.getRequestURL().toString());
            //1判断客户端是否携带凭证
            String sessionId=request.getHeader(Constant.SESSION_ID);
            if(StringUtils.isEmpty(sessionId)){
                throw new BusinessException(4010001,"用户凭证不能为空");
            }else {
                CustomPasswordToken customPasswordToken=new CustomPasswordToken(sessionId);
                getSubject(servletRequest,servletResponse).login(customPasswordToken);
            }
        } catch (BusinessException e) {
            customResponse(e.getCode(),e.getDefaultMessage(),servletResponse);
            return false;
        } catch (AuthenticationException e) {
            if(e.getCause() instanceof BusinessException){
                BusinessException exception= (BusinessException) e.getCause();
                customResponse(exception.getCode(),exception.getDefaultMessage(),servletResponse);
                return false;
            }else {
                customResponse(4000001,"用户认证失败",servletResponse);
                return false;
            }
        }catch (AuthorizationException e){
            if(e.getCause() instanceof BusinessException){
                BusinessException exception= (BusinessException) e.getCause();
                customResponse(exception.getCode(),exception.getDefaultMessage(),servletResponse);
                return false;
            }else {
                customResponse(4030001,"没有访问的权限",servletResponse);
                return false;
            }
        }
        catch (Exception e){
            if(e.getCause() instanceof BusinessException){
                BusinessException exception= (BusinessException) e.getCause();
                customResponse(exception.getCode(),exception.getDefaultMessage(),servletResponse);
            }else {
                customResponse(5000001,"系统异常",servletResponse);
                return false;
            }
        }
        return true;
    }

    private void customResponse(int code,String msg,ServletResponse response){
        try {
            DataResult result=DataResult.getResult(code,msg);
            response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
            String resultJson= JSON.toJSONString(result);
            OutputStream out=response.getOutputStream();
            out.write(resultJson.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
