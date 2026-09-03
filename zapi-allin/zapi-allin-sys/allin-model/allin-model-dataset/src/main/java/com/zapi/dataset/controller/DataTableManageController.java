package com.zapi.dataset.controller;

import com.zapi.dataset.config.DataSourceConfig;
import com.zapi.dataset.model.vo.FieldInfoVO;
import com.zapi.dataset.model.vo.PageResultVO;
import com.zapi.dataset.support.service.DatabaseMetaService;
import com.zapi.dataset.support.service.DatabaseMetaServiceFactory;
import com.zapi.common.Result;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/dataset/dataTableManage")
public class DataTableManageController {

    // 获取表明与表注释
    @PostMapping("/tables")
    public Result<PageResultVO<HashMap<String, Object>>> getTableSAndComment(@RequestBody DataSourceConfig config) throws SQLException {
        DatabaseMetaService metaService = DatabaseMetaServiceFactory.getService(config.getDbType());
        List<HashMap<String, Object>> tableSAndComment = metaService.getTableSAndComment(config);
        // 手动分页逻辑
        int total = tableSAndComment.size();
        int fromIndex = (config.getPageNum() - 1) * config.getPageSize();
        int toIndex = Math.min(fromIndex + config.getPageSize(), total);
        if (fromIndex >= total) {
            return Result.error("没有更多数据");
        }
        List<HashMap<String, Object>> pageData = tableSAndComment.subList(fromIndex, toIndex);
        PageResultVO<HashMap<String, Object>> pageResultVO = new PageResultVO<>(
                pageData,
                total,
                config.getPageNum(),
                config.getPageSize(),
                (int) Math.ceil((double) total / config.getPageSize())
        );
        return Result.success(pageResultVO);
    }

    // 获取指定表的字段信息
    @PostMapping("/getTableColumnList")
    public Result<List<FieldInfoVO>> getTableColumnList(@RequestBody DataSourceConfig config) throws SQLException {
        DatabaseMetaService metaService = DatabaseMetaServiceFactory.getService(config.getDbType());
        return Result.success(metaService.getTableColumnList(config));
    }

    // 查询数据源字段列表
    @GetMapping("/getColumnType/{type}")
    public Result<List<String>> getColumnType(@PathVariable String type) {
        DatabaseMetaService metaService = DatabaseMetaServiceFactory.getService(type);
        return Result.success(metaService.getColumnType());
    }

    // 更新表字段
    @PostMapping("/updateColumnType")
    public Result<Boolean> updateColumnType(@RequestBody DataSourceConfig config) throws SQLException {
        DatabaseMetaService metaService = DatabaseMetaServiceFactory.getService(config.getDbType());
        return Result.success(metaService.updateColumnType(config));
    }

    // 新增表信息
    @PostMapping("/addTable")
    public Result<Boolean> addTable(@RequestBody DataSourceConfig config) throws SQLException {
        DatabaseMetaService metaService = DatabaseMetaServiceFactory.getService(config.getDbType());
        return Result.success(metaService.addTable(config));
    }

    // 删除表
    @PostMapping("/dropTable")
    public Result<Boolean> dropTable(@RequestBody DataSourceConfig config) throws SQLException {
        DatabaseMetaService metaService = DatabaseMetaServiceFactory.getService(config.getDbType());
        return Result.success(metaService.dropTable(config));
    }

}
