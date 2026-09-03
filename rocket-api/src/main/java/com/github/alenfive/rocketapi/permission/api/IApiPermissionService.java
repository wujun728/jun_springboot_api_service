package com.github.alenfive.rocketapi.permission.api;


import javax.servlet.http.HttpServletRequest;

/**
 * @author bxf
 * @version 1.0
 * @date 2023/11/10 10:38
 */
public interface IApiPermissionService {

     /**
     * 校验接口权限
     * @param request 请求
     * @param permission 权限列表
     * @return 是否有权限访问
     */
    boolean verifyApiPermission(HttpServletRequest request, String... permission);


}
