package com.chen.generator;

import com.chen.generator.model.DatasourceProperties;
import com.chen.generator.model.ReadConfiguration;
import com.chen.generator.service.ExportService;
import com.chen.generator.service.SqlReader;

/**
 * @Author: Chenzedeng
 * @Email: chenzedeng@geexfinance.com
 * @Date: 2021/4/20 16:43
 * @description:
 * @version: 1.0
 */
public class Main {

    public static void main(String[] args) {
        ReadConfiguration readConfiguration = new ReadConfiguration();
        DatasourceProperties datasourceProperties = readConfiguration.getDatasourceProperties();

        System.out.println("-----开始解析-----");
        SqlReader sqlReader = new SqlReader(datasourceProperties);
        ExportService exportService = new ExportService();
        exportService.exportFile(datasourceProperties.getExportPath(), datasourceProperties.getDatabaseName(), sqlReader.getExcelViewList());
    }
}
