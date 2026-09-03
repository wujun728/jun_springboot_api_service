package com.yatop.lambda.base.model.views.components;

import com.yatop.lambda.base.model.views.Curd1;

import java.util.List;

public class Curd {
    private String type;
    private boolean draggable = true;
    private String api;
    private int perPage = 50;
    private boolean keepItemSelectionOnPageChange = true;
    private int maxKeepItemSelectionLength =11;
    private boolean autoFillHeight = true;
    private String labelTpl;
    private boolean autoGenerateFilter = true;
    private List<BulkActions> bulkActions;

    private String quickSaveApi;
    private String quickSaveItemApi;

    private boolean filterTogglable =true;
    private List<Object> headerToolbar;

    private List<Object> footerToolbar;



    private List<Curd1.Columns> columns;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public boolean isKeepItemSelectionOnPageChange() {
        return keepItemSelectionOnPageChange;
    }

    public void setKeepItemSelectionOnPageChange(boolean keepItemSelectionOnPageChange) {
        this.keepItemSelectionOnPageChange = keepItemSelectionOnPageChange;
    }

    public int getMaxKeepItemSelectionLength() {
        return maxKeepItemSelectionLength;
    }

    public void setMaxKeepItemSelectionLength(int maxKeepItemSelectionLength) {
        this.maxKeepItemSelectionLength = maxKeepItemSelectionLength;
    }

    public boolean isAutoFillHeight() {
        return autoFillHeight;
    }

    public void setAutoFillHeight(boolean autoFillHeight) {
        this.autoFillHeight = autoFillHeight;
    }

    public String getLabelTpl() {
        return labelTpl;
    }

    public void setLabelTpl(String labelTpl) {
        this.labelTpl = labelTpl;
    }

    public boolean isAutoGenerateFilter() {
        return autoGenerateFilter;
    }

    public void setAutoGenerateFilter(boolean autoGenerateFilter) {
        this.autoGenerateFilter = autoGenerateFilter;
    }

    public List<BulkActions> getBulkActions() {
        return bulkActions;
    }

    public void setBulkActions(List<BulkActions> bulkActions) {
        this.bulkActions = bulkActions;
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

    public boolean isFilterTogglable() {
        return filterTogglable;
    }

    public void setFilterTogglable(boolean filterTogglable) {
        this.filterTogglable = filterTogglable;
    }


    public List<Object> getHeaderToolbar() {
        return headerToolbar;
    }

    public void setHeaderToolbar(List<Object> headerToolbar) {
        this.headerToolbar = headerToolbar;
    }

    public List<Object> getFooterToolbar() {
        return footerToolbar;
    }

    public void setFooterToolbar(List<Object> footerToolbar) {
        this.footerToolbar = footerToolbar;
    }

    public List<Curd1.Columns> getColumns() {
        return columns;
    }

    public void setColumns(List<Curd1.Columns> columns) {
        this.columns = columns;
    }
}
