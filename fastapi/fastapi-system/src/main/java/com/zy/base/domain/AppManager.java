package com.zy.base.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zy.common.annotation.Excel;
import com.zy.common.core.domain.BaseEntity;

/**
 * 应用管理对象 app_manager
 * 
 * @author ruoyi
 * @date 2022-09-26
 */
public class AppManager extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 应用名称 */
    @Excel(name = "应用名称")
    private String name;

    /** 应用前缀 */
    @Excel(name = "应用前缀")
    private String prefix;

    /** 数据源id */
    @Excel(name = "数据源id")
    private Long dbConfigId;

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
    public void setPrefix(String prefix) 
    {
        this.prefix = prefix;
    }

    public String getPrefix() 
    {
        return prefix;
    }
    public void setDbConfigId(Long dbConfigId) 
    {
        this.dbConfigId = dbConfigId;
    }

    public Long getDbConfigId() 
    {
        return dbConfigId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("prefix", getPrefix())
            .append("dbConfigId", getDbConfigId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
