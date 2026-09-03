package com.github.alenfive.rocketapi.permission.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bxf
 * @version 1.0
 * @date 2023/11/10 10:43
 */
@Component
public class ApiPermissionClient {

    @Autowired(required = false)
    private IApiPermissionService apiPermissionService;

    /**
     * 是否有实现类
     * @return
     */
    public boolean hasPermissionService() {
        return apiPermissionService != null;
    }

    public boolean verifyApiPermission(HttpServletRequest request, String... permissions) {
        boolean verify = true;
        if (apiPermissionService != null) {
            verify = apiPermissionService.verifyApiPermission(request, permissions);
        }
        return verify;
    }




}
