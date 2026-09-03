

-- ----------- 菜单添加相对应的数据
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_code`, `menu_type`, `path`, `component`, `icon`, `sort`, `status`, `visible`, `create_time`, `update_time`, `deleted`)
VALUES
    (41, 0, '电子签章', 'contract', 1, '/contract', '', 'Avatar', 2, 1, 1, '2025-11-13 09:56:10', '2025-12-05 17:42:34', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_code`, `menu_type`, `path`, `component`, `icon`, `sort`, `status`, `visible`, `create_time`, `update_time`, `deleted`)
VALUES
    (42, 41, '合同列表', '/myContract', 2, '/contract/index', 'contract/index', 'DocumentCopy', 1, 1, 1, '2025-11-13 09:56:57', '2025-12-05 17:37:06', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_code`, `menu_type`, `path`, `component`, `icon`, `sort`, `status`, `visible`, `create_time`, `update_time`, `deleted`)
VALUES
    (43, 41, '我的印章', 'mySeal', 2, '/system/seal', 'system/seal/index', 'Cloudy', 2, 1, 1, '2025-11-13 09:57:35', '2025-12-05 17:37:10', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_code`, `menu_type`, `path`, `component`, `icon`, `sort`, `status`, `visible`, `create_time`, `update_time`, `deleted`)
VALUES
    (44, 41, '我的证书', 'myCert', 2, '/system/cert', 'system/cert/index', 'Coordinate', 3, 1, 1, '2025-11-13 09:58:07', '2025-12-05 17:37:12', 0);


-- ----------------------------
-- 之前这张表没了，，，重新添加
-- ----------------------------
DROP TABLE IF EXISTS `contract_info`;
CREATE TABLE `contract_info` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `contract_no` varchar(50) NOT NULL COMMENT '合同编号',
                                 `contract_name` varchar(200) NOT NULL COMMENT '合同名称',
                                 `contract_type` varchar(50) DEFAULT NULL COMMENT '合同类型',
                                 `file_id` bigint DEFAULT NULL COMMENT '合同文件ID（关联文件表）',
                                 `initiator_id` bigint NOT NULL COMMENT '发起人ID（用户ID）',
                                 `initiator_name` varchar(100) DEFAULT NULL COMMENT '发起人姓名',
                                 `contract_status` varchar(20) NOT NULL DEFAULT 'DRAFT' COMMENT '合同状态：DRAFT-草稿，REVIEWING-审核中，PENDING_SIGN-待签署，SIGNED-签署完成，ARCHIVED-归档',
                                 `contract_amount` decimal(18,2) DEFAULT NULL COMMENT '合同金额',
                                 `start_date` date DEFAULT NULL COMMENT '合同开始日期',
                                 `end_date` date DEFAULT NULL COMMENT '合同结束日期',
                                 `reviewer_id` bigint DEFAULT NULL COMMENT '审核人ID',
                                 `reviewer_name` varchar(100) DEFAULT NULL COMMENT '审核人姓名',
                                 `review_time` datetime DEFAULT NULL COMMENT '审核时间',
                                 `review_comment` varchar(500) DEFAULT NULL COMMENT '审核意见',
                                 `signer_id` bigint DEFAULT NULL COMMENT '签署人ID',
                                 `signer_name` varchar(100) DEFAULT NULL COMMENT '签署人姓名',
                                 `sign_time` datetime DEFAULT NULL COMMENT '签署时间',
                                 `archive_time` datetime DEFAULT NULL COMMENT '归档时间',
                                 `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='合同信息表';


-- ----------------------------
-- 盖章记录表
-- ----------------------------
DROP TABLE IF EXISTS `contract_sign_info`;
CREATE TABLE `contract_sign_info` (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
                                      `sign_front_file_id` bigint DEFAULT NULL COMMENT '签署前的文件（关联文件uid）',
                                      `sign_back_file_id` bigint DEFAULT NULL COMMENT '签署后的文件（关联文件uid）',
                                      `seal_id` bigint DEFAULT NULL COMMENT '印章id（关联印章信息表id）',
                                      `cert_id` bigint DEFAULT NULL COMMENT '证书id（关联证书信息表id）',
                                      `sign_algorithm` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '签署签名算法（常见的RSA，国办SM2）',
                                      `sign_page` int DEFAULT NULL COMMENT 'pdf签署页码',
                                      `pdf_page_count` int DEFAULT NULL COMMENT 'pdf总页数',
                                      `pdf_width` int DEFAULT NULL COMMENT 'pdf宽度',
                                      `pdf_height` int DEFAULT NULL COMMENT 'pdf高度',
                                      `coord_x` int DEFAULT NULL COMMENT '坐标x',
                                      `coord_y` int DEFAULT NULL COMMENT '坐标y',
                                      `pixel_size` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '像素大小（数值表示例如：100，120，表示宽100px，高120）',
                                      `sign_time` datetime DEFAULT NULL COMMENT '签署时间',
                                      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='合同签署信息表';
