/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50547
Source Host           : localhost:3306
Source Database       : app_blog

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2016-12-30 11:36:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `userName` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `userDesc` varchar(300) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('09c63f873a9e472ca464accb61cd5e51', 'test', 'DC483E80A7A0BD9EF71D8CF973673924', '2016-12-12 15:50:39', 'aaaaa');
INSERT INTO `sys_user` VALUES ('4754f010ef344c59b728ea60809ab926', 'e100000', '1973EBD114AAB8BD85457E037BBBFA62', '2016-12-12 13:43:59', 'aa44515112121');
INSERT INTO `sys_user` VALUES ('549d321508db446e9bcaa477835fe5f1', 'admin', 'E10ADC3949BA59ABBE56E057F20F883E', '2016-12-14 14:35:08', '所有权限');
INSERT INTO `sys_user` VALUES ('629ba7eb1d8944d2873ecfc6896288e7', 'zhangsan', '25F9E794323B453885F5181F1B624D0B', '2016-12-12 11:49:21', '张三负责系统的委会和开发工作。');

-- ----------------------------
-- Table structure for `tb_blog`
-- ----------------------------
DROP TABLE IF EXISTS `tb_blog`;
CREATE TABLE `tb_blog` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `title` varchar(200) DEFAULT NULL COMMENT '标题',
  `content` varchar(500) DEFAULT NULL COMMENT '内容',
  `userId` varchar(50) DEFAULT NULL COMMENT '作者',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_blog
-- ----------------------------
INSERT INTO `tb_blog` VALUES ('436bfa2df0fe4b65b126dec65b3efb34', '测试', '测试内容', '09c63f873a9e472ca464accb61cd5e51', '2016-12-30 11:16:31');
INSERT INTO `tb_blog` VALUES ('52d47feaf7404c818255e5ae021eacab', '测试', '测试内容', '09c63f873a9e472ca464accb61cd5e51', '2016-12-30 11:16:18');
INSERT INTO `tb_blog` VALUES ('7177a02736ed4985b4fc74a356a7b96e', '测试', '测试内容', '09c63f873a9e472ca464accb61cd5e51', '2016-12-30 11:16:30');
INSERT INTO `tb_blog` VALUES ('72142bfdfbb84bd793e9dd8e961f3518', '测试', '测试内容', '09c63f873a9e472ca464accb61cd5e51', '2016-12-30 11:16:21');
