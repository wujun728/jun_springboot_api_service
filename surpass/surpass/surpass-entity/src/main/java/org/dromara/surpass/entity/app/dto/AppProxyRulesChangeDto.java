package org.dromara.surpass.entity.app.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.surpass.validate.AddGroup;
import org.dromara.surpass.validate.EditGroup;


@Data
@EqualsAndHashCode(callSuper = false)
public class AppProxyRulesChangeDto {

    private String id;

    private String proxyRuleId;

    private String alias;

    @NotEmpty(message = "所属应用不能为空", groups = {AddGroup.class, EditGroup.class})
    private String appId;

    @NotEmpty(message = "代理协议不能为空", groups = {AddGroup.class, EditGroup.class})
    private String protocolType;

    private String config;

    String status;
}
