package com.jun.plugin.code.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RegExUtils;
import org.apache.velocity.VelocityContext;

import com.google.common.collect.Lists;
import com.jun.plugin.code.common.config.GenConfig;
import com.jun.plugin.code.common.config.Global;
import com.jun.plugin.code.common.domain.GenTable;
import com.jun.plugin.code.common.domain.GenTableColumn;
import com.jun.plugin.code.common.entity.ColumnInfo;
import com.jun.plugin.code.common.entity.TableInfo;
import com.jun.plugin.code.generator.core.model.ClassInfo;
import com.jun.plugin.code.generator.core.model.FieldInfo;
import com.jun.plugin.code.meta.util.Column;
import com.jun.plugin.code.meta.util.Table;

/**
 * 代码生成器 工具类
 * 
 * @author Wujun
 */
public class GenUtils {
	/** 项目空间路径 */
	private static final String PROJECT_PATH = "main/java/com/test";

	/** mybatis空间路径 */
	private static final String MYBATIS_PATH = "main/resources/mybatis";

	/** html空间路径 */
	private static final String TEMPLATES_PATH = "main/resources/templates";

	/** 类型转换 */
	public static Map<String, String> javaTypeMap = new HashMap<String, String>();

	/**
	 * 设置列信息
	 */
	public static List<ColumnInfo> transColums(List<ColumnInfo> columns) {
		// 列信息
		List<ColumnInfo> columsList = new ArrayList<>();
		for (ColumnInfo column : columns) {
			// 列名转换成Java属性名
			String attrName = StringUtils.convertToCamelCase(column.getColumnName());
			column.setAttrName(attrName);
			column.setAttrname(StringUtils.uncapitalize(attrName));

			// 列的数据类型，转换成Java类型
			String attrType = javaTypeMap.get(column.getDataType());
			column.setAttrType(attrType);

			columsList.add(column);
		}
		return columsList;
	}

	/**
	 * 获取模板信息
	 * 
	 * @return 模板列表
	 */
	public static VelocityContext getVelocityContext(TableInfo table) {
		// java对象数据传递到模板文件vm
		VelocityContext velocityContext = new VelocityContext();
		String packageName = Global.getPackageName();
		velocityContext.put("tableName", table.getTableName());
		velocityContext.put("tableComment", replaceKeyword(table.getTableComment()));
		velocityContext.put("primaryKey", table.getPrimaryKey());
		velocityContext.put("className", table.getClassName());
		velocityContext.put("classname", table.getClassname());
		velocityContext.put("moduleName", GenUtils.getModuleName(packageName));
		velocityContext.put("columns", table.getColumns());
		velocityContext.put("package", packageName);
		velocityContext.put("author", Global.getAuthor());
		velocityContext.put("datetime", DateUtils.getDate());
		return velocityContext;
	}

	/**
	 * 获取模板信息
	 * 
	 * @return 模板列表
	 */
	public static List<String> getTemplates() {
		List<String> templates = new ArrayList<String>();
		templates.add("templates/vm/java/domain.java.vm");
		templates.add("templates/vm/java/Mapper.java.vm");
		templates.add("templates/vm/java/Service.java.vm");
		templates.add("templates/vm/java/ServiceImpl.java.vm");
		templates.add("templates/vm/java/Controller.java.vm");
		templates.add("templates/vm/xml/Mapper.xml.vm");
		templates.add("templates/vm/html/list.html.vm");
		templates.add("templates/vm/html/add.html.vm");
		templates.add("templates/vm/html/edit.html.vm");
		templates.add("templates/vm/sql/sql.vm");
		return templates;
	}

	/**
	 * 获取模板信息
	 * 
	 * @return 模板列表
	 */
	public static List<String> getTemplatesFtl() {

		List<String> templates = Lists.newArrayList("templates/template_v1/crud/controller.ftl",
				"templates/template_v1/crud/dao.ftl", "templates/template_v1/crud/model.ftl",
				"templates/template_v1/crud/mybatis.ftl", "templates/template_v1/crud/service.ftl",
				"templates/template_v1/crud/service_impl.ftl");
		return templates;
	}

