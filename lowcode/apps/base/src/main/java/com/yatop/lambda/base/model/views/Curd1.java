/**
  * Copyright 2024 bejson.com 
  */
package com.yatop.lambda.base.model.views;
import com.yatop.lambda.base.model.views.components.BulkActions;
import com.yatop.lambda.base.model.views.components.Button;
import com.yatop.lambda.base.model.views.components.Filter;
import com.yatop.lambda.base.model.views.components.Options;

import java.util.List;

/**
 * Auto-generated: 2024-06-26 22:45:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Curd1 {

    private String type;
    private String name;
    private String api;

    private Filter filter;
    private List<BulkActions> bulkActions;
    private List<Columns> columns;
    private boolean affixHeader;
    private String columnsTogglable;
    private String placeholder;
    private String tableClassName;
    private String headerClassName;
    private String footerClassName;
    private String toolbarClassName;
    private int combineNum;
    private String bodyClassName;


    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setApi(String api) {
         this.api = api;
     }
     public String getApi() {
         return api;
     }

    public void setFilter(Filter filter) {
         this.filter = filter;
     }
     public Filter getFilter() {
         return filter;
     }

    public void setBulkActions(List<BulkActions> bulkActions) {
         this.bulkActions = bulkActions;
     }
     public List<BulkActions> getBulkActions() {
         return bulkActions;
     }

    public void setColumns(List<Columns> columns) {
         this.columns = columns;
     }
     public List<Columns> getColumns() {
         return columns;
     }

    public void setAffixHeader(boolean affixHeader) {
         this.affixHeader = affixHeader;
     }
     public boolean getAffixHeader() {
         return affixHeader;
     }

    public void setColumnsTogglable(String columnsTogglable) {
         this.columnsTogglable = columnsTogglable;
     }
     public String getColumnsTogglable() {
         return columnsTogglable;
     }

    public void setPlaceholder(String placeholder) {
         this.placeholder = placeholder;
     }
     public String getPlaceholder() {
         return placeholder;
     }

    public void setTableClassName(String tableClassName) {
         this.tableClassName = tableClassName;
     }
     public String getTableClassName() {
         return tableClassName;
     }

    public void setHeaderClassName(String headerClassName) {
         this.headerClassName = headerClassName;
     }
     public String getHeaderClassName() {
         return headerClassName;
     }

    public void setFooterClassName(String footerClassName) {
         this.footerClassName = footerClassName;
     }
     public String getFooterClassName() {
         return footerClassName;
     }

    public void setToolbarClassName(String toolbarClassName) {
         this.toolbarClassName = toolbarClassName;
     }
     public String getToolbarClassName() {
         return toolbarClassName;
     }

    public void setCombineNum(int combineNum) {
         this.combineNum = combineNum;
     }
     public int getCombineNum() {
         return combineNum;
     }

    public void setBodyClassName(String bodyClassName) {
         this.bodyClassName = bodyClassName;
     }
     public String getBodyClassName() {
         return bodyClassName;
     }

    public static class Columns {
        //private int width;
        private String name;
        private String label;
        private String type;
        private String remark;
        private boolean required;
        private List<Button> buttons;
        //private boolean toggled;

        public List<Button> getButtons() {
            return buttons;
        }

        public void setButtons(List<Button> buttons) {
            this.buttons = buttons;
        }



        private Searchable searchable;

        public Searchable getSearchable() {
            return searchable;
        }

        public void setSearchable(Searchable searchable) {
            this.searchable = searchable;
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


    public static class Searchable{
        private String type;
        private String name;
        private String label;
        private String placeholder;
        private String mode;

        private List<Options> options;

        public List<Options> getOptions() {
            return options;
        }

        public void setOptions(List<Options> options) {
            this.options = options;
        }

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

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }


}