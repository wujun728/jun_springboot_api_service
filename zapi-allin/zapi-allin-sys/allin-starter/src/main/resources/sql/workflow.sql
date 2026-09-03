ALTER TABLE wf_process_definition
    ADD COLUMN app_id VARCHAR(64) COMMENT '应用ID' AFTER business_form_id;

-- 为应用ID字段添加索引，提升查询性能
CREATE INDEX idx_app_id ON wf_process_definition(app_id);



ALTER TABLE wf_process_instance
    ADD COLUMN app_id VARCHAR(64) COMMENT '应用ID' AFTER start_user;

-- 为应用ID字段添加索引，提升查询性能
CREATE INDEX idx_app_id ON wf_process_instance(app_id);
