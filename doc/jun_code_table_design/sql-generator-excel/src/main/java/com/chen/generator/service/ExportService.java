package com.chen.generator.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.chen.generator.model.ExcelView;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Chenzedeng
 * @Email: chenzedeng@geexfinance.com
 * @Date: 2021/4/20 17:50
 * @description:
 * @version: 1.0
 */
public class ExportService {

    /**
     * 导出到本地文件
     *
     * @param path
     * @param excelViews
     * @param databaseName
     * @throws IOException
     */
    public void exportFile(String path, String databaseName, List<ExcelView> excelViews) {
        List<Map<String, Object>> maps = excelViews.stream().map(v -> {
            Map<String, Object> map = new HashMap<>(3);
            map.put("title", v.getExportParams());
            map.put("entity", v.getExportClass());
            map.put("data", v.getDataList());
            return map;
        }).collect(Collectors.toList());
        Workbook workbook = ExcelExportUtil.exportExcel(maps, ExcelType.XSSF);
        String format = DateUtil.format(new Date(), "yyyy年MM月dd日HH时mm分ss秒");
        File file = FileUtil.file(path, StrUtil.format("export_{}_{}.xlsx", databaseName, format));
        try {
            workbook.write(new FileOutputStream(file));
            System.out.println(StrUtil.format("---------->>文件导出成功<<----------\n保存路径:{}", file.getAbsoluteFile()));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("导出文件失败");
        }
    }
}
