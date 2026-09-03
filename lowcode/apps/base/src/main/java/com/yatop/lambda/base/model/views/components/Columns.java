package com.yatop.lambda.base.model.views.components;

public  class Columns {

        private String name;
        private String label;
        private String type;

        private boolean required;


        private boolean quickEdit;

        private String remark;

    public boolean isQuickEdit() {
        return quickEdit;
    }

    public void setQuickEdit(boolean quickEdit) {
        this.quickEdit = quickEdit;
    }

    public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        private boolean sortable;
        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setLabel(String label) {
            this.label = label;
        }
        public String getLabel() {
            return label;
        }

        public void setSortable(boolean sortable) {
            this.sortable = sortable;
        }
        public boolean getSortable() {
            return sortable;
        }

    }