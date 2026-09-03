package com.zy.iservice.domain.bo;

import com.zy.common.core.domain.BaseEntity;

/**
 * SelectIntfaceAppAuthListBO
 *
 * @author javachen
 * @date 2022-09-26
 */
public class SelectIntfaceAppAuthListBO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 接口id */
    private Long id;

    /** 应用id */
    private Long intfaceAppId;

    /** 接口名称 */
    private String name;

    /** app_manager表name */
    private String appManagerName;

    /** app_manager表prefix (应用前缀==缓存前缀) */
    private String appManagerPrefix;

    /** 接口描述 */
    private String depict;

    /** 接口状态 1已授权 2未授权 */
    private String status;

    public Long getIntfaceAppId() {
        return intfaceAppId;
    }

    public void setIntfaceAppId(Long intfaceAppId) {
        this.intfaceAppId = intfaceAppId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppManagerName() {
        return appManagerName;
    }

    public void setAppManagerName(String appManagerName) {
        this.appManagerName = appManagerName;
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict;
    }

    public SelectIntfaceAppAuthListBO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppManagerPrefix() {
        return appManagerPrefix;
    }

    public void setAppManagerPrefix(String appManagerPrefix) {
        this.appManagerPrefix = appManagerPrefix;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
