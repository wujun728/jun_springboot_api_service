package com.yuyaogc.lowcode.engine.plugin.activerecord;

public class Column {
    String column;
    String type;
    Integer length;
    boolean nullable;

    public Column(String column, String type, Integer length, boolean nullable) {
        this.column = column;
        this.type = type;
        this.length = length;
        this.nullable = nullable;
    }

    public String getColumn() {
        return column;
    }

    public String getType() {
        return type;
    }

    public Integer getLength() {
        return length;
    }

    public boolean getNullable() {
        return nullable;
    }
}
