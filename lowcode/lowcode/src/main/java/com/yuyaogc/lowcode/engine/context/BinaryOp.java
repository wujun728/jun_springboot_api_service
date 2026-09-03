package com.yuyaogc.lowcode.engine.context;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * 二元查询操作
 *
 *
 */
public class BinaryOp {

    @JsonProperty
    Object field;
    @JsonProperty
    String op;
    @JsonProperty
    Object value;

    public BinaryOp(Object field, String op, Object value) {
        this.field = field;
        this.op = op;
        this.value = value;
    }

    public Object getField() {
        return field;
    }

    public String getOp() {
        return op;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BinaryOp)) {
            return false;
        }
        BinaryOp other = (BinaryOp) obj;
        return Objects.equals(field, other.field) && Objects.equals(op, other.op) && Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", field, op, value);
    }
}
