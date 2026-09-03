package com.zapi.dataset.support.service;


import com.zapi.dataset.config.DataSourceConfig;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseExportService {
    /**
     * 导出数据库脚本
     * @param config 数据源配置
     * @param exportData 是否导出数据
     * @param selectedTables 选择的表(空表示全部)
     * @return 生成的SQL脚本
     */
    String exportScript(DataSourceConfig config, boolean exportData, List<String> selectedTables) throws SQLException;
}
