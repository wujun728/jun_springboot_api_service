package com.zapi.workbench.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.workbench.dto.DragEntryDTO;
import com.zapi.workbench.entity.WbAppEntry;
import com.zapi.workbench.service.WbAppEntryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 应用条目控制器
 */
@Api(tags = "工作台-应用条目管理")
@RestController
@RequestMapping("/api/workbench/appEntry")
public class WbAppEntryController {

    @Autowired
    private WbAppEntryService wbAppEntryService;

    /**
     * 分页查询应用条目
     *
     * @param current 当前页
     * @param size 每页大小
     * @param appId 应用ID
     * @param name 条目名称
     * @param type 条目类型
     * @param parentId 上级ID
     * @return 分页结果
     */
    @ApiOperation("分页查询应用条目")
    @GetMapping("/page")
    public Result<Page<WbAppEntry>> getAppEntryPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("应用ID") @RequestParam(required = false) Long appId,
            @ApiParam("条目名称") @RequestParam(required = false) String name,
            @ApiParam("条目类型") @RequestParam(required = false) String type,
            @ApiParam("上级ID") @RequestParam(required = false) Long parentId) {
        Page<WbAppEntry> page = new Page<>(current, size);
        Page<WbAppEntry> result = wbAppEntryService.getAppEntryPage(page, appId, name, type, parentId);
        return Result.success(result);
    }

    /**
     * 根据应用ID获取树形结构的条目列表
     *
     * @param appId 应用ID
     * @return 树形结构列表
     */
    @ApiOperation("获取应用条目树")
    @GetMapping("/tree")
    public Result<List<WbAppEntry>> getAppEntryTree(@ApiParam("应用ID") @RequestParam Long appId) {
        List<WbAppEntry> list = wbAppEntryService.getAppEntryTree(appId);
        return Result.success(list);
    }

    /**
     * 根据应用ID和父级ID获取子条目列表
     *
     * @param appId 应用ID
     * @param parentId 父级ID
     * @return 子条目列表
     */
    @ApiOperation("获取子条目列表")
    @GetMapping("/children")
    public Result<List<WbAppEntry>> getAppEntryByParentId(
            @ApiParam("应用ID") @RequestParam Long appId,
            @ApiParam("父级ID") @RequestParam(required = false) Long parentId) {
        List<WbAppEntry> list = wbAppEntryService.getAppEntryByParentId(appId, parentId);
        return Result.success(list);
    }

    /**
     * 根据ID查询应用条目详情
     *
     * @param id 条目ID
     * @return 条目详情
     */
    @ApiOperation("查询应用条目详情")
    @GetMapping("/{id}")
    public Result<WbAppEntry> getAppEntryById(@ApiParam("条目ID") @PathVariable Long id) {
        WbAppEntry appEntry = wbAppEntryService.getById(id);
        if (appEntry == null) {
            return Result.error("应用条目不存在");
        }
        return Result.success(appEntry);
    }

    /**
     * 添加应用条目
     *
     * @param appEntry 应用条目对象
     * @return 操作结果
     */
    @ApiOperation("添加应用条目")
    @PostMapping
    public Result<String> addAppEntry(@RequestBody WbAppEntry appEntry) {
        boolean success = wbAppEntryService.addAppEntry(appEntry);
        if (success) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新应用条目
     *
     * @param appEntry 应用条目对象
     * @return 操作结果
     */
    @ApiOperation("更新应用条目")
    @PutMapping
    public Result<String> updateAppEntry(@RequestBody WbAppEntry appEntry) {
        if (appEntry.getId() == null) {
            return Result.error("条目ID不能为空");
        }
        boolean success = wbAppEntryService.updateAppEntry(appEntry);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 删除应用条目（逻辑删除）
     *
     * @param id 条目ID
     * @return 操作结果
     */
    @ApiOperation("删除应用条目")
    @DeleteMapping("/{id}")
    public Result<String> deleteAppEntry(@ApiParam("条目ID") @PathVariable Long id) {
        try {
            boolean success = wbAppEntryService.deleteAppEntry(id);
            if (success) {
                return Result.success("删除成功");
            }
            return Result.error("删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除应用条目（逻辑删除）
     *
     * @param ids 条目ID列表
     * @return 操作结果
     */
    @ApiOperation("批量删除应用条目")
    @DeleteMapping("/batch")
    public Result<String> batchDeleteAppEntry(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的条目");
        }
        boolean success = wbAppEntryService.batchDeleteAppEntry(ids);
        if (success) {
            return Result.success("批量删除成功");
        }
        return Result.error("批量删除失败");
    }

    @ApiOperation("拖拽排序")
    @PostMapping("/dragSort")
    public Result<Void> dragSort(@RequestBody @Valid DragEntryDTO dragEntryDTO){
        wbAppEntryService.dragSort(dragEntryDTO);
        return Result.success();
    }


}
