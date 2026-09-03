package com.ruoyi.magic.api;

import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.mapper.SysMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.interceptor.RequestInterceptor;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.magicapi.core.model.JsonBean;
import org.ssssssss.script.MagicScriptContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * magic-api 接口鉴权
 */
@Component
@Order(1)
public class CustomRequestInterceptor implements RequestInterceptor
{

    private static final Logger logger = LoggerFactory.getLogger(CustomRequestInterceptor.class);

    @Autowired
    SysMenuMapper sysMenuMapper;
    @Autowired
    PermissionService permissionService;

    /**
     * 接口请求之前
     *
     * @param info    接口信息
     * @param context 脚本变量信息
     */
    @Override
    public Object preHandle(ApiInfo info, MagicScriptContext context, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 获取所以权限列表
        // 判断是否需要认证
        String urlName = request.getRequestURI().replace("/prod-api", "");
        if (sysMenuMapper.selectMenuPerms().contains(urlName)) {
            return permissionService.hasPermi(urlName) ? null : new JsonBean<>(403, "用户权限不足");
        }
        return null;
    }

    /**
     * 接口执行之后
     *
     * @param info    接口信息
     * @param context 变量信息
     * @param value   即将要返回到页面的值
     */
    @Override
    public Object postHandle(ApiInfo info, MagicScriptContext context, Object value, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("{} 执行完毕，返回结果:{}", info.getName(), value);
        return null;
    }
}
