package com.jun.plugin.code.common.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jun.plugin.code.common.config.GenConfig;
import com.jun.plugin.code.common.domain.GenTable;
import com.jun.plugin.code.common.service.IGenService;
import com.jun.plugin.code.common.service.IGenTableService;
@Controller
@RequestMapping("/gen")
public class GenController {
	private static final Logger logger = LoggerFactory.getLogger(GenController.class);
	
    @Autowired
    private IGenService genService;
    /**
     * http://localhost:8888/generator/gen/down/area
     * @param response
     * @param tableName
     * @throws IOException
     */
    @GetMapping("/down/{tableName}")
    public void genCode(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException
    {
        byte[] data = genService.generatorCode(tableName);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\""+tableName+".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }
    
	@Autowired
	private IGenTableService genTableService;

	/**
	 * http://192.168.199.101:8888/generator/api/gen/download/sys_area 生成代码（下载方式）
	 */
	@GetMapping("/download/{tableName}")
	public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
		logger.info(GenConfig.getPackageName());
		String packages = GenConfig.getPackageName();
		// 查询是否已经有相同名称的表数据, 如果有, 则先删除
		GenTable query = new GenTable();
		query.setTableName(tableName);
		List<GenTable> list = genTableService.selectGenTableList(query);
		if (list.size() > 0) {
			genTableService.deleteGenTableByIds(list.stream().map(GenTable::getTableId).toArray(Long[]::new));
		}

		// 查询表元数据信息
		String[] tableNames = new String[] { tableName };
		List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);

		genTableService.importGenTable(tableList, packages);
		byte[] data = genTableService.downloadCode(tableName);
		genCode(response, data);
	}

	/**
	 * 生成zip文件
	 */
	private void genCode(HttpServletResponse response, byte[] data) throws IOException {
		response.reset();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
		response.setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, response.getOutputStream());
	}
}
