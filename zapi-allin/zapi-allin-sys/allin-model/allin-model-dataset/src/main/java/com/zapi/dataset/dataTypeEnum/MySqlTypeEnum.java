package com.zapi.dataset.dataTypeEnum;

import lombok.Getter;

@Getter
public enum MySqlTypeEnum {
    // 枚举值列表，对应 MySQL 数据库的类型名称
    INT("int"),
    VARCHAR("varchar"),
    LONGTEXT("longtext"),
    CHAR("char"),
    TEXT("text"),
    DATE("date"),
    DATETIME("datetime"),
    TIMESTAMP("timestamp"),
    FLOAT("float"),
    DOUBLE("double"),
    BOOLEAN("tinyint"),
    BLOB("blob"),
    ENUM("enum"),
    SET("set");

    // 枚举值对应的数据库类型名称
    private final String typeName;

    // 构造函数
    MySqlTypeEnum(String typeName) {
        this.typeName = typeName;
    }

    // 获取枚举值对应的数据库类型名称
    public String getTypeName() {
        return typeName;
    }

    // 根据数据库类型名称获取对应的枚举值
    public static MySqlTypeEnum matchingCode(String typeName) {
        // 遍历枚举值
        for (MySqlTypeEnum type : MySqlTypeEnum.values()) {
            // 比较类型名称（忽略大小写）
            if (type.typeName.equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        // 如果没有匹配的枚举值，可以抛出异常或返回 null
        throw new IllegalArgumentException("Unknown MySQL type: " + typeName);
    }
}
