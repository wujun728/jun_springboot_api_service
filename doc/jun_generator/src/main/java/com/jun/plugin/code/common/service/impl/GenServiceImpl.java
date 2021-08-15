package com.jun.plugin.code.common.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jun.plugin.code.common.config.Global;
import com.jun.plugin.code.common.entity.ColumnInfo;
import com.jun.plugin.code.common.entity.TableInfo;
import com.jun.plugin.code.common.mapper.GenMapper;
import com.jun.plugin.code.common.service.IGenService;
import com.jun.plugin.code.common.util.Constants;
import com.jun.plugin.code.common.util.GenUtils;
import com.jun.plugin.code.common.util.StringUtils;
import com.jun.plugin.code.common.util.VelocityInitializer;
import com.jun.plugin.code.generator.core.model.ClassInfo;
import com.jun.plugin.code.generator.core.util.CodeGeneratorTool;
import com.jun.plugin.code.generator.core.util.FreemarkerTool;
import com.jun.plugin.code.meta.util.Table;

import freemarker.template.TemplateException;

/**
 * 代码生成 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class GenServiceImpl implements IGenService {
	private static final Logger log = LoggerFactory.getLogger(GenServiceImpl.class);

	@Autowired
	private GenMapper genMapper;

	@Resource
	private FreemarkerTool freemarkerTool;

	/**
	 * 查询ry数据库表信息
	 * 
	 * @param tableInfo 表信息
	 * @return 数据库表列表
	 */
	@Override
	public List<TableInfo> selectTableList(TableInfo tableInfo) {
		return genMapper.selectTableList(tableInfo);
	}

	/**
	 * 生成代码
	 * 
	 * @param tableName 表名称
	 * @return 数据
	 */
	@Override
	public byte[] generatorCode(String tableName) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		// 查询表信息
		TableInfo table = genMapper.selectTableByName(tableName);
		// 查询列信息
		List<ColumnInfo> columns = genMapper.selectTableColumnsByName(tableName);
		// 生成代码
		generatorCode(table, columns, zip);
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	/**
	 * 批量生成代码
	 * 
	 * @param tableNames 表数组
	 * @return 数据
	 */
	@Override
	public byte[] generatorCode(String[] tableNames) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		for (String tableName : tableNames) {
			// 查询表信息
			TableInfo table = genMapper.selectTableByName(tableName);
			// 查询列信息
			List<ColumnInfo> columns = genMapper.selectTableColumnsByName(tableName);
			// 生成代码
			generatorCode(table, columns, zip);
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	/**
	 * 生成代码
	 * 
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void generatorCodeFtl(TableInfo table, List<ColumnInfo> columns, ZipOutputStream zip)
			throws IOException, TemplateException {
		// 表名转换成Java属性名
		String className = GenUtils.tableToJava(table.getTableName());
		table.setClassName(className);
		table.setClassname(StringUtils.uncapitalize(className));
		// 列信息
		table.setColumns(GenUtils.transColums(columns));
		// 设置主键
		table.setPrimaryKey(table.getColumnsLast());
		
		String packageName = Global.getPackageName();
		String moduleName = GenUtils.getModuleName(packageName);
		
		// code genarete
		String template_path = "template_v1/crud/";
		ClassInfo classInfo = GenUtils.getClassInfo(table);
		Table tab = GenUtils.getTableInfo(table);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("classInfo", classInfo);
		params.put("Table", tab);
		params.put("TableInfo", table);

		// 获取模板列表
		List<String> templates = GenUtils.getTemplatesFtl();
		for (String template : templates) {
			StringWriter sw = new StringWriter();
			sw.append(freemarkerTool.processString(template, params));
			String filename = classInfo.getClassName() + template.replace(".ftl", ".java");
			try {
				// 添加到zip
				zip.putNextEntry(new ZipEntry(filename));
				IOUtils.write(sw.toString(), zip, Constants.UTF8);
				IOUtils.closeQuietly(sw);
				zip.closeEntry();
			} catch (IOException e) {
				log.error("渲染模板失败，表名：" + table.getTableName(), e);
			}
		}
	}

	/**
	 * 生成代码
	 */
	public void generatorCode(TableInfo table, List<ColumnInfo> columns, ZipOutputStream zip) {
		// 表名转换成Java属性名
		String className = GenUtils.tableToJava(table.getTableName());
		table.setClassName(className);
		table.setClassname(StringUtils.uncapitalize(className));
		// 列信息
		table.setColumns(GenUtils.transColums(columns));
		// 设置主键
		table.setPrimaryKey(table.getColumnsLast());

		VelocityInitializer.initVelocity();

		String packageName = Global.getPackageName();
		String moduleName = GenUtils.getModuleName(packageName);

		VelocityContext context = GenUtils.getVelocityContext(table);

		// 获取模板列表
		List<String> templates = GenUtils.getTemplates();
		for (String template : templates) {
			// 渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, Constants.UTF8);
			tpl.merge(context, sw);
			try {
				// 添加到zip
				zip.putNextEntry(new ZipEntry(GenUtils.getFileName(template, table, moduleName)));
				IOUtils.write(sw.toString(), zip, Constants.UTF8);
				IOUtils.closeQuietly(sw);
				zip.closeEntry();
			} catch (IOException e) {
				log.error("渲染模板失败，表名：" + table.getTableName(), e);
			}
		}
	}
}