	public static ClassInfo getClassInfo(TableInfo table) {
		ClassInfo c = new ClassInfo();
		c.setClassName(table.getClassName());
		c.setClassComment(table.getTableComment());
		c.setTableName(table.getTableName());
		List<FieldInfo> fieldList = Lists.newArrayList();
		List<ColumnInfo> columnList = table.getColumns();
		columnList.forEach(item -> {
			FieldInfo f = new FieldInfo();
			f.setColumnName(item.getColumnName());
			f.setFieldClass(item.getAttrType());// 获取字段类类型
			f.setFieldComment(item.getColumnComment());
			f.setFieldLength(null);// 获取字段长度
			f.setFieldName(item.getAttrName());
			fieldList.add(f);
		});
		c.setFieldList(fieldList);
		return c;
	}

	public static Table getTableInfo(TableInfo table) {
		Table t = new Table();
		t.setClassName(table.getClassName());
		t.setClassComment(table.getTableComment());
		t.setTableName(table.getTableName());
		t.setClassNameLower(table.getClassname());
		t.setRemarks(table.getRemark());
		t.setSqlName(table.getTableName());
		List<Column> column = Lists.newArrayList();
		List<ColumnInfo> columnList = table.getColumns();
		columnList.forEach(item -> {
			Column c = new Column();
			c.setColumn(item.getColumnName());
			c.setColumnName(item.getColumnName());
			c.setDesc(item.getColumnComment());
			c.setFieldClass(item.getAttrType());
			c.setFieldComment(item.getColumnComment());
			c.setFieldName(item.getAttrName());
			c.setId(false);
			c.setIdentity(null);
			c.setLength(null);
			c.setName(item.getAttrname());
			c.setNullable(null);
			c.setSimpleType(null);
			c.setType(item.getAttrType());
			c.setUpperName(item.getAttrName());
			column.add(c);
		});
		t.setColumnList(column);
		return t;

	}

	/**
	 * 表名转换成Java类名
	 */
	public static String tableToJava(String tableName) {
		if (Constants.AUTO_REOMVE_PRE.equals(Global.getAutoRemovePre())) {
			tableName = tableName.substring(tableName.indexOf("_") + 1);
		}
		if (StringUtils.isNotEmpty(Global.getTablePrefix())) {
			tableName = tableName.replace(Global.getTablePrefix(), "");
		}
		return StringUtils.convertToCamelCase(tableName);
	}

	/**
	 * 获取文件名
	 */
	public static String getFileName(String template, TableInfo table, String moduleName) {
		// 小写类名
		String classname = table.getClassname();
		// 大写类名
		String className = table.getClassName();
		String javaPath = PROJECT_PATH + "/" + moduleName + "/";
		String mybatisPath = MYBATIS_PATH + "/" + moduleName + "/" + className;
		String htmlPath = TEMPLATES_PATH + "/" + moduleName + "/" + classname;

		if (template.contains("domain.java.vm")) {
			return javaPath + "domain" + "/" + className + ".java";
		}

		if (template.contains("Mapper.java.vm")) {
			return javaPath + "mapper" + "/" + className + "Mapper.java";
		}

		if (template.contains("Service.java.vm")) {
			return javaPath + "service" + "/" + "I" + className + "Service.java";
		}

		if (template.contains("ServiceImpl.java.vm")) {
			return javaPath + "service" + "/" + className + "ServiceImpl.java";
		}

		if (template.contains("Controller.java.vm")) {
			return javaPath + "controller" + "/" + className + "Controller.java";
		}

		if (template.contains("Mapper.xml.vm")) {
			return mybatisPath + "Mapper.xml";
		}

		if (template.contains("list.html.vm")) {
			return htmlPath + "/" + classname + ".html";
		}
		if (template.contains("add.html.vm")) {
			return htmlPath + "/" + "add.html";
		}
		if (template.contains("edit.html.vm")) {
			return htmlPath + "/" + "edit.html";
		}
		if (template.contains("sql.vm")) {
			return classname + "Menu.sql";
		}
		return null;
	}

