-- 为 form_base_info 表添加 entry_id 字段
-- 执行时间: 2025-01-09

-- 添加应用目录ID字段
ALTER TABLE `form_base_info` 
ADD COLUMN `entry_id` BIGINT NULL COMMENT '应用目录ID' AFTER `form_description`;

-- 添加索引以提高查询性能
ALTER TABLE `form_base_info` 
ADD INDEX `idx_entry_id` (`entry_id`);

-- 添加外键约束（可选，如果需要强制引用完整性）
-- ALTER TABLE `form_base_info` 
-- ADD CONSTRAINT `fk_form_entry` 
-- FOREIGN KEY (`entry_id`) REFERENCES `wb_app_entry` (`id`) 
-- ON DELETE SET NULL 
-- ON UPDATE CASCADE;
