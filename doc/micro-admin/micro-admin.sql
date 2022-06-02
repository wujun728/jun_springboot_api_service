/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50018
Source Host           : localhost:3306
Source Database       : micro-admin

Target Server Type    : MYSQL
Target Server Version : 50018
File Encoding         : 65001

Date: 2018-10-30 15:40:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for nh_micro_dict_items
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_dict_items`;
CREATE TABLE `nh_micro_dict_items` (
  `id` varchar(255) NOT NULL,
  `item_id` varchar(100) default NULL,
  `item_name` varchar(100) default NULL,
  `dict_id` varchar(100) default NULL,
  `create_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典项表';

-- ----------------------------
-- Records of nh_micro_dict_items
-- ----------------------------
INSERT INTO `nh_micro_dict_items` VALUES ('62991084-583e-4e36-84cb-477cd3feee14', '1', '男', 'xingbie', null);
INSERT INTO `nh_micro_dict_items` VALUES ('62991084-583e-4e36-84cb-477cd3feee15', '2', '女', 'xingbie', null);

-- ----------------------------
-- Table structure for nh_micro_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_dictionary`;
CREATE TABLE `nh_micro_dictionary` (
  `id` varchar(255) NOT NULL,
  `dict_id` varchar(100) default NULL,
  `dict_name` varchar(100) default NULL,
  `create_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';

-- ----------------------------
-- Records of nh_micro_dictionary
-- ----------------------------
INSERT INTO `nh_micro_dictionary` VALUES ('563bab75-dc9b-4f22-9c0c-feb49678ee50', 'xingbie', '性别', null);

