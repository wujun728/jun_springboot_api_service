CREATE TABLE surpass_app_proxy_rules
(
    id            VARCHAR(45)            NOT NULL COMMENT 'ID',
    proxy_rule_id VARCHAR(45) COMMENT '关联的代理资源ID，为空则代表通用配置',
    app_id        VARCHAR(45)            NOT NULL COMMENT '所属应用ID',
    alias         VARCHAR(45)            NOT NULL COMMENT '别名',
    protocol_type VARCHAR(50)            NOT NULL COMMENT '协议类型 (api_key, oauth2, jwt, basic_auth)',
    config        varchar(2048)          NOT NULL COMMENT '认证配置详情 (JSON格式存储动态参数)',
    created_by    VARCHAR(45)            NULL COMMENT '创建人',
    created_date  TIMESTAMP              NULL COMMENT '创建时间',
    modified_by   VARCHAR(45)            NULL COMMENT '修改人',
    modified_date TIMESTAMP              NULL COMMENT '修改时间',
    status        varchar(45)            null comment '状态',
    deleted       varchar(1) default 'n' null comment '删除标记',
    -- 主键
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='反向代理认证协议配置表';