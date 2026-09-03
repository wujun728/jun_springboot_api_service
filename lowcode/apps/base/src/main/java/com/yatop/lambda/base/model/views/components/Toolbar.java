/**
  * Copyright 2024 bejson.com 
  */
package com.yatop.lambda.base.model.views.components;

/**
 * Auto-generated: 2024-06-26 22:45:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Toolbar {

    private String type;
    private String actionType;
    private String link;
    private String label;
    private boolean primary;

    private Dialog dialog;

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setActionType(String actionType) {
         this.actionType = actionType;
     }
     public String getActionType() {
         return actionType;
     }

    public void setLink(String link) {
         this.link = link;
     }
     public String getLink() {
         return link;
     }

    public void setLabel(String label) {
         this.label = label;
     }
     public String getLabel() {
         return label;
     }

    public void setPrimary(boolean primary) {
         this.primary = primary;
     }
     public boolean getPrimary() {
         return primary;
     }

}