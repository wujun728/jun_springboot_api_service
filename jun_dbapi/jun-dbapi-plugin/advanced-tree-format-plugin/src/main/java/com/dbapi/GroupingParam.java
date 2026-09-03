package com.dbapi;

import java.util.ArrayList;
import java.util.List;

public class GroupingParam {

    private String groupBy;
    private String childrenName;
    private List<String> fields = new ArrayList<>();
    private List<String> orderBy = new ArrayList<>();

    public String getGroupBy() {
        return this.groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getChildrenName() {
        return this.childrenName;
    }

    public void setChildrenName(String childrenName) {
        this.childrenName = childrenName;
    }

    public List<String> getFields() {
        return this.fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(List<String> orderBy) {
        this.orderBy = orderBy;
    }
}
