ALTER TABLE `SQLREST_API_ASSIGNMENT`
ADD COLUMN `response_format`  tinytext NULL comment '响应格式配置' AFTER `engine`,
ADD COLUMN `naming_strategy`  varchar(16) NOT NULL DEFAULT 'NONE' comment '命名策略' AFTER `response_format` ;

UPDATE SQLREST_API_ASSIGNMENT set response_format = '{"LOCAL_DATE":"yyyy-MM-dd","DATE":"yyyy-MM-dd","TIMESTAMP":"yyyy-MM-dd HH:mm","LOCAL_DATE_TIME":"yyyy-MM-dd HH:mm","TIME":"HH:mm:ss","BIG_DECIMAL":"6"}' where response_format is null;

INSERT INTO `SQLREST_SYSTEM_PARAM` (`param_key`, `param_type`, `param_value`) VALUES ('apiDocOpen', 'BOOLEAN', 'true');

