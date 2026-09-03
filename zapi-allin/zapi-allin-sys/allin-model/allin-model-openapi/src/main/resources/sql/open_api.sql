-- 开放接口配置表
CREATE TABLE `open_api_config` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` bigint NOT NULL COMMENT '所属应用ID（关联sys_application）',
  `config_name` varchar(100) NOT NULL COMMENT '配置名称',
  `call_type` tinyint NOT NULL COMMENT '调用类型：1-被动（提供接口） 2-主动（调用外部）',
  `data_type` varchar(20) NOT NULL COMMENT '数据类型：user-用户 role-角色 dept-部门',
  `sync_mode` tinyint NOT NULL COMMENT '同步方式：1-全量 2-增量',
  
  -- 被动模式配置（外部调用本系统）
  `api_path` varchar(255) DEFAULT NULL COMMENT '接口路径（被动模式）',
  `api_method` varchar(10) DEFAULT NULL COMMENT '请求方法：GET/POST/PUT/DELETE',
  
  -- 主动模式配置（本系统调用外部）
  `remote_url` varchar(500) DEFAULT NULL COMMENT '远程接口地址（主动模式）',
  `remote_method` varchar(10) DEFAULT NULL COMMENT '远程请求方法',
  `request_headers` text DEFAULT NULL COMMENT '请求头配置（JSON格式）',
  `request_params` text DEFAULT NULL COMMENT '请求参数模板（JSON格式）',
  `response_mapping` text DEFAULT NULL COMMENT '响应字段映射（JSON格式）',
  
  -- 增量同步配置
  `increment_field` varchar(50) DEFAULT NULL COMMENT '增量字段（如update_time）',
  `last_sync_time` datetime DEFAULT NULL COMMENT '上次同步时间',
  `last_sync_value` varchar(100) DEFAULT NULL COMMENT '上次同步值',
  
  -- 定时任务配置（主动模式）
  `cron_expression` varchar(50) DEFAULT NULL COMMENT 'Cron表达式（主动定时同步）',
  `auto_sync` tinyint DEFAULT 0 COMMENT '是否自动同步：0-否 1-是',
  
  -- 通用字段
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updater` varchar(50) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_app_id` (`app_id`),
  KEY `idx_data_type` (`data_type`),
  KEY `idx_call_type` (`call_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='开放接口配置表';

-- 接口调用日志表
CREATE TABLE `open_api_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_id` bigint NOT NULL COMMENT '配置ID',
  `app_id` bigint NOT NULL COMMENT '应用ID',
  `call_type` tinyint NOT NULL COMMENT '调用类型：1-被动 2-主动',
  `request_url` varchar(500) DEFAULT NULL COMMENT '请求地址',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方法',
  `request_params` text DEFAULT NULL COMMENT '请求参数',
  `response_data` text DEFAULT NULL COMMENT '响应数据',
  `status` tinyint NOT NULL COMMENT '状态：0-失败 1-成功',
  `error_msg` text DEFAULT NULL COMMENT '错误信息',
  `sync_count` int DEFAULT 0 COMMENT '同步数据条数',
  `cost_time` bigint DEFAULT NULL COMMENT '耗时（毫秒）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '调用时间',
  `client_ip` varchar(50) DEFAULT NULL COMMENT '客户端IP（被动模式）',
  PRIMARY KEY (`id`),
  KEY `idx_config_id` (`config_id`),
  KEY `idx_app_id` (`app_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口调用日志表';