-- ----------------------------
-- Table structure for nh_micro_ref_menu_role
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_ref_menu_role`;
CREATE TABLE `nh_micro_ref_menu_role` (
  `id` varchar(50) NOT NULL,
  `menu_id` varchar(50) default NULL,
  `role_id` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单角色映射表';

-- ----------------------------
-- Records of nh_micro_ref_menu_role
-- ----------------------------
INSERT INTO `nh_micro_ref_menu_role` VALUES ('0019150d-a12d-4ae4-90cc-836abf8481da', 'demo2', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-be12-b3a816cc3196', 'account_manage', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-bed2-b3a816cc4886', 'demo3', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-bed2-b3a816cc4887', 'demo4', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-bed2-b3a816cc4888', 'demo4-popup', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-bed2-b3a816cc4889', 'demo4-notice', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-bed2-b3a816cc4890', 'demo5', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-bed2-b3a816cc4891', 'demo5-button', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-bed2-b3a816cc4892', 'demo5-load', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-bed2-b3a816cc4893', 'demo5-icon', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-bed2-b3a816cc4894', 'demo6', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-bed2-b3a816cc4895', 'demo7', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-bed2-b3a816cc4896', 'demo8', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-4442-bed2-b3a816cc4897', 'menu', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('02833c3e-6e6f-5542-bed2-b3a816cc4897', 'rolesManange', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('1ca6b12c-99b7-40b7-8430-d8cee5bc3868', 'demo1-validate', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('3f550160-08e9-4e58-b707-5c53b84fa57d', 'demo1-form', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('4d6955f1-7ca6-461b-bd4a-ff78a870fc1d', 'dictionary', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('4d6955f1-7ca6-461b-bd4c-ff78a870fc1d', 'demo1', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('4f1a5836-6af4-4c38-a625-482150f05f23', 'user', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('636c5991-4a26-4da6-bf4d-fb7ae3e8d189', 'demo', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('d3b25a9c-862c-4e28-b375-2e8f42aab959', 'system_mange', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('e81b3149-6012-4517-ba89-aeb36caaa740', 'button', 'role_system_admin');
INSERT INTO `nh_micro_ref_menu_role` VALUES ('fc990021-2c46-4121-a97c-c038dae4674b', 'editrole', 'role_system_admin');

-- ----------------------------
-- Table structure for nh_micro_ref_user_role
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_ref_user_role`;
CREATE TABLE `nh_micro_ref_user_role` (
  `id` varchar(50) NOT NULL,
  `user_id` varchar(50) default NULL COMMENT '用户id',
  `role_id` varchar(100) default NULL COMMENT '角色id',
  `remark` varchar(200) default NULL COMMENT '备注',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色映射表';

-- ----------------------------
-- Records of nh_micro_ref_user_role
-- ----------------------------
INSERT INTO `nh_micro_ref_user_role` VALUES ('655dd9ad-11c2-4eeb-8e27-6d2a3c8296ef', 'guest', 'role_test', null, null, null);
INSERT INTO `nh_micro_ref_user_role` VALUES ('d2ef55d2-e523-431b-984e-3ba6f400a46f', 'admin', 'role_system_admin', null, null, null);

-- ----------------------------
-- Table structure for nh_micro_role
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_role`;
CREATE TABLE `nh_micro_role` (
  `id` varchar(50) NOT NULL,
  `role_id` varchar(50) default NULL COMMENT '角色id',
  `role_name` varchar(100) default NULL COMMENT '角色名称',
  `role_type` varchar(100) default NULL COMMENT '角色类型',
  `remark` varchar(200) default NULL COMMENT '备注',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of nh_micro_role
-- ----------------------------
INSERT INTO `nh_micro_role` VALUES ('0021e454-812a-4bd3-9a9e-8fc1c1dd2756', 'role_system_admin', '系统管理员', 'system', null, null, null);
INSERT INTO `nh_micro_role` VALUES ('ecd1ef80-daa5-41cc-8901-1370ced42752', 'role_test', '测试', 'system', null, '2018-01-29 10:03:34', null);

-- ----------------------------
-- Table structure for nh_micro_sysmenu
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_sysmenu`;
CREATE TABLE `nh_micro_sysmenu` (
  `id` int(65) NOT NULL auto_increment COMMENT '主键id',
  `code` varchar(255) NOT NULL COMMENT '菜单编号',
  `pcode` varchar(255) NOT NULL COMMENT '菜单父编号',
  `pcodes` varchar(255) default NULL COMMENT '当前菜单的所有父菜单编号',
  `name` varchar(255) NOT NULL COMMENT '菜单名称',
  `icon` varchar(255) default NULL COMMENT '菜单图标',
  `url` varchar(255) default NULL COMMENT 'url地址',
  `num` int(65) default NULL COMMENT '菜单排序号',
  `levels` int(65) default NULL COMMENT '菜单层级',
  `ismenu` int(11) default NULL COMMENT '是否是菜单（1：是  0：不是）',
  `tips` varchar(255) default NULL COMMENT '备注',
  `status` int(65) default NULL COMMENT '菜单状态 :  1:启用   0:不启用',
  `isopen` int(11) default NULL COMMENT '是否打开:    1:打开   0:不打开',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
-- Records of nh_micro_sysmenu
-- ----------------------------
INSERT INTO `nh_micro_sysmenu` VALUES ('1', 'system_mange', 'system', '[0],[system]', '系统管理', null, '', '1', '1', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('2', 'demo', 'system', '[0],[system]', '样例管理', null, '', '2', '1', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('3', 'user', 'system_mange', '[0],[system],[system_mange]', '用户列表', null, 'micromvc/uc/userList/toPage', '1', '2', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('4', 'demo1', 'demo', '[0],[system],[demo]', '表单', null, '', '1', '2', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('5', 'demo1-form', 'demo1', '[0],[system],[demo],[demo1]', '基础表单', null, 'micropage/nh-micro-jsp/demo/form_basic.jsp', '1', '3', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('6', 'demo1-validate', 'demo1', '[0],[system],[demo],[demo1]', '表单验证', null, 'micropage/nh-micro-jsp/demo/form_validate.jsp', '2', '3', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('7', 'demo2', 'demo', '[0],[system],[demo]', '表格', null, 'micropage/nh-micro-jsp/demo/table_jqgrid.jsp', '2', '2', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('8', 'demo3', 'demo', '[0],[system],[demo]', '日历', null, 'micropage/nh-micro-jsp/demo/layerdate.jsp', '3', '2', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('9', 'demo4', 'demo', '[0],[system],[demo]', '弹窗', null, null, '4', '2', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('10', 'demo4-popup', 'demo4', '[0],[system],[demo],[demo4]', '弹窗', null, 'micropage/nh-micro-jsp/demo/sweetalert.jsp', '1', '3', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('11', 'demo4-notice', 'demo4', '[0],[system],[demo],[demo4]', '通知', null, 'micropage/nh-micro-jsp/demo/toastr_notifications.jsp', '2', '3', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('12', 'demo5', 'demo', '[0],[system],[demo]', 'UI元素', null, null, '5', '2', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('13', 'demo5-button', 'demo5', '[0],[system],[demo],[demo5]', '按钮', null, 'micropage/nh-micro-jsp/demo/buttons.jsp', '1', '3', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('14', 'demo5-load', 'demo5', '[0],[system],[demo],[demo5]', '加载', null, 'micropage/nh-micro-jsp/demo/spinners.jsp', '2', '3', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('15', 'demo5-icon', 'demo5', '[0],[system],[demo],[demo5]', '字体图标', null, 'micropage/nh-micro-jsp/demo/icons.jsp', '3', '3', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('16', 'demo6', 'demo', '[0],[system],[demo]', '文件上传', null, 'micropage/nh-micro-jsp/demo/form_file_upload.jsp', '6', '2', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('17', 'demo7', 'demo', '[0],[system],[demo]', '统计图', null, 'micropage/nh-micro-jsp/demo/graph_echarts.jsp', '7', '2', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('18', 'demo8', 'demo', '[0],[system],[demo]', '页面示例', null, 'micropage/nh-micro-jsp/demo/example_page.jsp', '8', '2', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('237', 'menu', 'system_mange', '[0],[system],[system_mange]', '菜单按钮', null, 'micropage/nh-micro-jsp/system/menus.jsp', '9', '2', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('254', 'dictionary', 'system_mange', null, '字典管理', null, 'micropage/nh-micro-jsp/dictionary-page/listDictionary.jsp', '10', '2', '1', null, '1', '1');
INSERT INTO `nh_micro_sysmenu` VALUES ('265', 'rolesManange', 'system_mange', null, '角色管理', null, 'micromvc/uc/roleList/toPage', '11', '2', '1', null, '1', '1');

-- ----------------------------
-- Table structure for nh_micro_user
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_user`;
CREATE TABLE `nh_micro_user` (
  `id` varchar(50) NOT NULL,
  `user_id` varchar(50) default NULL COMMENT '用户id',
  `user_name` varchar(100) default NULL COMMENT '用户名',
  `user_age` int(11) default NULL COMMENT '用户类型',
  `user_remark` varchar(200) default NULL COMMENT '备注',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  `user_password` varchar(255) default NULL COMMENT '密码',
  `user_type` varchar(50) default NULL,
  `user_status` int(11) default NULL,
  `user_mobile` varchar(50) default NULL,
  `user_id_num` varchar(50) default NULL,
  `user_email` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' 用户登录';

-- ----------------------------
-- Records of nh_micro_user
-- ----------------------------
INSERT INTO `nh_micro_user` VALUES ('1', 'guest', '访客', null, '1', null, null, '084e0343a0486ff05530df6c705c8bb4', null, '0', '1', '1', '1');
INSERT INTO `nh_micro_user` VALUES ('2', 'admin', 'admin', null, '', null, null, '21232f297a57a5a743894a0e4a801fc3', null, '0', null, null, null);
