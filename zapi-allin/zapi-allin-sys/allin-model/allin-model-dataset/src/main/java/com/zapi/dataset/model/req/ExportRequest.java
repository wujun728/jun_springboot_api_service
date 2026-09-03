package com.zapi.dataset.model.req;

import com.zapi.dataset.config.DataSourceConfig;
import lombok.Data;

import java.util.List;

@Data
public class ExportRequest {

    private DataSourceConfig dataSourceConfig;
    private boolean exportData;
    private List<String> selectedTables;
    private String dbType;

    private String targetDbType;
}
