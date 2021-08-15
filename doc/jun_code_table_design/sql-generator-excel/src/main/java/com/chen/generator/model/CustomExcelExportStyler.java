package com.chen.generator.model;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.styler.AbstractExcelExportStyler;
import org.apache.poi.ss.usermodel.*;

/**
 * @Author: YuChen
 * @CreateDate: 2021/5/8 9:31 上午
 * @Description:
 * @version: 1.0
 */
public class CustomExcelExportStyler extends AbstractExcelExportStyler {

    @Override
    public CellStyle getHeaderStyle(short i) {
        CellStyle titleStyle = this.workbook.createCellStyle();
        Font font = this.workbook.createFont();
        font.setFontHeightInPoints((short)24);
        titleStyle.setFont(font);
        titleStyle.setFillForegroundColor(i);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return titleStyle;
    }

    @Override
    public CellStyle getTitleStyle(short i) {
        return null;
    }

    @Override
    public CellStyle getStyles(boolean b, ExcelExportEntity excelExportEntity) {
        return null;
    }

}
