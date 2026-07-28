package com.ruoyi.api.version.entity;

import java.util.Date;

public class ApiVersionRecord {

    private Long id;
    private String apiId;
    private String apiPath;
    private String apiName;
    private String content;
    private String diffContent;
    private Integer versionNo;
    private String operateType;
    private String operateBy;
    private Date operateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getApiId() { return apiId; }
    public void setApiId(String apiId) { this.apiId = apiId; }

    public String getApiPath() { return apiPath; }
    public void setApiPath(String apiPath) { this.apiPath = apiPath; }

    public String getApiName() { return apiName; }
    public void setApiName(String apiName) { this.apiName = apiName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDiffContent() { return diffContent; }
    public void setDiffContent(String diffContent) { this.diffContent = diffContent; }

    public Integer getVersionNo() { return versionNo; }
    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }

    public String getOperateType() { return operateType; }
    public void setOperateType(String operateType) { this.operateType = operateType; }

    public String getOperateBy() { return operateBy; }
    public void setOperateBy(String operateBy) { this.operateBy = operateBy; }

    public Date getOperateTime() { return operateTime; }
    public void setOperateTime(Date operateTime) { this.operateTime = operateTime; }
}