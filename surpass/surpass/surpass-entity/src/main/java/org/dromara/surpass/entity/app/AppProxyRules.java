package org.dromara.surpass.entity.app;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: wuyan
 * @time: 2026/02/05 9:54
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "surpass_app_proxy_rules")
@Entity
public class AppProxyRules extends JpaEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5551633673516662324L;

    @Id
    @Column
    @GeneratedValue
    String id;

    @Column
    private String alias;

    /**
     * 关联的代理资源ID，为空则代表通用配置
     */
    @Column
    private String proxyRuleId;

    /**
     * 应用ID
     */
    @Column
    private String appId;
    /**
     * 协议类型 (api_key, oauth2, jwt, basic_auth)
     */
    @Column
    private String protocolType;

    /**
     * 配置
     */
    @Column
    private String config;

    @SoftDelete
    @Column
    String deleted;

    @Column
    String status;

    @Column
    private String createdBy;

    @Column
    private Date createdDate;

    @Column
    private String modifiedBy;

    @Column
    private Date modifiedDate;
}
