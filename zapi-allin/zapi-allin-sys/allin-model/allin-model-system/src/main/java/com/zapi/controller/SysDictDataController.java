package com.zapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.entity.SysDictData;
import com.zapi.service.SysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典数据Controller
 */
@Api(tags = "字典数据管理")
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController {

    @Autowired
    private SysDictDataService dictDataService;

    /**
     * 分页查询字典数据列表
     */
    @ApiOperation("分页查询字典数据列表")
    @GetMapping("/list")
    public Result<Page<SysDictData>> list(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("字典类型") @RequestParam(required = false) String dictType,
            @ApiParam("字典标签") @RequestParam(required = false) String dictLabel,
            @ApiParam("状态") @RequestParam(required = false) String status) {
        
        Page<SysDictData> page = new Page<>(current, size);
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<>();
        
        // 过滤已删除的记录
        queryWrapper.eq(SysDictData::getDeleted, 0);
        
        if (StringUtils.hasText(dictType)) {
            queryWrapper.eq(SysDictData::getDictType, dictType);
        }
        if (StringUtils.hasText(dictLabel)) {
            queryWrapper.like(SysDictData::getDictLabel, dictLabel);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(SysDictData::getStatus, status);
        }
        
        queryWrapper.orderByAsc(SysDictData::getDictSort);
        Page<SysDictData> result = dictDataService.page(page, queryWrapper);
        
        return Result.success(result);
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @ApiOperation("根据字典类型查询字典数据信息")
    @GetMapping("/type/{dictType}")
    public Result<List<SysDictData>> dictType(@ApiParam("字典类型") @PathVariable String dictType) {
        List<SysDictData> data = dictDataService.selectDictDataByType(dictType);
        return Result.success(data);
    }

    /**
     * 根据ID获取字典数据详情
     */
    @ApiOperation("根据ID获取字典数据详情")
    @GetMapping("/{dictCode}")
    public Result<SysDictData> getInfo(@ApiParam("字典编码") @PathVariable Long dictCode) {
        SysDictData dictData = dictDataService.selectDictDataById(dictCode);
        if (dictData != null) {
            return Result.success(dictData);
        }
        return Result.error("字典数据不存在");
    }

    /**
     * 根据字典类型和字典键值查询字典标签
     */
    @ApiOperation("根据字典类型和字典键值查询字典标签")
    @GetMapping("/label")
    public Result<String> getDictLabel(
            @ApiParam("字典类型") @RequestParam String dictType,
            @ApiParam("字典键值") @RequestParam String dictValue) {
        String label = dictDataService.selectDictLabel(dictType, dictValue);
        return Result.success(label);
    }

    /**
     * 新增字典数据
     */
    @ApiOperation("新增字典数据")
    @PostMapping("/add")
    public Result<String> add(@RequestBody SysDictData dictData) {
        // 设置默认状态
        if (!StringUtils.hasText(dictData.getStatus())) {
            dictData.setStatus("0");
        }
        // 设置默认排序
        if (dictData.getDictSort() == null) {
            dictData.setDictSort(0);
        }
        // 设置默认是否默认
        if (!StringUtils.hasText(dictData.getIsDefault())) {
            dictData.setIsDefault("N");
        }
        
        boolean success = dictDataService.insertDictData(dictData);
        if (success) {
            return Result.success("新增成功");
        }
        return Result.error("新增失败");
    }

    /**
     * 修改字典数据
     */
    @ApiOperation("修改字典数据")
    @PutMapping("/update")
    public Result<String> update(@RequestBody SysDictData dictData) {
        boolean success = dictDataService.updateDictData(dictData);
        if (success) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    /**
     * 删除字典数据
     */
    @ApiOperation("删除字典数据")
    @DeleteMapping("/delete/{dictCodes}")
    public Result<String> delete(@ApiParam("字典编码，多个用逗号分隔") @PathVariable Long[] dictCodes) {
        boolean success = dictDataService.deleteDictDataByIds(dictCodes);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}