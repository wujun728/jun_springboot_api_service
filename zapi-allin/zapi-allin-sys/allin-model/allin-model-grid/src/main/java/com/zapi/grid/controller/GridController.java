package com.zapi.grid.controller;

import com.zapi.common.Result;
import com.zapi.grid.model.entity.Grid;
import com.zapi.grid.service.GridService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 表格Controller
 *
 * @author zapi
 * @since 2025-01-09
 */
@Api(tags = "表格管理")
@RestController
@RequestMapping("/api/grid")
@RequiredArgsConstructor
public class GridController {

    private final GridService gridService;

    @GetMapping("/{gridId}")
    @ApiOperation("获取表格详情")
    public Result<Grid> getGridDetail(@ApiParam("表格ID") @PathVariable String gridId) {
        Grid grid = gridService.getById(gridId);
        return Result.success(grid);
    }

    @GetMapping("/entry/{entryId}")
    @ApiOperation("根据应用条目ID获取表格")
    public Result<Grid> getGridByEntryId(@ApiParam("应用条目ID") @PathVariable Long entryId) {
        Grid grid = gridService.getByEntryId(entryId);
        return Result.success(grid);
    }

    @PostMapping
    @ApiOperation("创建表格")
    public Result<Grid> createGrid(@RequestBody Grid grid) {
        gridService.save(grid);
        return Result.success(grid);
    }

    @PutMapping
    @ApiOperation("更新表格")
    public Result<Void> updateGrid(@RequestBody Grid grid) {
        gridService.updateById(grid);
        return Result.success();
    }

    @DeleteMapping("/{gridId}")
    @ApiOperation("删除表格")
    public Result<Void> deleteGrid(@ApiParam("表格ID") @PathVariable String gridId) {
        gridService.removeById(gridId);
        return Result.success();
    }
}
