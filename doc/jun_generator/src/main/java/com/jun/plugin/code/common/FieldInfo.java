package com.jun.plugin.code.common;

import lombok.Data;

/**
 * field info
 */
@Data
public class FieldInfo {

	private String columnName;// 列名 name
	private String fieldComment;// 列备注 desc
	private String columnType; //// 字段类型
	private String columnSize;// 字段长度
	private String nullable;// 是否必填
	private String isPrimaryKey;// 是否是主键  id
	private String isAutoincrement;// 是否自增  YES自增，NO：非自增 identity

	private String fieldName;// 类属性名称
	private String fieldNameFirstUpper;// 首字母大写属性名字 upperName
	private String fieldType; //// 类属性类型 type
	private String fieldSimpleType; // 属性类型 simpleType
	private String fieldJavaTypeClass; // 属性类型 simpleType

}