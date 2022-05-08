package com.gitee.freakchicken.dbapi.common;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName(value = "api_sql")
public class ApiSql {

    @TableId(type= IdType.AUTO)
    Integer id;

    @TableField("api_id")
    String apiId;

    @TableField("sql_text")
    String sqlText;

    @TableField(value = "transform_plugin", insertStrategy = FieldStrategy.NOT_EMPTY)
    String transformPlugin;

    @TableField(value = "transform_plugin_params")
    String transformPluginParams;

    public ApiSql(String apiId, String sql) {
        this.apiId = apiId;
        this.sqlText = sql;
    }

    public ApiSql() {
    }
}
