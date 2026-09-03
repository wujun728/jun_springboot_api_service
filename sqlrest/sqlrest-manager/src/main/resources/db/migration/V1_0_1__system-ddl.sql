create table `SQLREST_SYSTEM_USER`  (
  `id`                  bigint(20)            not null auto_increment   comment '主键id',
  `username`            varchar(255)          not null                  comment '登录名称',
  `password`            varchar(128)          not null                  comment '登录密码',
  `salt`                varchar(128)          not null                  comment '密码盐值',
  `real_name`           varchar(255)          not null default ''       comment '实际姓名',
  `email`               varchar(255)          not null default ''       comment '电子邮箱',
  `address`             varchar(255)          not null default ''       comment '所在地址',
  `locked`              tinyint(1)            not null default 0        comment '是否锁定',
  `create_time`         timestamp             not null default current_timestamp comment '创建时间',
  `update_time`         timestamp             not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (`id`),
  unique key (`username`)
) engine=InnoDB character set = utf8 comment = '系统用户表';

create table `SQLREST_DATASOURCE` (
  `id`                  bigint(20)   unsigned not null auto_increment            comment '主键',
  `name`                varchar(200)          not null default ''                comment '连接名称',
  `type`                varchar(200)          not null default ''                comment '数据库类型',
  `version`             varchar(255)          not null default ''                comment '驱动版本',
  `driver`              varchar(200)          not null default ''                comment '驱动类名称',
  `url`                 longtext                                                 comment 'jdbc-url连接串',
  `username`            varchar(200)          not null default ''                comment '连接账号',
  `password`            varchar(200)          not null default ''                comment '账号密码',
  `create_time`         timestamp             not null default current_timestamp comment '创建时间',
  `update_time`         timestamp             not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (`id`),
  unique key (`name`)
) engine=InnoDB default charset=utf8 comment='数据库连接';

CREATE TABLE `SQLREST_API_GROUP`
(
    `id`             bigint(20)   unsigned auto_increment                     comment '主键',
    `name`           varchar(255)          not null default ''                comment '分组名称',
    `create_time`    timestamp             not null default current_timestamp comment '创建时间',
    `update_time`    timestamp             not null default current_timestamp on update current_timestamp comment '修改时间',
    PRIMARY KEY (`id`) ,
    UNIQUE KEY `uk_name`(`name`)
) engine=InnoDB default charset=utf8 comment='接口分组表';

CREATE TABLE `SQLREST_API_MODULE`
(
    `id`             bigint(20)   unsigned auto_increment                     comment '主键',
    `name`           varchar(255)          not null default ''                comment '模块名称',
    `create_time`    timestamp             not null default current_timestamp comment '创建时间',
    `update_time`    timestamp             not null default current_timestamp on update current_timestamp comment '修改时间',
    PRIMARY KEY (`id`) ,
    UNIQUE KEY `uk_name`(`name`)
) engine=InnoDB default charset=utf8 comment='接口模块表';

CREATE TABLE `SQLREST_API_ASSIGNMENT`
(
    `id`             bigint(20)   unsigned not null auto_increment            comment '主键',
    `group_id`       bigint(20)   unsigned not null                           comment '分组ID',
    `module_id`      bigint(20)   unsigned not null                           comment '模块ID',
    `datasource_id`  bigint(20)   unsigned not null                           comment '数据源ID',
    `name`           varchar(255)          not null default ''                comment '接口名称',
    `description`    varchar(1024)                  default null              comment '接口描述',
    `method`         varchar(16)           not null default 'GET'             comment '请求方法',
    `path`           varchar(255)          not null default ''                comment '请求路径',
    `params`         text                                                     comment '入参JSON列表',
    `status`         tinyint(1)            not null default 0                 comment '是否发布',
    `open`           tinyint(1)            not null default 0                 comment '是否公开',
    `engine`         varchar(16)           not null default 'SQL'             comment '执行引擎',
    `flow_status`    tinyint(1)            not null default 0                 comment '是否开启流量控制',
    `flow_grade`     bigint(20)   unsigned          default null              comment '流控类型',
    `flow_count`     bigint(20)   unsigned          default null              comment '流控阈值',
    `content_type`   varchar(50)           not null default ''                comment 'ContentType',
    `create_time`    timestamp             not null default current_timestamp comment '创建时间',
    `update_time`    timestamp             not null default current_timestamp on update current_timestamp comment '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_method_path`(`method`,`path`),
    foreign key (`group_id`) references `SQLREST_API_GROUP` (`id`) on delete cascade on update cascade,
    foreign key (`module_id`) references `SQLREST_API_MODULE` (`id`) on delete cascade on update cascade,
    foreign key (`datasource_id`) references `SQLREST_DATASOURCE` (`id`) on delete cascade on update cascade
) engine=InnoDB auto_increment=1 default charset=utf8 comment='接口配置表';

