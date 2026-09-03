package com.zapi.dataset.config;

import com.zapi.dataset.model.vo.FieldInfoVO;
import lombok.Data;

import java.util.List;

@Data
public class DataSourceConfig {
    private String dbType; // mysql, dm(达梦)等
    private String url;
    private String username;
    private String password;
    private String schema;
    private String tableName;
    private String tableComment;
    private List<FieldInfoVO> fields; // 字段信息
    private Integer pageNum;
    private Integer pageSize;
    // getters and setters
}