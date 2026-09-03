package org.dromara.surpass.entity.app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.mybatis.jpa.entity.JpaPage;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/17 16:53
 */

@Data
@EqualsAndHashCode(callSuper=false)
public class AppProxyRulesPageDto extends JpaPage {
    private String protocolType;
    private String appId;
    private String status;
    private String proxyRuleId;

}