	/**
	 * 获取模块名
	 * 
	 * @param packageName 包名
	 * @return 模块名
	 */
	public static String getModuleName(String packageName) {
		int lastIndex = packageName.lastIndexOf(".");
		int nameLength = packageName.length();
		String moduleName = StringUtils.substring(packageName, lastIndex + 1, nameLength);
		return moduleName;
	}

	public static String replaceKeyword(String keyword) {
		String keyName = keyword.replaceAll("(?:表|信息)", "");
		return keyName;
	}

	static {
		javaTypeMap.put("tinyint", "Integer");
		javaTypeMap.put("smallint", "Integer");
		javaTypeMap.put("mediumint", "Integer");
		javaTypeMap.put("int", "Integer");
		javaTypeMap.put("integer", "integer");
		javaTypeMap.put("bigint", "Long");
		javaTypeMap.put("float", "Float");
		javaTypeMap.put("double", "Double");
		javaTypeMap.put("decimal", "BigDecimal");
		javaTypeMap.put("bit", "Boolean");
		javaTypeMap.put("char", "String");
		javaTypeMap.put("varchar", "String");
		javaTypeMap.put("tinytext", "String");
		javaTypeMap.put("text", "String");
		javaTypeMap.put("mediumtext", "String");
		javaTypeMap.put("longtext", "String");
		javaTypeMap.put("time", "Date");
		javaTypeMap.put("date", "Date");
		javaTypeMap.put("datetime", "Date");
		javaTypeMap.put("timestamp", "Date");
	}

	public static void main(String[] args) {
		System.out.println(StringUtils.convertToCamelCase("user_name"));
		System.out.println(replaceKeyword("岗位信息表"));
		System.out.println(getModuleName("com.ruoyi.system"));
	}

	/**
	 * 初始化表信息
	 */
	public static void initTable(GenTable genTable, String operName, String packages) {
		genTable.setClassName(convertClassName(genTable.getTableName()));
		genTable.setPackageName(packages);
		genTable.setModuleName(getModuleName(packages));
		genTable.setBusinessName(getBusinessName(genTable.getTableName()));
		genTable.setFunctionName(replaceText(genTable.getTableComment()));
		genTable.setFunctionAuthor(GenConfig.getAuthor());
		genTable.setCreateBy(operName);
	}

