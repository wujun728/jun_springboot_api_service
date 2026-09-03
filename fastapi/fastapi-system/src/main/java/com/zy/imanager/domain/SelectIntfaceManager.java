package com.zy.imanager.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zy.common.annotation.Excel;
import com.zy.common.core.domain.BaseEntity;

/**
 * 查询接口管理对象 select_intface_manager
 *
 * @author javachen
 * @date 2022-09-26
 */
public class SelectIntfaceManager extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 接口名称 */
    @Excel(name = "接口名称")
    private String name;

    /** sql语句 */
    private String sqlStr;

    /** 结果类型 1单个对象 2列表对象 */
    @Excel(name = "结果类型 1单个对象 2列表对象")
    private Long resultType;

    /** app_manager表id */
    @Excel(name = "app_manager表id")
    private Long appManagerId;

    /** app_manager表name */
    @Excel(name = "app_manager表name")
    private String appManagerName;

    /** app_manager表prefix (应用前缀==缓存前缀) */
    private String appManagerPrefix;

    /** 是否启用缓存 1启用 2关闭 */
    @Excel(name = "是否启用缓存 1启用 2关闭")
    private Long openCache;

    /** 接口描述 */
    @Excel(name = "接口描述")
    private String depict;

    /** kv转换内容为json(只有当结果类型为列表对象时才生效) */
    private String kvConver;

    /** 行列转换内容为json(只有当结果类型为列表对象时才生效) */
    private String rcConver;

    /** key提取 */
    private String keyExtract;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setSqlStr(String sqlStr)
    {
        this.sqlStr = sqlStr;
    }

    public String getSqlStr()
    {
        return sqlStr;
    }
    public void setResultType(Long resultType)
    {
        this.resultType = resultType;
    }

    public Long getResultType()
    {
        return resultType;
    }
    public void setAppManagerId(Long appManagerId)
    {
        this.appManagerId = appManagerId;
    }

    public Long getAppManagerId()
    {
        return appManagerId;
    }
    public void setAppManagerName(String appManagerName)
    {
        this.appManagerName = appManagerName;
    }

    public String getAppManagerName()
    {
        return appManagerName;
    }
    public void setAppManagerPrefix(String appManagerPrefix)
    {
        this.appManagerPrefix = appManagerPrefix;
    }

    public String getAppManagerPrefix()
    {
        return appManagerPrefix;
    }
    public void setOpenCache(Long openCache)
    {
        this.openCache = openCache;
    }

    public Long getOpenCache()
    {
        return openCache;
    }
    public void setDepict(String depict)
    {
        this.depict = depict;
    }

    public String getDepict()
    {
        return depict;
    }

    public String getKvConver() {
        return kvConver;
    }

    public void setKvConver(String kvConver) {
        this.kvConver = kvConver;
    }

    public String getRcConver() {
        return rcConver;
    }

    public void setRcConver(String rcConver) {
        this.rcConver = rcConver;
    }

    public String getKeyExtract() {
        return keyExtract;
    }

    public void setKeyExtract(String keyExtract) {
        this.keyExtract = keyExtract;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("sqlStr", getSqlStr())
                .append("resultType", getResultType())
                .append("appManagerId", getAppManagerId())
                .append("appManagerName", getAppManagerName())
                .append("appManagerPrefix", getAppManagerPrefix())
                .append("openCache", getOpenCache())
                .append("depict", getDepict())
                .append("kvConver", getKvConver())
                .append("rcConver", getRcConver())
                .append("keyExtract", getKeyExtract())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
