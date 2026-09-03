package com.zapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.entity.SysDictType;
import com.zapi.service.SysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 字典类型Controller
 */
@Api(tags = "字典类型管理")
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController {

    @Autowired
    private SysDictTypeService dictTypeService;

    /**
     * 分页查询字典类型列表
     */
    @ApiOperation("分页查询字典类型列表")
    @GetMapping("/list")
    public Result<Page<SysDictType>> list(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("字典名称") @RequestParam(required = false) String dictName,
            @ApiParam("字典类型") @RequestParam(required = false) String dictType,
            @ApiParam("状态") @RequestParam(required = false) String status) {
        
        Page<SysDictType> page = new Page<>(current, size);
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        
        // 过滤已删除的记录
        queryWrapper.eq(SysDictType::getDeleted, 0);
        
        if (StringUtils.hasText(dictName)) {
            queryWrapper.like(SysDictType::getDictName, dictName);
        }
        if (StringUtils.hasText(dictType)) {
            queryWrapper.like(SysDictType::getDictType, dictType);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(SysDictType::getStatus, status);
        }
        
        queryWrapper.orderByAsc(SysDictType::getDictId);
        Page<SysDictType> result = dictTypeService.page(page, queryWrapper);
        
        return Result.success(result);
    }

    /**
     * 查询所有字典类型
     */
    @ApiOperation("查询所有字典类型")
    @GetMapping("/all")
    public Result<List<SysDictType>> all() {
        List<SysDictType> list = dictTypeService.selectDictTypeAll();
        return Result.success(list);
    }

    /**
     * 根据ID获取字典类型详情
     */
    @ApiOperation("根据ID获取字典类型详情")
    @GetMapping("/{dictId}")
    public Result<SysDictType> getInfo(@ApiParam("字典ID") @PathVariable Long dictId) {
        SysDictType dictType = dictTypeService.getById(dictId);
        if (dictType != null) {
            return Result.success(dictType);
        }
        return Result.error("字典类型不存在");
    }

    /**
     * 根据字典类型查询字典类型信息
     */
    @ApiOperation("根据字典类型查询字典类型信息")
    @GetMapping("/type/{dictType}")
    public Result<SysDictType> getByType(@ApiParam("字典类型") @PathVariable String dictType) {
        SysDictType sysDictType = dictTypeService.selectDictTypeByType(dictType);
        if (sysDictType != null) {
            return Result.success(sysDictType);
        }
        return Result.error("字典类型不存在");
    }

    /**
     * 新增字典类型
     */
    @ApiOperation("新增字典类型")
    @PostMapping("/add")
    public Result<String> add(@RequestBody SysDictType dictType) {
        // 校验字典类型是否唯一
        if (!dictTypeService.checkDictTypeUnique(dictType)) {
            return Result.error("新增字典'" + dictType.getDictName() + "'失败，字典类型已存在");
        }
        
        // 设置默认状态
        if (!StringUtils.hasText(dictType.getStatus())) {
            dictType.setStatus("0");
        }
        
        boolean success = dictTypeService.save(dictType);
        if (success) {
            return Result.success("新增成功");
        }
        return Result.error("新增失败");
    }

    /**
     * 修改字典类型
     */
    @ApiOperation("修改字典类型")
    @PutMapping("/update")
    public Result<String> update(@RequestBody SysDictType dictType) {
        // 校验字典类型是否唯一
        if (!dictTypeService.checkDictTypeUnique(dictType)) {
            return Result.error("修改字典'" + dictType.getDictName() + "'失败，字典类型已存在");
        }
        
        boolean success = dictTypeService.updateById(dictType);
        if (success) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    /**
     * 删除字典类型
     */
    @ApiOperation("删除字典类型")
    @DeleteMapping("/delete/{dictIds}")
    public Result<String> delete(@ApiParam("字典ID，多个用逗号分隔") @PathVariable Long[] dictIds) {
        boolean success = dictTypeService.removeByIds(Arrays.asList(dictIds));
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 刷新字典缓存
     */
    @ApiOperation("刷新字典缓存")
    @DeleteMapping("/refreshCache")
    public Result<String> refreshCache() {
        // 这里可以添加缓存刷新逻辑
        return Result.success("刷新成功");
    }
}