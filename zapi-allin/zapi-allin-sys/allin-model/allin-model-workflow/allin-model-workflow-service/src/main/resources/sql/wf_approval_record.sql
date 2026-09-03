-- 流程审批节点记录表
-- 设计思路：一个审批节点执行一次 = 一条记录，审批目标以JSON格式存储

DROP TABLE IF EXISTS wf_approval_record;

CREATE TABLE wf_approval_record (
    id VARCHAR(64) NOT NULL COMMENT '审批记录ID（UUID）',
    process_instance_id VARCHAR(64) NOT NULL COMMENT '流程实例ID',
    process_definition_id VARCHAR(64) NOT NULL COMMENT '流程定义ID',
    node_id VARCHAR(64) NOT NULL COMMENT '审批节点ID',
    node_name VARCHAR(128) COMMENT '审批节点名称',
    approval_targets JSON COMMENT '审批目标配置（JSON格式）：[{"id":1,"type":"user","name":"张三"}]',
    approval_user_ids JSON COMMENT '展开后的实际审批用户ID列表（JSON数组）：["1","2","3"]',
    approval_user_names VARCHAR(1024) COMMENT '展开后的实际审批用户姓名（顿号分隔）',
    approval_type VARCHAR(32) DEFAULT 'ANY' COMMENT '审批方式：ANY-或签（任意一人审批）/ALL-会签（所有人审批）',
    status TINYINT DEFAULT 1 COMMENT '审批状态：1-进行中 2-已通过 3-已驳回',
    total_count INT DEFAULT 0 COMMENT '总审批人数',
    approved_count INT DEFAULT 0 COMMENT '已通过审批人数',
    rejected_count INT DEFAULT 0 COMMENT '已驳回审批人数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (id),
    INDEX idx_process_instance_id (process_instance_id),
    INDEX idx_process_definition_id (process_definition_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程审批节点记录表';

-- 如果表已存在，使用以下语句添加新字段：
-- ALTER TABLE wf_approval_record ADD COLUMN total_count INT DEFAULT 0 COMMENT '总审批人数' AFTER status;
-- ALTER TABLE wf_approval_record ADD COLUMN approved_count INT DEFAULT 0 COMMENT '已通过审批人数' AFTER total_count;
-- ALTER TABLE wf_approval_record ADD COLUMN rejected_count INT DEFAULT 0 COMMENT '已驳回审批人数' AFTER approved_count;

-- 注意：查询"我的待办"时使用 JSON_CONTAINS(approval_user_ids, '"用户ID"') 进行查询
-- 示例：SELECT * FROM wf_approval_record WHERE JSON_CONTAINS(approval_user_ids, '"2"') AND status = 1 AND deleted = 0;
