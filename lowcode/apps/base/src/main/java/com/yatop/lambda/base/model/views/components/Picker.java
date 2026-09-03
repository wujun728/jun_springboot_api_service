package com.yatop.lambda.base.model.views.components;

import java.util.List;
import java.util.Map;

public class Picker {

    private String type;
    private String name;

    private boolean joinValues;

    private String valueField;

    private String  labelField;

    private String label;

    private boolean embed;

    private String source;

    private String size;

    private String value;

    private boolean multiple;

    private PickerSchema pickerSchema;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isJoinValues() {
        return joinValues;
    }

    public void setJoinValues(boolean joinValues) {
        this.joinValues = joinValues;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setLabelField(String labelField) {
        this.labelField = labelField;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isEmbed() {
        return embed;
    }

    public void setEmbed(boolean embed) {
        this.embed = embed;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public PickerSchema getPickerSchema() {
        return pickerSchema;
    }

    public void setPickerSchema(PickerSchema pickerSchema) {
        this.pickerSchema = pickerSchema;
    }




   public static class  PickerSchema {
        private String mode;

        private String name;

        private String quickSaveApi;

        private String  quickSaveItemApi;

        private boolean draggable;

        private HeaderToolbar headerToolbar;

        private List<Object> footerToolbar;


       private List<Object> columns;

       public List<Object> getColumns() {
           return columns;
       }

       public void setColumns(List<Object> columns) {
           this.columns = columns;
       }

       public List<Object> getFooterToolbar() {
           return footerToolbar;
       }

       public void setFooterToolbar(List<Object> footerToolbar) {
           this.footerToolbar = footerToolbar;
       }

       public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQuickSaveApi() {
            return quickSaveApi;
        }

        public void setQuickSaveApi(String quickSaveApi) {
            this.quickSaveApi = quickSaveApi;
        }

        public String getQuickSaveItemApi() {
            return quickSaveItemApi;
        }

        public void setQuickSaveItemApi(String quickSaveItemApi) {
            this.quickSaveItemApi = quickSaveItemApi;
        }

        public boolean isDraggable() {
            return draggable;
        }

        public void setDraggable(boolean draggable) {
            this.draggable = draggable;
        }

        public HeaderToolbar getHeaderToolbar() {
            return headerToolbar;
        }

        public void setHeaderToolbar(HeaderToolbar headerToolbar) {
            this.headerToolbar = headerToolbar;
        }
    }





   public static class  HeaderToolbar {
        private boolean wrapWithPanel;

        private String type;

        private String className;

        private String target;

        private String mode;

        private List<Object> body;

        public boolean isWrapWithPanel() {
            return wrapWithPanel;
        }

        public void setWrapWithPanel(boolean wrapWithPanel) {
            this.wrapWithPanel = wrapWithPanel;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public List<Object> getBody() {
            return body;
        }

        public void setBody(List<Object> body) {
            this.body = body;
        }
    }
}





