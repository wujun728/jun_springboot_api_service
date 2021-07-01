/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : company_frame

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2019-12-03 12:05:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(64) NOT NULL COMMENT '用户id',
  `username` varchar(50) NOT NULL COMMENT '账户名称',
  `salt` varchar(20) DEFAULT NULL COMMENT '加密盐值',
  `password` varchar(200) NOT NULL COMMENT '用户密码密文',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `dept_id` varchar(64) DEFAULT NULL COMMENT '部门id',
  `real_name` varchar(60) DEFAULT NULL COMMENT '真实名称',
  `nick_name` varchar(60) DEFAULT NULL COMMENT '昵称',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱(唯一)',
  `status` tinyint(4) DEFAULT '1' COMMENT '账户状态(1.正常 2.锁定 )',
  `sex` tinyint(4) DEFAULT '1' COMMENT '性别(1.男 2.女)',
  `deleted` tinyint(4) DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  `create_id` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_id` varchar(64) DEFAULT NULL COMMENT '更新人',
  `create_where` tinyint(4) DEFAULT '1' COMMENT '创建来源(1.web 2.android 3.ios )',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1dfaafa7-fddf-46f2-b3d8-11bfe9ac7230', 'dev123', 'fa386978e2c04b7baef1', '9204993352a150ddd9febc421a5e5636', '13177777777', '4bd0b0a3-097d-4902-a1f7-641ea3b771bd', null, null, null, '1', '1', '1', null, null, '1', '2019-11-09 22:47:30', null);
INSERT INTO `sys_user` VALUES ('5bc41939-78d9-40f8-a761-b9cf35f5d9e4', 'test', '7d69eec997034bfb8c5c', '', '13878888888', 'a4f3e984-622b-4330-bcda-0ea01e44d299', null, null, null, '2', '1', '1', null, 'fcf34b56-a7a2-4719-9236-867495e74c31', '1', '2020-01-01 20:01:20', '2020-01-01 20:03:27');
INSERT INTO `sys_user` VALUES ('7f8c0e32-058e-409d-8e7c-22a9afe6a0a0', 'zhangyang', '062f33e33afe4509b24b', '920f5d75077b25912e5054c4d58e0a4c', '13899999999', '4bd0b0a3-097d-4902-a1f7-641ea3b771bd', '张杨', null, '16399@163.com', '1', '1', '1', null, '7f8c0e32-058e-409d-8e7c-22a9afe6a0a0', '1', '2019-11-09 21:23:36', '2019-11-09 22:45:36');
INSERT INTO `sys_user` VALUES ('d860412c-9a4b-404b-8b71-ae8e3f4c27b7', 't', 'a8aed440045b4e0c9c69', 'b08f907d879ea98a681df0082a9cb95e', '13899999999', '72a4f388-50f8-4019-8c67-530cd7c74e7a', null, null, null, '1', '1', '1', null, null, '1', '2019-11-19 10:34:24', null);
INSERT INTO `sys_user` VALUES ('fcf34b56-a7a2-4719-9236-867495e74c31', 'admin', '324ce32d86224b00a02b', 'ac7e435db19997a46e3b390e69cb148b', '13888888888', '72a4f388-50f8-4019-8c67-530cd7c74e7a', '迎学教育', '小霍', 'yingxue@163.com', '1', '2', '1', null, 'fcf34b56-a7a2-4719-9236-867495e74c31', '3', '2019-09-22 19:38:05', '2019-11-09 21:20:58');
