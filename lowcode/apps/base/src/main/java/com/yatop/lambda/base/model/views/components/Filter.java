/**
  * Copyright 2024 bejson.com 
  */
package com.yatop.lambda.base.model.views.components;
import com.yatop.lambda.base.model.views.components.Controls;

import java.util.List;

/**
 * Auto-generated: 2024-06-26 22:45:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Filter {

    private String title;
    private String mode;
    private boolean wrapWithPanel;
    private String submitText;
    private List<Controls> controls;


    private List<Object> body;

    public List<Object> getBody() {
        return body;
    }

    public void setBody(List<Object> body) {
        this.body = body;
    }

    private String className;
    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setMode(String mode) {
         this.mode = mode;
     }
     public String getMode() {
         return mode;
     }

    public void setWrapWithPanel(boolean wrapWithPanel) {
         this.wrapWithPanel = wrapWithPanel;
     }
     public boolean getWrapWithPanel() {
         return wrapWithPanel;
     }

    public void setSubmitText(String submitText) {
         this.submitText = submitText;
     }
     public String getSubmitText() {
         return submitText;
     }

    public void setControls(List<Controls> controls) {
         this.controls = controls;
     }
     public List<Controls> getControls() {
         return controls;
     }

    public void setClassName(String className) {
         this.className = className;
     }
     public String getClassName() {
         return className;
     }

}