	/**
	 * 初始化列属性字段
	 */
	public static void initColumnField(GenTableColumn column, GenTable table) {
		String dataType = getDbType(column.getColumnType());
		String columnName = column.getColumnName();
		column.setTableId(table.getTableId());
		column.setCreateBy(table.getCreateBy());
		// 设置java字段名
		column.setJavaField(StringUtils.toCamelCase(columnName));

		if (arraysContains(GenConstants.COLUMNTYPE_STR, dataType)) {
			column.setJavaType(GenConstants.TYPE_STRING);
			// 字符串长度超过500设置为文本域
			Integer columnLength = getColumnLength(column.getColumnType());
			String htmlType = columnLength >= 500 ? GenConstants.HTML_TEXTAREA : GenConstants.HTML_INPUT;
			column.setHtmlType(htmlType);
		} else if (arraysContains(GenConstants.COLUMNTYPE_TIME, dataType)) {
			column.setJavaType(GenConstants.TYPE_DATE);
			column.setHtmlType(GenConstants.HTML_DATETIME);
		} else if (arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType)) {
			column.setHtmlType(GenConstants.HTML_INPUT);

			// 如果是浮点型 统一用BigDecimal
			String[] str = StringUtils.split(StringUtils.substringBetween(column.getColumnType(), "(", ")"), ",");
			if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
				column.setJavaType(GenConstants.TYPE_BIGDECIMAL);
			}
			// 如果是整形
			else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10) {
				column.setJavaType(GenConstants.TYPE_INTEGER);
			}
			// 长整形
			else {
				column.setJavaType(GenConstants.TYPE_LONG);
			}
		}

		// 插入字段（默认所有字段都需要插入）
		column.setIsInsert(GenConstants.REQUIRE);

		// 编辑字段
		if (!arraysContains(GenConstants.COLUMNNAME_NOT_EDIT, columnName) && !column.isPk()) {
			column.setIsEdit(GenConstants.REQUIRE);
		}
		// 列表字段
		if (!arraysContains(GenConstants.COLUMNNAME_NOT_LIST, columnName) && !column.isPk()) {
			column.setIsList(GenConstants.REQUIRE);
		}
		// 查询字段
		if (!arraysContains(GenConstants.COLUMNNAME_NOT_QUERY, columnName) && !column.isPk()) {
			column.setIsQuery(GenConstants.REQUIRE);
		}

		// 查询字段类型
		if (StringUtils.endsWithIgnoreCase(columnName, "name")) {
			column.setQueryType(GenConstants.QUERY_LIKE);
		}
		// 状态字段设置单选框
		if (StringUtils.endsWithIgnoreCase(columnName, "status")) {
			column.setHtmlType(GenConstants.HTML_RADIO);
		}
		// 类型&性别字段设置下拉框
		else if (StringUtils.endsWithIgnoreCase(columnName, "type")
				|| StringUtils.endsWithIgnoreCase(columnName, "sex")) {
			column.setHtmlType(GenConstants.HTML_SELECT);
		}
	}

	/**
	 * 校验数组是否包含指定值
	 * 
	 * @param arr         数组
	 * @param targetValue 值
	 * @return 是否包含
	 */
	public static boolean arraysContains(String[] arr, String targetValue) {
		return Arrays.asList(arr).contains(targetValue);
	}

	/**
	 * 获取业务名
	 * 
	 * @param tableName 表名
	 * @return 业务名
	 */
	public static String getBusinessName(String tableName) {
		int lastIndex = tableName.lastIndexOf("_");
		int nameLength = tableName.length();
		String businessName = StringUtils.substring(tableName, lastIndex + 1, nameLength);
		return businessName;
	}

	/**
	 * 表名转换成Java类名
	 * 
	 * @param tableName 表名称
	 * @return 类名
	 */
	public static String convertClassName(String tableName) {
		boolean autoRemovePre = Boolean.valueOf(GenConfig.getAutoRemovePre());
		String tablePrefix = GenConfig.getTablePrefix();
		if (autoRemovePre && StringUtils.isNotEmpty(tablePrefix)) {
			String[] searchList = StringUtils.split(tablePrefix, ",");
			tableName = replaceFirst(tableName, searchList);
		}
		return StringUtils.convertToCamelCase(tableName);
	}

	/**
	 * 批量替换前缀
	 * 
	 * @param replacementm 替换值
	 * @param searchList   替换列表
	 * @return
	 */
	public static String replaceFirst(String replacementm, String[] searchList) {
		String text = replacementm;
		for (String searchString : searchList) {
			if (replacementm.startsWith(searchString)) {
				text = replacementm.replaceFirst(searchString, "");
				break;
			}
		}
		return text;
	}

	/**
	 * 关键字替换
	 * 
	 * @param text 需要被替换的名字
	 * @return 替换后的名字
	 */
	public static String replaceText(String text) {
		return RegExUtils.replaceAll(text, "(?:表|若依)", "");
	}

	/**
	 * 获取数据库类型字段
	 * 
	 * @param columnType 列类型
	 * @return 截取后的列类型
	 */
	public static String getDbType(String columnType) {
		if (StringUtils.indexOf(columnType, "(") > 0) {
			return StringUtils.substringBefore(columnType, "(");
		} else {
			return columnType;
		}
	}

	/**
	 * 获取字段长度
	 * 
	 * @param columnType 列类型
	 * @return 截取后的列类型
	 */
	public static Integer getColumnLength(String columnType) {
		if (StringUtils.indexOf(columnType, "(") > 0) {
			String length = StringUtils.substringBetween(columnType, "(", ")");
			return Integer.valueOf(length);
		} else {
			return 0;
		}
	}
}
