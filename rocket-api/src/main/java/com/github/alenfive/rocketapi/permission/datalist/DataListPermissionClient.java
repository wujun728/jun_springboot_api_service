package com.github.alenfive.rocketapi.permission.datalist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据权限处理
 * @author bianxiongfeng
 * @version 1.0
 * @date 2023/11/10 9:41
 */
@Component
public class DataListPermissionClient {


    @Autowired(required = false)
    private IDataListPermissionService permissionService;

    /**
     * 是否有实现类
     * @return
     */
    public boolean hasPermissionService() {
        return permissionService != null;
    }

    /**
     * 根据权限过滤
     * @param allId 全部的数据集id
     * @return 当前用户有权限的数据集id
     */
    public List<String> filterByPermission(List<String> allId, String dataTypeList) {
        if (permissionService != null) {
            return permissionService.filterByPermission(allId, dataTypeList);
        }
        return allId;
    }

    /**
     * 数据集新增后的权限处理
     * @param id 新增的数据集id
     */
    public void addPermission(String dataTypeList,String ...id) {
        if (permissionService != null) {
            permissionService.addPermission(dataTypeList,id);
        }
    }

    /**
     * 数据集删除后的权限处理
     * @param id 删除的数据集id
     */
    public void deletePermission(String dataTypeList,String ...id) {
        if (permissionService != null) {
            permissionService.deletePermission(dataTypeList,id);
        }
    }
}
