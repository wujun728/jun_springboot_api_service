package com.gitee.freakchicken.entity;

/**
 * @program: dbapi-starter
 * @description:
 * @author: jiangqiang
 * @create: 2021-03-11 14:03
 **/
public class SqlNode {

    String id;
    String text;
    String type;
    String datasourceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }
}
