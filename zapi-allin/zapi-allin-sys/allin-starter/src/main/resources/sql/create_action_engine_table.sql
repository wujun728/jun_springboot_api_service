-- 动作执行引擎表
CREATE TABLE IF NOT EXISTS `action_engine` (
    `id` BIGINT(20) NOT NULL COMMENT '主键ID',
    `app_id` VARCHAR(64) NOT NULL COMMENT '应用ID',
    `action_name` VARCHAR(100) NOT NULL COMMENT '动作名称',
    `action_description` VARCHAR(500) DEFAULT NULL COMMENT '动作描述',
    `action_type` VARCHAR(50) NOT NULL COMMENT '动作类型',
    `action_config` TEXT COMMENT '动作配置（JSON格式）',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag` TINYINT(1) DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_app_id` (`app_id`),
    KEY `idx_action_name` (`action_name`),
    KEY `idx_action_type` (`action_type`),
    KEY `idx_status` (`status`),
    KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动作执行引擎表';
