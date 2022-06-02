-- ----------------------------
-- Table structure for nh_micro_user
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_user`;
CREATE TABLE `nh_micro_user` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) default NULL COMMENT '元数据标识（预留字段）',
  `meta_name` varchar(100) default NULL COMMENT '元数据名称',
  `meta_type` varchar(100) default NULL COMMENT '元数据类型',
  `meta_content` text COMMENT '元数据内容',
  `remark` varchar(200) default NULL COMMENT '备注',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  `user_password` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' 用户登录';

-- ----------------------------
-- Records of nh_micro_user
-- ----------------------------
INSERT INTO `nh_micro_user` VALUES ('1', 'guest', null, null, null, null, null, null, 'guest');
INSERT INTO `nh_micro_user` VALUES ('2', 'admin', null, null, null, null, null, null, 'admin');

-- ----------------------------
-- Table structure for match_rule
-- ----------------------------
DROP TABLE IF EXISTS `match_rule`;
CREATE TABLE `match_rule` (
  `id` varchar(50) NOT NULL,
  `rule_id` varchar(200) default NULL COMMENT '规则编号',
  `rule_name` varchar(200) default NULL COMMENT '规则名称',
  `rule_param` text COMMENT '规则参数',
  `remark` varchar(1000) default NULL,
  `groovy_name` varchar(255) default NULL COMMENT '规则对应的算法名称',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of match_rule
-- ----------------------------
INSERT INTO `match_rule` VALUES ('ce0a8fc8-5644-4e1f-bf28-cc85d177ed7d', 'rule_product', '产品类型和平台类型对应规则', '{\"pipeiList\":[{\"buyProduct\":\"1\",\"buyPlatform\":\"1\"}]}', '', 'match_rule_product');
INSERT INTO `match_rule` VALUES ('dddssaaa', 'rule_amount', '金额范围规则', '{\"min\":100,\"max\":1000}', '', 'match_rule_amount');
INSERT INTO `match_rule` VALUES ('dddvvvvxx', 'rule_matchnum', '撮合条数范围规则', '{\"min\":1,\"max\":2}', '', 'match_rule_matchnum');

CREATE TABLE `match_buy` (
  `id` varchar(50) NOT NULL,
  `user_name` varchar(255) default NULL COMMENT '用户名',
  `lender_rate` decimal(10,2) default NULL COMMENT '出借利率',
  `account_amount` decimal(10,2) default NULL COMMENT '待撮合金额',
  `product_class` varchar(100) default NULL COMMENT '产品类别',
  `platform_class` varchar(100) default NULL COMMENT '平台类别',
  `lender_start_date` datetime default NULL COMMENT '出借日期',
  `input_amount` decimal(10,2) default '0.00' COMMENT '挂单金额',
  `create_time` datetime default NULL COMMENT '挂单创建时间',
  `match_priority` int(11) default '0' COMMENT '撮合优先级',
  `update_time` timestamp NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
  `match_time` datetime default NULL COMMENT '撮合时间',
  `lender_no` varchar(100) default NULL COMMENT '出借编号',
  `user_id` varchar(100) default NULL COMMENT '用户编号',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `match_sale` (
  `id` varchar(50) NOT NULL,
  `user_name` varchar(255) default NULL COMMENT '用户名',
  `borrow_rate` decimal(10,2) default NULL COMMENT '借款利率',
  `account_amount` decimal(10,2) default NULL COMMENT '待撮合金额',
  `product_class` varchar(100) default NULL COMMENT '产品类别',
  `platform_class` varchar(100) default NULL COMMENT '平台类别',
  `borrow_start_date` datetime default NULL COMMENT '借款开始时间',
  `input_amount` decimal(10,2) default '0.00' COMMENT '挂单金额',
  `create_time` datetime default NULL COMMENT '挂单时间',
  `match_priority` int(11) default '0' COMMENT '撮合优先级',
  `update_time` timestamp NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
  `match_time` datetime default NULL COMMENT '撮合时间',
  `borrow_no` varchar(100) default NULL COMMENT '借款编号',
  `user_id` varchar(100) default NULL COMMENT '借款人编号',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `match_freeze` (
  `id` varchar(100) NOT NULL,
  `order_id` varchar(100) default NULL COMMENT '挂单id',
  `table_name` varchar(50) default NULL COMMENT '卖单买单表区分',
  `match_id` varchar(50) default NULL COMMENT '撮合id',
  `create_time` datetime default NULL COMMENT '冻结时间',
  `match_amount` decimal(10,2) default NULL COMMENT '冻结金额',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `match_result` (
  `id` varchar(50) NOT NULL,
  `match_id` varchar(100) default NULL COMMENT '撮合id',
  `buy_id` varchar(100) default NULL COMMENT '买单id',
  `sale_id` varchar(100) default NULL COMMENT '卖单id',
  `match_amount` decimal(10,0) default NULL COMMENT '撮合金额',
  `create_time` datetime default NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

