-- 应用条目表
CREATE TABLE `wb_app_entry` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '目录名称',
  `type` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '目录类型： 分组group 表单form 表格grid',
  `parent_id` bigint DEFAULT NULL COMMENT '上级ID',
  `app_id` bigint DEFAULT NULL COMMENT '应用ID',
  `icon` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `color` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标颜色',
  `sort` int DEFAULT NULL COMMENT '排序字段（存相邻前驱节点ID）',
  `has_flow` int DEFAULT NULL COMMENT '是否是流程表单0否1是（流程和普通表单切换，默认普通表单）',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `create_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `is_deleted` int DEFAULT '0' COMMENT '是否删除(0正常，1删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_isDeleted` (`is_deleted`) USING BTREE,
  KEY `idx_app_id` (`app_id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='应用条目表';
