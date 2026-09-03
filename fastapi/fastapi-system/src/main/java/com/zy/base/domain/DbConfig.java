package com.zy.base.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zy.common.annotation.Excel;
import com.zy.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 数据源配置对象 db_config
 *
 * @author javachen
 * @date 2022-09-26
 */
public class DbConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 数据库名称 */
    @Excel(name = "数据库名称")
    private String name;

    /** 数据库ip */
    @Excel(name = "数据库ip")
    private String ipAddress;

    /** 连接url */
    @Excel(name = "连接url")
    private String connectUrl;

    /** 数据库驱动 */
    @Excel(name = "数据库驱动")
    private String connectDriver;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 检查状态 1健康 2异常 */
    @Excel(name = "当前状态 1健康 2异常")
    private Long checkStatus;

    /** 检查时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

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
    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }
    public void setConnectUrl(String connectUrl)
    {
        this.connectUrl = connectUrl;
    }

    public String getConnectUrl()
    {
        return connectUrl;
    }
    public void setConnectDriver(String connectDriver)
    {
        this.connectDriver = connectDriver;
    }

    public String getConnectDriver()
    {
        return connectDriver;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    public Long getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Long checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("ipAddress", getIpAddress())
                .append("connectUrl", getConnectUrl())
                .append("connectDriver", getConnectDriver())
                .append("userName", getUserName())
                .append("password", getPassword())
                .append("checkStatus", getCheckStatus())
                .append("checkTime",getCheckTime())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
