package com.zy.iservice.domain.bo;

import com.zy.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 接口应用授权对象 intface_app_auth
 *
 * @author javachen
 * @date 2022-11-19
 */
public class InsertUpdateIntfaceAppAuthBO extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    private Long intfaceAppId;

    private Long [] selectIntfaceId;

    public Long getIntfaceAppId() {
        return intfaceAppId;
    }

    public void setIntfaceAppId(Long intfaceAppId) {
        this.intfaceAppId = intfaceAppId;
    }

    public Long[] getSelectIntfaceId() {
        return selectIntfaceId;
    }

    public void setSelectIntfaceId(Long[] selectIntfaceId) {
        this.selectIntfaceId = selectIntfaceId;
    }

    public InsertUpdateIntfaceAppAuthBO(Long intfaceAppId, Long[] selectIntfaceId) {
        this.intfaceAppId = intfaceAppId;
        this.selectIntfaceId = selectIntfaceId;
    }

    public InsertUpdateIntfaceAppAuthBO() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("intfaceAppId", getIntfaceAppId())
            .append("selectIntfaceId", getSelectIntfaceId())
            .toString();
    }
}
