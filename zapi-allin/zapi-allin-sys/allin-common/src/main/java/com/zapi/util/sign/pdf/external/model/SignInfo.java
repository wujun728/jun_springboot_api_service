package com.zapi.util.sign.pdf.external.model;

import com.itextpdf.kernel.geom.Rectangle;

/**
 * @author Ethan
 * @version 1.0
 * @description: 签章信息
 * @date 2021/6/24 0024
 */
public class SignInfo {
    private Rectangle rectangle;
    private int pageNumber;
    private String reason;
    private String location;
    private String fieldName;//签入字段
    private boolean appendModel; //是否允许后续签名，如果是最后一个签名，应该是false
    private String imgPath;//红章

    public SignInfo(Rectangle rectangle, int pageNumber, String reason, String location, String fieldName,String imgPath,boolean appendModel) {
        this.rectangle = rectangle;
        this.pageNumber = pageNumber;
        this.reason = reason;
        this.location = location;
        this.appendModel = appendModel;
        this.fieldName = fieldName;
        this.imgPath = imgPath;
    }
   
    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAppendModel() {
        return appendModel;
    }

    public void setAppendModel(boolean appendModel) {
        this.appendModel = appendModel;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
