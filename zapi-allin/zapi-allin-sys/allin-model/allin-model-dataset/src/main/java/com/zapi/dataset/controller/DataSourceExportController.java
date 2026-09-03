package com.zapi.dataset.controller;

import com.zapi.dataset.config.DataSourceConfig;
import com.zapi.dataset.model.req.ExportRequest;
import com.zapi.dataset.model.req.SqlConvertRequest;
import com.zapi.dataset.support.converter.DmToMySqlConverter;
import com.zapi.dataset.support.converter.MySqlToDmConverter;
import com.zapi.dataset.support.factory.DatabaseExportServiceFactory;
import com.zapi.dataset.support.service.DatabaseExportService;
import com.zapi.dataset.support.service.DatabaseMetaService;
import com.zapi.dataset.support.service.DatabaseMetaServiceFactory;
import com.zapi.dataset.utils.UUIDGenerator;
import com.zapi.common.Result;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dataset/dataSourceExport")
public class DataSourceExportController {

    @PostMapping("/tables")
    public Result<List<String>> getTableList(@RequestBody DataSourceConfig config) throws SQLException {
        DatabaseMetaService metaService = DatabaseMetaServiceFactory.getService(config.getDbType());
        return Result.success(metaService.getTableList(config));
    }

    /**
     * 预览SQL脚本
     * @param request 导出请求参数
     * @return SQL文本预览
     */
    @PostMapping("/previewSql")
    public Result<String> previewSql(@RequestBody ExportRequest request) throws Exception {
        try {
            // 获取导出服务并生成脚本
            DatabaseExportService service = DatabaseExportServiceFactory.getService(request.getDbType());
            String script = service.exportScript(
                    request.getDataSourceConfig(),
                    request.isExportData(),
                    request.getSelectedTables()
            );

            // 如果请求SQL转换
            if (request.getTargetDbType() != null && !request.getTargetDbType().equals(request.getDbType())) {
                script = convertSqlScript(script, request.getDbType(), request.getTargetDbType());
            }

            return Result.success(script);
        } catch (Exception e) {
            return Result.error("SQL预览失败: " + e.getMessage());
        }
    }

    /**
     * 导出SQL脚本文件
     * @param request 导出请求参数
     * @return 文件下载
     */
    @PostMapping("/generate")
    public ResponseEntity<?> generateScript(@RequestBody ExportRequest request) throws Exception {
        // 获取导出服务并生成脚本
        DatabaseExportService service = DatabaseExportServiceFactory.getService(request.getDbType());
        String script = service.exportScript(
                request.getDataSourceConfig(),
                request.isExportData(),
                request.getSelectedTables()
        );

        // 如果请求SQL转换
        if (request.getTargetDbType() != null && !request.getTargetDbType().equals(request.getDbType())) {
            script = convertSqlScript(script, request.getDbType(), request.getTargetDbType());
        }

        // 返回文件下载
        String fileName = "export_" + UUIDGenerator.uuid() + ".sql";
        Path tempFile = Files.createTempFile("export_", ".sql");
        Files.write(tempFile, script.getBytes(StandardCharsets.UTF_8));

        Resource resource = new FileSystemResource(tempFile.toFile());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


    @PostMapping("/convertSql")
    public Result<String> convertSql(@RequestBody SqlConvertRequest request) {
        try {
            String convertedSql = convertSqlScript(
                    request.getSqlContent(),
                    request.getSourceDbType(),
                    request.getTargetDbType()
            );
            return Result.success(convertedSql);
        } catch (Exception e) {
            return Result.error("SQL转换失败: " + e.getMessage());
        }
    }


    /**
     * 通用的SQL脚本转换方法
     */
    private String convertSqlScript(String sqlContent, String sourceDbType, String targetDbType) {
        if ("mysql".equalsIgnoreCase(targetDbType)) {
            return DmToMySqlConverter.convert(sqlContent);
        } else if ("dm".equalsIgnoreCase(targetDbType)) {
            return MySqlToDmConverter.convert(sqlContent);
        }
        throw new IllegalArgumentException("不支持的数据库类型: " + targetDbType);
    }


}
