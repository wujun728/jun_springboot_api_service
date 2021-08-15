package com.chen.generator.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * @Author: Chenzedeng
 * @Email: chenzedeng@geexfinance.com
 * @Date: 2021/4/20 17:20
 * @description:
 * @version: 1.0
 */
@Data
public class ExcelView {

    private ExportParams exportParams;

    private List<ColumnEntity> dataList;

    public ExcelView(TableEntity tableEntity) {
        this.exportParams = new ExportParams();
        this.dataList = tableEntity.getColumns();
        exportParams.setSheetName(tableEntity.getTableComment() != null ? tableEntity.getTableComment() : tableEntity.getTableName());
        exportParams.setTitle(StrUtil.format("{}   {}", tableEntity.getTableName(), tableEntity.getTableComment()));
        exportParams.setType(ExcelType.XSSF);
        this.autoWidth();
    }

    /**
     * 自动计算宽度
     */
    private void autoWidth() {
        Field[] fields = ReflectUtil.getFields(getExportClass());
        for (Field field : fields) {
            int maxSize = 0;
            for (ColumnEntity entity : dataList) {
                Object value = ReflectUtil.getFieldValue(entity, field);
                maxSize = Math.max(maxSize, String.valueOf(value).length());
            }
            Excel annotation = field.getAnnotation(Excel.class);
            maxSize = Math.max(maxSize, annotation.name().length());
            InvocationHandler h = Proxy.getInvocationHandler(annotation);
            // 获取 AnnotationInvocationHandler 的 memberValues 字段
            Field hField;
            try {
                hField = h.getClass().getDeclaredField("memberValues");
                // 因为这个字段事 private final 修饰，所以要打开权限
                hField.setAccessible(true);
                // 获取 memberValues
                Map memberValues = (Map) hField.get(h);
                // 修改 value 属性值
                memberValues.put("width", Double.valueOf(maxSize * 3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Class getExportClass() {
        return ColumnEntity.class;
    }

}
