package com.zapi.dataset.dataTypeEnum;

import lombok.Getter;

@Getter
public enum DmTypeEnum {
    INT("INT"),
    BIGINT("BIGINT"),
    TINYINT("TINYINT"),
    VARCHAR("VARCHAR"),
    CHAR("CHAR"),
    TEXT("TEXT"),
    DATE("DATE"),
    DATETIME("DATETIME"),
    TIMESTAMP("TIMESTAMP"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    BOOLEAN("BOOLEAN"),
    BLOB("BLOB"),
    ENUM("ENUM"),
    SET("SET"),
    DECIMAL("DECIMAL"),
    NUMERIC("NUMERIC"),
    BIT("BIT"),
    VARBIT("VARBIT");

    private final String typeName;

    DmTypeEnum(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static DmTypeEnum matchingCode(String typeName) {
        for (DmTypeEnum type : DmTypeEnum.values()) {
            if (type.typeName.equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown DmDB type: " + typeName);
    }


}
