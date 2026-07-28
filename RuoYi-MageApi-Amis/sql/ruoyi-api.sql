-- =====================================================
-- ruoyi-api 扩展模块 表结构 + 初始化数据
-- 数据库: MySQL 8.x, utf8mb4_bin
-- =====================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 插件配置表
-- ----------------------------
DROP TABLE IF EXISTS `api_plugin_config`;
CREATE TABLE `api_plugin_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `plugin_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '插件类名',
  `plugin_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '插件类型(cache/transform/alarm)',
  `plugin_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '插件名称',
  `plugin_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '插件参数(JSON)',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '1' COMMENT '状态(1启用 0禁用)',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='API插件配置';

-- ----------------------------
-- 2. Groovy API 配置表
-- ----------------------------
DROP TABLE IF EXISTS `api_groovy_config`;
CREATE TABLE `api_groovy_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `api_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'API路径',
  `api_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'API名称',
  `script_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'groovy' COMMENT '脚本类型(groovy/sql/java)',
  `script_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '脚本内容',
  `datasource_id` bigint DEFAULT NULL COMMENT '数据源ID',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '1' COMMENT '状态(1启用 0禁用)',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_api_path` (`api_path`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='Groovy API配置';

-- ----------------------------
-- 3. 聚合API配置表
-- ----------------------------
DROP TABLE IF EXISTS `api_aggregate_config`;
CREATE TABLE `api_aggregate_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `prefix` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用前缀',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'API名称(多个用逗号分隔)',
  `sql_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT 'SQL内容',
  `datasource_id` bigint DEFAULT NULL COMMENT '数据源ID',
  `result_type` int DEFAULT '2' COMMENT '结果类型(1单对象 2列表)',
  `kv_conver` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'KV转换配置(JSON)',
  `rc_conver` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '行列转换配置(JSON)',
  `key_extract` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Key提取配置(JSON)',
  `open_cache` int DEFAULT '0' COMMENT '启用缓存(1是 0否)',
  `auth_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'authToken' COMMENT '认证类型(authToken/authCode)',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '1' COMMENT '状态(1启用 0禁用)',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_prefix_name` (`prefix`, `name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='聚合API配置';

-- ----------------------------
-- 4. 开放平台应用表
-- ----------------------------
DROP TABLE IF EXISTS `api_openapi_app`;
CREATE TABLE `api_openapi_app` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `app_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用Key',
  `app_secret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用Secret',
  `app_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '应用名称',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '1' COMMENT '状态(1启用 0禁用)',
  `ip_whitelist` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'IP白名单(逗号分隔)',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_app_key` (`app_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='开放平台应用';

-- ----------------------------
-- 5. API版本记录表
-- ----------------------------
DROP TABLE IF EXISTS `api_version_record`;
CREATE TABLE `api_version_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `api_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'API标识',
  `api_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'API路径',
  `api_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'API名称',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT 'API内容',
  `diff_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '差异内容',
  `version_no` int DEFAULT '1' COMMENT '版本号',
  `operate_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'update' COMMENT '操作类型(create/update/rollback)',
  `operate_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '操作人',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_api_id` (`api_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='API版本记录';

-- ----------------------------
-- 6. 数据源配置表
-- ----------------------------
DROP TABLE IF EXISTS `api_datasource`;
CREATE TABLE `api_datasource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `ds_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '数据源名称',
  `ds_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'mysql' COMMENT '数据库类型(mysql/oracle/postgresql/sqlserver)',
  `ds_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '连接URL',
  `ds_driver` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '驱动类',
  `ds_username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `ds_password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '1' COMMENT '状态(1启用 0禁用)',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='API数据源配置';

-- ----------------------------
-- 初始化数据
-- ----------------------------

-- 开放平台应用初始化
INSERT INTO `api_openapi_app` VALUES (1, 'test_app_key', 'test_app_secret', '测试应用', '1', '127.0.0.1,localhost', NULL, 'admin', NOW(), 'admin', NOW());

-- 数据源初始化 (假设有本地MySQL)
INSERT INTO `api_datasource` VALUES (1, '本地MySQL', 'mysql', 'jdbc:mysql://localhost:3306/ruoyi_online?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8', 'com.mysql.cj.jdbc.Driver', 'root', 'root', '1', 'admin', NOW(), 'admin', NOW());

-- 聚合API示例
INSERT INTO `api_aggregate_config` VALUES (1, 'demo', 'test', 'SELECT 1 as id, \'test\' as name', 1, 2, NULL, NULL, NULL, 0, 'authToken', '1', 'admin', NOW(), 'admin', NOW());

-- Groovy API示例
INSERT INTO `api_groovy_config` VALUES (1, '/api/demo/hello', 'Hello示例', 'groovy', 'return [message: \"Hello from Groovy!\", time: new Date().toString()]', 1, '1', 'admin', NOW(), 'admin', NOW());

-- 版本记录示例
INSERT INTO `api_version_record` VALUES (1, 'demo-api', '/demo/hello', 'Hello API', 'return \"v1\"', '[new]', 1, 'create', 'admin', NOW());

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 7. 系统菜单 (API管理目录)
-- ----------------------------
-- 注意: 若已运行过主SQL(RuoYi-MageApi-Amis.sql), sys_menu表已存在
-- 以下菜单ID从2000开始, 不冲突
INSERT IGNORE INTO `sys_menu` VALUES (2000, 'API管理', 0, 5, 'api', NULL, NULL, 1, 0, 'M', '0', '0', '', 'tree', 'admin', NOW(), 'admin', NOW(), 'API管理目录');
INSERT IGNORE INTO `sys_menu` VALUES (2001, '数据源管理', 2000, 1, 'datasource', 'api/datasource/index', NULL, 1, 0, 'C', '0', '0', 'api:datasource:list', 'list', 'admin', NOW(), 'admin', NOW(), '');
INSERT IGNORE INTO `sys_menu` VALUES (2002, '插件管理', 2000, 2, 'plugin', 'api/plugin/index', NULL, 1, 0, 'C', '0', '0', 'api:plugin:list', 'list', 'admin', NOW(), 'admin', NOW(), '');
INSERT IGNORE INTO `sys_menu` VALUES (2003, 'Groovy API', 2000, 3, 'groovy', 'api/groovy/index', NULL, 1, 0, 'C', '0', '0', 'api:groovy:list', 'list', 'admin', NOW(), 'admin', NOW(), '');
INSERT IGNORE INTO `sys_menu` VALUES (2004, '聚合API', 2000, 4, 'aggregate', 'api/aggregate/index', NULL, 1, 0, 'C', '0', '0', 'api:aggregate:list', 'list', 'admin', NOW(), 'admin', NOW(), '');
INSERT IGNORE INTO `sys_menu` VALUES (2005, 'MCP Server', 2000, 5, 'mcp', 'api/mcp/index', NULL, 1, 0, 'C', '0', '0', 'api:mcp:list', 'list', 'admin', NOW(), 'admin', NOW(), '');
INSERT IGNORE INTO `sys_menu` VALUES (2006, '开放平台', 2000, 6, 'openapi', 'api/openapi/index', NULL, 1, 0, 'C', '0', '0', 'api:openapi:list', 'list', 'admin', NOW(), 'admin', NOW(), '');
INSERT IGNORE INTO `sys_menu` VALUES (2007, '版本历史', 2000, 7, 'version', 'api/version/index', NULL, 1, 0, 'C', '0', '0', 'api:version:list', 'list', 'admin', NOW(), 'admin', NOW(), '');