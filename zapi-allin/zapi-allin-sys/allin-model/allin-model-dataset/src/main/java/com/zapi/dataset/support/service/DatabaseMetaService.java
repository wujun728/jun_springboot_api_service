package com.zapi.dataset.support.service;




import com.zapi.dataset.config.DataSourceConfig;
import com.zapi.dataset.model.vo.FieldInfoVO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface DatabaseMetaService {

    List<String> getTableList(DataSourceConfig config) throws SQLException;


    List<HashMap<String, Object>> getTableSAndComment(DataSourceConfig config) throws SQLException;

    List<FieldInfoVO> getTableColumnList(DataSourceConfig config) throws SQLException;

    List<String> getColumnType();

    boolean updateColumnType(DataSourceConfig config) throws SQLException;

    boolean addTable(DataSourceConfig config) throws SQLException;

    boolean dropTable(DataSourceConfig config);

    /**
     * 录入业务数据，json为字段-值映射，直接插入到config指定的表
     * @param config 数据源配置（需包含表名等信息）
     * @param json 字段-值映射的json字符串
     * @return 是否成功
     */
    boolean insertBusinessData(DataSourceConfig config, String json) throws SQLException;
}
