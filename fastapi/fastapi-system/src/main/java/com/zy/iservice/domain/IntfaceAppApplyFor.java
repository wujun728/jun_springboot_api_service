package com.zy.iservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zy.common.annotation.Excel;
import com.zy.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 接口应用申请对象 intface_app_apply_for
 *
 * @author javachen
 * @date 2022-11-19
 */
public class IntfaceAppApplyFor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 应用名称 */
    @Excel(name = "应用名称")
    private String name;

    /** 访问ip */
    @Excel(name = "访问ip")
    private String accessIp;

    /** 联系人 */
    @Excel(name = "联系人")
    private String linkPerson;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String linkPhone;

    /** client_id */
    private String clientId;

    /** client_secret */
    private String clientSecret;

    /** 创建者id */
    private Long createId;

    /** 检查时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    /** 密钥有效期(开始时间) */
    private Date clientStartTime;

    /** 密钥有效期(天数) */
    private Integer clientExpireDay;

    /** 检查时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    /** 密钥有效期(结束时间) clientStartTime+clientExpireDay */
    private Date clientEndTime;

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
    public void setAccessIp(String accessIp)
    {
        this.accessIp = accessIp;
    }

    public String getAccessIp()
    {
        return accessIp;
    }
    public void setLinkPerson(String linkPerson)
    {
        this.linkPerson = linkPerson;
    }

    public String getLinkPerson()
    {
        return linkPerson;
    }
    public void setLinkPhone(String linkPhone)
    {
        this.linkPhone = linkPhone;
    }

    public String getLinkPhone()
    {
        return linkPhone;
    }
    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getClientId()
    {
        return clientId;
    }
    public void setClientSecret(String clientSecret)
    {
        this.clientSecret = clientSecret;
    }

    public String getClientSecret()
    {
        return clientSecret;
    }
    public void setCreateId(Long createId)
    {
        this.createId = createId;
    }

    public Long getCreateId()
    {
        return createId;
    }

    public Date getClientStartTime() {
        return clientStartTime;
    }

    public void setClientStartTime(Date clientStartTime) {
        this.clientStartTime = clientStartTime;
    }

    public Date getClientEndTime() {
        return clientEndTime;
    }

    public void setClientEndTime(Date clientEndTime) {
        this.clientEndTime = clientEndTime;
    }

    public Integer getClientExpireDay() {
        return clientExpireDay;
    }

    public void setClientExpireDay(Integer clientExpireDay) {
        this.clientExpireDay = clientExpireDay;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("accessIp", getAccessIp())
            .append("linkPerson", getLinkPerson())
            .append("linkPhone", getLinkPhone())
            .append("clientId", getClientId())
            .append("clientSecret", getClientSecret())
            .append("clientStartTime", getClientStartTime())
            .append("clientExpireDay", getClientExpireDay())
            .append("createId", getCreateId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
