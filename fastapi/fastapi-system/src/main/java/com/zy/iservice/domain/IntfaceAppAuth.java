package com.zy.iservice.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zy.common.annotation.Excel;
import com.zy.common.core.domain.BaseEntity;

/**
 * 接口应用授权对象 intface_app_auth
 *
 * @author javachen
 * @date 2022-11-19
 */
public class IntfaceAppAuth extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** intface_app_apply_for表id */
    private Long intfaceAppId;

    /** select_intface_manager表id
 */
    private Long selectIntfaceId;

    public void setIntfaceAppId(Long intfaceAppId)
    {
        this.intfaceAppId = intfaceAppId;
    }

    public Long getIntfaceAppId()
    {
        return intfaceAppId;
    }
    public void setSelectIntfaceId(Long selectIntfaceId)
    {
        this.selectIntfaceId = selectIntfaceId;
    }

    public Long getSelectIntfaceId()
    {
        return selectIntfaceId;
    }

    public IntfaceAppAuth() {
    }

    public IntfaceAppAuth(Long intfaceAppId, Long selectIntfaceId) {
        this.intfaceAppId = intfaceAppId;
        this.selectIntfaceId = selectIntfaceId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("intfaceAppId", getIntfaceAppId())
            .append("selectIntfaceId", getSelectIntfaceId())
            .toString();
    }
}
