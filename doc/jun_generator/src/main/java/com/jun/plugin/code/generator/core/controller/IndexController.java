package com.jun.plugin.code.generator.core.controller;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jun.plugin.code.generator.core.model.ClassInfo;
import com.jun.plugin.code.generator.core.util.CodeGeneratorTool;
import com.jun.plugin.code.generator.core.util.FreemarkerTool;
import com.jun.plugin.code.generator.core.util.ReturnT;
import com.jun.plugin.code.generator.model.ParamInfo;
import com.jun.plugin.code.generator.service.GeneratorService;
import com.jun.plugin.code.meta.util.Table;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Resource
	private FreemarkerTool freemarkerTool;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/codeGenerate")
	@ResponseBody
	public ReturnT<Map<String, String>> codeGenerate(String tableSql) {
		String template_path = "template_v1/crud";
		try {

			if (StringUtils.isBlank(tableSql)) {
				return new ReturnT<Map<String, String>>(ReturnT.FAIL_CODE, "表结构信息不可为空");
			}

			// parse table
			ClassInfo classInfo = CodeGeneratorTool.processTableIntoClassInfo(tableSql);
			Table table = CodeGeneratorTool.processTableIntoTable(tableSql);

			// code genarete
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("classInfo", classInfo);
			params.put("Table", table);

			// result
			Map<String, String> result = new HashMap<String, String>();
			result.put("controller_code", freemarkerTool.processString(template_path + "/controller.ftl", params));
			result.put("service_code", freemarkerTool.processString(template_path + "/service.ftl", params));
			result.put("service_impl_code", freemarkerTool.processString(template_path + "/service_impl.ftl", params));
			result.put("dao_code", freemarkerTool.processString(template_path + "/dao.ftl", params));
			result.put("mybatis_code", freemarkerTool.processString(template_path + "/mybatis.ftl", params));
			result.put("model_code", freemarkerTool.processString(template_path + "/model.ftl", params));

			// 计算,生成代码行数
			int lineNum = 0;
			for (Map.Entry<String, String> item : result.entrySet()) {
				if (item.getValue() != null) {
					lineNum += StringUtils.countMatches(item.getValue(), "\n");
				}
			}
			logger.info("生成代码行数：{}", lineNum);

			return new ReturnT<Map<String, String>>(result);
		} catch (IOException | TemplateException e) {
			logger.error(e.getMessage(), e);
			return new ReturnT<Map<String, String>>(ReturnT.FAIL_CODE, "表结构解析失败");
		}

	}

	/**
	 * http://localhost:8888/generator/down/area
	 * 
	 * @param response
	 * @param tableName
	 * @throws IOException
	 * @throws TemplateException 
	 */
	@GetMapping("/down/{tableName}")
	public void genCode(HttpServletResponse response, @PathVariable("tableName") String tableName, String tableSql)
			throws IOException, TemplateException {
		if (StringUtils.isBlank(tableSql)) {
			logger.error("表结构信息不可为空");
		}

		byte[] data = freemarkerTool.generatorCode(tableName,tableSql);
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + tableName + ".zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, response.getOutputStream());
	}

}