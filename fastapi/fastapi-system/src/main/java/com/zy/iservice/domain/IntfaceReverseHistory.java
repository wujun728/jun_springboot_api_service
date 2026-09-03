package com.zy.iservice.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zy.common.annotation.Excel;
import com.zy.common.core.domain.BaseEntity;

/**
 * 接口调用历史对象 intface_reverse_history
 *
 * @author javachen
 * @date 2022-11-22
 */
public class IntfaceReverseHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** select_intface_manager表id */
    private Long selectIntfaceManagerId;

    /** 接口名称 */
    @Excel(name = "接口名称")
    private String name;

    /** app_manager表name */
    @Excel(name = "app_manager表name")
    private String appManagerName;

    /** app_manager表prefix (应用前缀==缓存前缀) */
    private String appManagerPrefix;

    /** 接口描述 */
    @Excel(name = "接口描述")
    private String depict;

    /** 输入参数 */
    @Excel(name = "输入参数")
    private String inputParam;

    /** 调用类型 1登录调用 2令牌调用 */
    @Excel(name = "调用类型 1登录调用 2令牌调用")
    private Long type;

    /** 调用人id */
    @Excel(name = "调用人id")
    private Long reverseUserId;

    /** 调用人 */
    @Excel(name = "调用人")
    private String reverseUserName;

    /** intface_app_apply_for表id */
    @Excel(name = "intface_app_apply_for表id")
    private Long reverseAppApplyForId;

    /** intface_app_apply_for表name */
    @Excel(name = "intface_app_apply_for表name")
    private String reverseAppApplyForName;

    /** 调用状态 1成功 2失败 */
    @Excel(name = "调用状态 1成功 2失败")
    private Long reverseStatus;

    /** 调用耗时(毫秒) */
    @Excel(name = "调用耗时(毫秒)")
    private Long reverseTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setSelectIntfaceManagerId(Long selectIntfaceManagerId)
    {
        this.selectIntfaceManagerId = selectIntfaceManagerId;
    }

    public Long getSelectIntfaceManagerId()
    {
        return selectIntfaceManagerId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
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
    public void setDepict(String depict)
    {
        this.depict = depict;
    }

    public String getDepict()
    {
        return depict;
    }
    public void setInputParam(String inputParam)
    {
        this.inputParam = inputParam;
    }

    public String getInputParam()
    {
        return inputParam;
    }
    public void setType(Long type)
    {
        this.type = type;
    }

    public Long getType()
    {
        return type;
    }
    public void setReverseUserId(Long reverseUserId)
    {
        this.reverseUserId = reverseUserId;
    }

    public Long getReverseUserId()
    {
        return reverseUserId;
    }
    public void setReverseUserName(String reverseUserName)
    {
        this.reverseUserName = reverseUserName;
    }

    public String getReverseUserName()
    {
        return reverseUserName;
    }
    public void setReverseAppApplyForId(Long reverseAppApplyForId)
    {
        this.reverseAppApplyForId = reverseAppApplyForId;
    }

    public Long getReverseAppApplyForId()
    {
        return reverseAppApplyForId;
    }
    public void setReverseAppApplyForName(String reverseAppApplyForName)
    {
        this.reverseAppApplyForName = reverseAppApplyForName;
    }

    public String getReverseAppApplyForName()
    {
        return reverseAppApplyForName;
    }

    public Long getReverseStatus() {
        return reverseStatus;
    }

    public void setReverseStatus(Long reverseStatus) {
        this.reverseStatus = reverseStatus;
    }

    public Long getReverseTime() {
        return reverseTime;
    }

    public void setReverseTime(Long reverseTime) {
        this.reverseTime = reverseTime;
    }

    @Override
    public String toString() {
        return "IntfaceReverseHistory{" +
                "id=" + id +
                ", selectIntfaceManagerId=" + selectIntfaceManagerId +
                ", name='" + name + '\'' +
                ", appManagerName='" + appManagerName + '\'' +
                ", appManagerPrefix='" + appManagerPrefix + '\'' +
                ", depict='" + depict + '\'' +
                ", inputParam='" + inputParam + '\'' +
                ", type=" + type +
                ", reverseUserId=" + reverseUserId +
                ", reverseUserName='" + reverseUserName + '\'' +
                ", reverseAppApplyForId=" + reverseAppApplyForId +
                ", reverseAppApplyForName='" + reverseAppApplyForName + '\'' +
                ", reverseStatus=" + reverseStatus +
                ", reverseTime=" + reverseTime +
                '}';
    }
}
