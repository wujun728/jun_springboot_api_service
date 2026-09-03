package com.ruoyi.common.cloud.feign;

import com.ruoyi.common.cloud.constants.SecurityConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Feign请求拦截器，传递请求头信息
 *
 * @author ruoyi
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 传递用户信息请求头
            String userId = request.getHeader(SecurityConstants.DETAILS_USER_ID);
            if (userId != null) {
                requestTemplate.header(SecurityConstants.DETAILS_USER_ID, userId);
            }
            String userName = request.getHeader(SecurityConstants.DETAILS_USERNAME);
            if (userName != null) {
                requestTemplate.header(SecurityConstants.DETAILS_USERNAME, userName);
            }
            String authentication = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
            if (authentication != null) {
                requestTemplate.header(SecurityConstants.AUTHORIZATION_HEADER, authentication);
            }
            // 标记内部请求来源
            requestTemplate.header(SecurityConstants.FROM_SOURCE, SecurityConstants.INNER);
        }
    }
}
