package com.yuyaogc.lowcode.engine.entity;

import java.io.Serializable;


public class Validate implements Serializable {
    private EntityField field;
    private String id;
    private long max;
    private long min;
    private boolean empty;
    private String message;
    private String regexp;

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public EntityField getField() {
        return field;
    }

    public void setField(EntityField field) {
        this.field = field;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
