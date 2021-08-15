package com.jun.plugin.code.common;

import java.util.List;

import com.jun.plugin.code.meta.util.Column;

import lombok.Data;

@Data
public class ClassInfo {

private String tableName;//表名
private String className;//类名小写
private String ClassName;//类名大写
private String classNameLower;//类名小写
private String sqlName;// sqlName
private String tableRemarks;// 备注remarks
private List<FieldInfo> fieldList;
private List<Column> columnList;

}
