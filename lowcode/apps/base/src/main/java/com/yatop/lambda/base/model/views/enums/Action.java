package com.yatop.lambda.base.model.views.enums;

public enum Action {
    CREATE("input-text","创建"),
    DELETE("",""),
    UPDATE("input-text","修改"),
    READ("static", "查看");

    Action(String type, String title){
        this.type = type;
        this.title = title;
    }

    private String title;
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
