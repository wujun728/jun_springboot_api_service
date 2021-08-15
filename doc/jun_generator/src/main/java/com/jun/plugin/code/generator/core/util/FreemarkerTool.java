package com.jun.plugin.code.generator.core.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jun.plugin.code.common.config.Global;
import com.jun.plugin.code.common.entity.ColumnInfo;
import com.jun.plugin.code.common.entity.TableInfo;
import com.jun.plugin.code.common.util.Constants;
import com.jun.plugin.code.common.util.GenUtils;
import com.jun.plugin.code.common.util.StringUtils;
import com.jun.plugin.code.generator.core.model.ClassInfo;
import com.jun.plugin.code.meta.util.Table;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

@Component
public class FreemarkerTool {
    private static final Logger logger = LoggerFactory.getLogger(CodeGeneratorTool.class);

    @Autowired
    private Configuration configuration;
    
    @Resource
	private FreemarkerTool freemarkerTool;

    /**
     * process Template Into String
     *
     * @param template
     * @param model
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String processTemplateIntoString(Template template, Object model)
            throws IOException, TemplateException {

        StringWriter result = new StringWriter();
        template.process(model, result);
        return result.toString();
    }

    /**
     * process String
     *
     * @param templateName
     * @param params
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String processString(String templateName, Map<String, Object> params)
            throws IOException, TemplateException {

        Template template = configuration.getTemplate(templateName);
        String htmlText = processTemplateIntoString(template, params);
        return htmlText;
    }
    
	public byte[] generatorCode(String tableName,String tableSql) throws IOException, TemplateException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		// 查询表信息
//		TableInfo table = genMapper.selectTableByName(tableName);
		// 查询列信息
//		List<ColumnInfo> columns = genMapper.selectTableColumnsByName(tableName);
		
		// parse table
		ClassInfo classInfo = CodeGeneratorTool.processTableIntoClassInfo(tableSql);
		Table table = CodeGeneratorTool.processTableIntoTable(tableSql);
	
		// code genarete
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("classInfo", classInfo);
		params.put("Table", table);
		
		// 生成代码
		generatorCodeFtl(params, zip);
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}
	
	/**
	 * 生成代码
	 * 
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void generatorCodeFtl(Map<String, Object> params, ZipOutputStream zip) throws IOException, TemplateException {
		// 表名转换成Java属性名
//		String className = GenUtils.tableToJava(table.getTableName());
//		table.setClassName(className);
//		table.setClassname(StringUtils.uncapitalize(className));
//		// 列信息
//		table.setColumns(GenUtils.transColums(columns));
//		// 设置主键
//		table.setPrimaryKey(table.getColumnsLast());
//		String packageName = Global.getPackageName();
//		String moduleName = GenUtils.getModuleName(packageName);
		
		// code genarete
		String template_path = "template_v1/crud/";
//		ClassInfo classInfo = GenUtils.getClassInfo(table);
//		Table tab = GenUtils.getTableInfo(table);
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("classInfo", classInfo);
//		params.put("Table", tab);
//		params.put("TableInfo", table);
		
		// result
		Map<String, String> result = new HashMap<String, String>();
		result.put("controller_code", freemarkerTool.processString(template_path + "/controller.ftl", params));
		result.put("service_code", freemarkerTool.processString(template_path + "/service.ftl", params));
		result.put("service_impl_code", freemarkerTool.processString(template_path + "/service_impl.ftl", params));
		result.put("dao_code", freemarkerTool.processString(template_path + "/dao.ftl", params));
		result.put("mybatis_code", freemarkerTool.processString(template_path + "/mybatis.ftl", params));
		result.put("model_code", freemarkerTool.processString(template_path + "/model.ftl", params));
		
		ClassInfo classInfo = (ClassInfo) params.get("classInfo");

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
				logger.error("渲染模板失败，表名：" + classInfo.getTableName(), e);
			}
		}
	}


}
