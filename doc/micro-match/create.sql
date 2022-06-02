-- ----------------------------
-- Table structure for nh_micro_user
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_user`;
CREATE TABLE `nh_micro_user` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) default NULL COMMENT 'Ԫ���ݱ�ʶ��Ԥ���ֶΣ�',
  `meta_name` varchar(100) default NULL COMMENT 'Ԫ��������',
  `meta_type` varchar(100) default NULL COMMENT 'Ԫ��������',
  `meta_content` text COMMENT 'Ԫ��������',
  `remark` varchar(200) default NULL COMMENT '��ע',
  `create_time` datetime default NULL COMMENT '����ʱ��',
  `update_time` datetime default NULL COMMENT '����ʱ��',
  `user_password` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' �û���¼';

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
  `rule_id` varchar(200) default NULL COMMENT '������',
  `rule_name` varchar(200) default NULL COMMENT '��������',
  `rule_param` text COMMENT '�������',
  `remark` varchar(1000) default NULL,
  `groovy_name` varchar(255) default NULL COMMENT '�����Ӧ���㷨����',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of match_rule
-- ----------------------------
INSERT INTO `match_rule` VALUES ('ce0a8fc8-5644-4e1f-bf28-cc85d177ed7d', 'rule_product', '��Ʒ���ͺ�ƽ̨���Ͷ�Ӧ����', '{\"pipeiList\":[{\"buyProduct\":\"1\",\"buyPlatform\":\"1\"}]}', '', 'match_rule_product');
INSERT INTO `match_rule` VALUES ('dddssaaa', 'rule_amount', '��Χ����', '{\"min\":100,\"max\":1000}', '', 'match_rule_amount');
INSERT INTO `match_rule` VALUES ('dddvvvvxx', 'rule_matchnum', '���������Χ����', '{\"min\":1,\"max\":2}', '', 'match_rule_matchnum');

CREATE TABLE `match_buy` (
  `id` varchar(50) NOT NULL,
  `user_name` varchar(255) default NULL COMMENT '�û���',
  `lender_rate` decimal(10,2) default NULL COMMENT '��������',
  `account_amount` decimal(10,2) default NULL COMMENT '����Ͻ��',
  `product_class` varchar(100) default NULL COMMENT '��Ʒ���',
  `platform_class` varchar(100) default NULL COMMENT 'ƽ̨���',
  `lender_start_date` datetime default NULL COMMENT '��������',
  `input_amount` decimal(10,2) default '0.00' COMMENT '�ҵ����',
  `create_time` datetime default NULL COMMENT '�ҵ�����ʱ��',
  `match_priority` int(11) default '0' COMMENT '������ȼ�',
  `update_time` timestamp NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `match_time` datetime default NULL COMMENT '���ʱ��',
  `lender_no` varchar(100) default NULL COMMENT '������',
  `user_id` varchar(100) default NULL COMMENT '�û����',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `match_sale` (
  `id` varchar(50) NOT NULL,
  `user_name` varchar(255) default NULL COMMENT '�û���',
  `borrow_rate` decimal(10,2) default NULL COMMENT '�������',
  `account_amount` decimal(10,2) default NULL COMMENT '����Ͻ��',
  `product_class` varchar(100) default NULL COMMENT '��Ʒ���',
  `platform_class` varchar(100) default NULL COMMENT 'ƽ̨���',
  `borrow_start_date` datetime default NULL COMMENT '��ʼʱ��',
  `input_amount` decimal(10,2) default '0.00' COMMENT '�ҵ����',
  `create_time` datetime default NULL COMMENT '�ҵ�ʱ��',
  `match_priority` int(11) default '0' COMMENT '������ȼ�',
  `update_time` timestamp NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `match_time` datetime default NULL COMMENT '���ʱ��',
  `borrow_no` varchar(100) default NULL COMMENT '�����',
  `user_id` varchar(100) default NULL COMMENT '����˱��',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `match_freeze` (
  `id` varchar(100) NOT NULL,
  `order_id` varchar(100) default NULL COMMENT '�ҵ�id',
  `table_name` varchar(50) default NULL COMMENT '�����򵥱�����',
  `match_id` varchar(50) default NULL COMMENT '���id',
  `create_time` datetime default NULL COMMENT '����ʱ��',
  `match_amount` decimal(10,2) default NULL COMMENT '������',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `match_result` (
  `id` varchar(50) NOT NULL,
  `match_id` varchar(100) default NULL COMMENT '���id',
  `buy_id` varchar(100) default NULL COMMENT '��id',
  `sale_id` varchar(100) default NULL COMMENT '����id',
  `match_amount` decimal(10,0) default NULL COMMENT '��Ͻ��',
  `create_time` datetime default NULL COMMENT '����ʱ��'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

