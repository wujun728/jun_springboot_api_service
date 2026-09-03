/**
  * Copyright 2024 bejson.com 
  */
package com.yatop.lambda.base.model.views.components;
import java.util.List;

/**
 * Auto-generated: 2024-06-26 22:45:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Page {
    private String type;
    private String title;
    private String remark;

    private String subTitle;

    private List<Object> toolbar;
    private List<Object> body;
    private List<Object> aside;

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setRemark(String remark) {
         this.remark = remark;
     }
     public String getRemark() {
         return remark;
     }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<Object> getToolbar() {
        return toolbar;
    }

    public void setToolbar(List<Object> toolbar) {
        this.toolbar = toolbar;
    }

    public List<Object> getAside() {
        return aside;
    }

    public void setAside(List<Object> aside) {
        this.aside = aside;
    }

    public List<Object> getBody() {
        return body;
    }

    public void setBody(List<Object> body) {
        this.body = body;
    }
}