CREATE TABLE `SQLREST_API_CONTEXT`
(
    `id`                      bigint(20)   unsigned auto_increment             comment '主键',
    `api_id`                  bigint(20)   unsigned not null                   comment 'API接口ID',
    `sql_text`                text                  not null                   comment 'SQL内容',
    primary key (`id`),
    foreign key (`api_id`) references `SQLREST_API_ASSIGNMENT` (`id`) on delete cascade on update cascade
) engine=InnoDB default charset=utf8 comment='接口sql表';

CREATE TABLE `SQLREST_FIREWALL_RULES`
(
    `id`             bigint(20)   unsigned auto_increment                     comment '主键',
    `status`         varchar(4)            not null default 'OFF'             comment '状态：OFF-关闭;ON-启用',
    `mode`           varchar(16)           not null default 'BLACK'           comment '状态：WHITE-白名单;BLACK-黑名单',
    `addresses`      text                  default null                       comment '客户端地址(IP)列表',
    `create_time`    timestamp             not null default current_timestamp comment '创建时间',
    `update_time`    timestamp             not null default current_timestamp on update current_timestamp comment '修改时间',
    PRIMARY KEY (`id`)
) engine=InnoDB default charset=utf8 comment='防火墙规则表';

CREATE TABLE `SQLREST_APP_CLIENT`
(
    `id`              bigint(20)   unsigned auto_increment                     comment '主键',
    `name`            varchar(255)          not null                           comment '应用客户端名称',
    `description`     varchar(1024)         default null                       comment '应用客户端描述',
    `app_key`         varchar(64)           not null                           comment '应用的账号',
    `app_secret`      varchar(64)           not null                           comment '应用的密钥',
    `expire_duration` varchar(16)           not null default 'FOR_EVER'        comment '过期类型',
    `expire_at`       bigint(20)            not null default '0'               comment '过期时间',
    `access_token`    varchar(64)           default null                       comment '最近TOKEN',
    `create_time`     timestamp             not null default current_timestamp comment '创建时间',
    `update_time`     timestamp             not null default current_timestamp on update current_timestamp comment '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_app_key`(`app_key`)
) engine=InnoDB default charset=utf8 comment='客户端应用表';

CREATE TABLE `SQLREST_CLIENT_GROUP`
(
    `id`        bigint(20)  unsigned auto_increment  comment '主键',
    `client_id` bigint(20)  not null                 comment '客户端应用ID',
    `group_id`  bigint(20)  not null                 comment 'API分组ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_client_group`(`client_id`,`group_id`)
) engine=InnoDB default charset=utf8 comment='客户端应用授权分组配置表';

CREATE TABLE `SQLREST_ACCESS_RECORD`
(
    `id`              bigint(20)   unsigned auto_increment       comment '主键',
    `path`            varchar(255)       default null            comment '路径',
    `status`          bigint(11)         default null            comment 'HTTP状态码',
    `duration`        bigint(20)         default null            comment '处理时间',
    `ip_addr`         varchar(64)        default null            comment '客户端IP',
    `user_agent`      varchar(255)       default null            comment '客户端UA',
    `client_key`      varchar(64)        default null            comment '客户端Key',
    `api_id`          varchar(50)        default null            comment 'API接口ID',
    `exception`       varchar(1024)      default null            comment '错误日志',
    `create_time`     timestamp          not null default current_timestamp comment '创建时间',
    PRIMARY KEY (`id`)
) engine=InnoDB default charset=utf8 comment='客户端应用接口访问日志表';

CREATE TABLE `SQLREST_SYSTEM_PARAM` (
  `id`               bigint(20)        NOT NULL AUTO_INCREMENT  COMMENT '主键id',
  `param_key`        varchar(128)      NOT NULL                 COMMENT '参数KEY',
  `param_type`       varchar(64)       NOT NULL                 COMMENT '值类型',
  `param_value`      varchar(255)      NOT NULL                 COMMENT '参数值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `param_key` (`param_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统参数表';
