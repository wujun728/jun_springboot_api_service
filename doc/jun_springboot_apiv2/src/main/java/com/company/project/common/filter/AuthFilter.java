package com.company.project.common.filter;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.company.project.common.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@WebFilter(filterName = "authFilter", urlPatterns = "/*")
@Order(1)
public class AuthFilter implements Filter {
    @Autowired
    OpenapiConfig openapiConfig;
    //需要拦截的地址
    private static final String[] whiteList = {"/api/open/xxx"};
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String url = req.getRequestURI();
        log.info("url:{}", url);
        if (Arrays.asList(whiteList).contains(url)) {
            if (validateSign(req)) {
                chain.doFilter(request, response);
            } else {
                responseResult(resp, DataResult.fail("鉴权失败"));
            }
        }else {
            chain.doFilter(request, response);
        }

    }

    /**
     * 一个简单的签名认证，规则：
     * 1. 将请求参数按ascii码排序
     * 2. 拼接为a=value&b=value...这样的字符串（去掉sign）
     * 3. 混合密钥（secret）进行md5获得签名，与请求的签名进行比较
     */
    private boolean validateSign(HttpServletRequest request) {
        //获得请求签名，如sign=19e907700db7ad91318424a97c54ed57
        String requestSign = request.getHeader("Authorization");
        if (StringUtils.isEmpty(requestSign)) {
            log.error("签名认证失败：sign不能为空");
            return false;
        }
        String from = request.getHeader("From");
        if (StringUtils.isEmpty(from)) {
            log.error("签名认证失败：From不能为空");
            return false;
        }

        //时间拦截 begin
        String time = request.getParameter("time");
        if (StringUtils.isEmpty(time) || !isTime(time)) {
            log.error("签名认证失败：time不能为空, 或不是时间戳");
            return false;
        }
        if (Math.abs(System.currentTimeMillis() - Long.parseLong(time)) > 1000 * 60 * 3) {
            log.error("签名认证失败：time不能超过1分钟");
            return false;
        }
        //时间拦截end

        //查询来源 begin
        List<App> apps = openapiConfig.getApps();
        if (CollectionUtils.isEmpty(apps)) {
            log.error("签名认证失败：根据from获取secret失败");
            return false;
        }
        //查询来源 begin
        Optional<App> optionalApp = apps.stream().filter(app -> from.equals(app.getFrom())).findFirst();
        if (!optionalApp.isPresent()) {
            log.error("签名认证失败：根据from获取secret失败");
            return false;
        }
        App thirdAp = optionalApp.get();

        List<String> keys = new ArrayList<>(request.getParameterMap().keySet());
        //排除sign参数
        keys.remove("sign");
        //排序
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            //拼接字符串
            sb.append(key).append("=").append(request.getParameter(key)).append("&");
        }
        String linkString = sb.toString();
        //去除最后一个'&'
        linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);
        //混合密钥md5
        String sign = DigestUtils.md5Hex(linkString + thirdAp.getSecret());
        //比较
        return StringUtils.equals(sign, requestSign);
    }

    public static void main(String[] args) {
        String secret = "29e907700db7ad91318424a97c54ed576";//hdy
        Long current = System.currentTimeMillis();//当前时间戳
//        String param = "sn=19047921&time="+current;//实际接口一签名生成示例
//        System.out.println(param);
        String param = "time=" + current;//实际接口二签名生成示例
        System.out.println(param);
        String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(param + secret);//签名生成，把sign放到header中，key为Authorization
        System.out.println(sign);

    }

    public boolean isTime(String timeStr) {
        try {
            if (timeStr.length() != 13) {
                return false;
            }
            Long.parseLong(timeStr);
        } catch (Exception e) {
            log.error("签名认证失败：time非时间戳");
        }
        return true;
    }

    private void responseResult(HttpServletResponse response, DataResult result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}