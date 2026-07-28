package com.ruoyi.api.openapi.audit;

import java.util.Date;

public class ApiAuditLog {

    private Long id;
    private String appKey;
    private String apiPath;
    private String method;
    private String clientIp;
    private String params;
    private Integer status;
    private String errorMsg;
    private Long costTime;
    private Date createTime;
    private String createBy;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAppKey() { return appKey; }
    public void setAppKey(String appKey) { this.appKey = appKey; }

    public String getApiPath() { return apiPath; }
    public void setApiPath(String apiPath) { this.apiPath = apiPath; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }

    public String getParams() { return params; }
    public void setParams(String params) { this.params = params; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getErrorMsg() { return errorMsg; }
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }

    public Long getCostTime() { return costTime; }
    public void setCostTime(Long costTime) { this.costTime = costTime; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
}