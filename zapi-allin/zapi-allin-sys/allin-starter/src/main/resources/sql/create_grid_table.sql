-- 创建表格基础信息表
CREATE TABLE IF NOT EXISTS `grid_base_info` (
    `id` VARCHAR(64) NOT NULL COMMENT '表格ID',
    `grid_name` VARCHAR(100) NOT NULL COMMENT '表格名称',
    `grid_description` VARCHAR(500) DEFAULT NULL COMMENT '表格描述',
    `grid_config` LONGTEXT DEFAULT NULL COMMENT '表格配置JSON',
    `entry_id` BIGINT DEFAULT NULL COMMENT '应用条目ID',
    `status` TINYINT DEFAULT 1 COMMENT '表格状态：0-禁用，1-启用',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_entry_id` (`entry_id`),
    KEY `idx_status` (`status`),
    KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表格基础信息表';
