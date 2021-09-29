--客户信息
CREATE TABLE `NewTable` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`cusname`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户名称' ,
`custype`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '客户类型' ,
`cusdesc`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户描述' ,
`deleted`  tinyint(4) NULL DEFAULT '' COMMENT '是否删除(1未删除；0已删除)' ,
`create_id`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建人' ,
`update_id`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新人' ,
`create_where`  tinyint(4) NULL DEFAULT '' COMMENT '创建来源(1.web 2.android 3.ios )' ,
`create_time`  datetime NULL DEFAULT '' COMMENT '创建时间' ,
`update_time`  datetime NULL DEFAULT '' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='客户信息'
AUTO_INCREMENT=11
ROW_FORMAT=DYNAMIC
;

