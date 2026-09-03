package com.zapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.dto.DepartmentTreeNode;
import com.zapi.entity.Department;
import com.zapi.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门Controller
 */
@Api(tags = "部门管理")
@RestController
@RequestMapping("/system/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 分页查询部门列表
     */
    @ApiOperation("分页查询部门列表")
    @GetMapping("/list")
    public Result<Page<Department>> getDepartmentList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("部门名称") @RequestParam(required = false) String deptName) {
        Page<Department> page = new Page<>(current, size);
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        
        // 过滤已删除的记录
        queryWrapper.eq("deleted", 0);
        
        if (deptName != null && !deptName.isEmpty()) {
            queryWrapper.like("dept_name", deptName);
        }
        
        queryWrapper.orderByAsc("sort").orderByDesc("create_time");
        Page<Department> result = departmentService.page(page, queryWrapper);
        
        return Result.success(result);
    }

    /**
     * 获取所有部门
     */
    @ApiOperation("获取所有部门")
    @GetMapping("/all")
    public Result<List<Department>> getAllDepartments() {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0).eq("status", 1).orderByAsc("sort");
        List<Department> departments = departmentService.list(queryWrapper);
        return Result.success(departments);
    }

    /**
     * 获取部门树形结构
     */
    @ApiOperation("获取部门树形结构")
    @GetMapping("/tree")
    public Result<List<DepartmentTreeNode>> getDepartmentTree() {
        List<DepartmentTreeNode> tree = departmentService.buildDepartmentTree();
        return Result.success(tree);
    }

    /**
     * 根据ID获取部门
     */
    @ApiOperation("根据ID获取部门")
    @GetMapping("/{id}")
    public Result<Department> getDepartmentById(@ApiParam("部门ID") @PathVariable Long id) {
        Department department = departmentService.getById(id);
        if (department != null) {
            return Result.success(department);
        }
        return Result.error("部门不存在");
    }

    /**
     * 添加部门
     */
    @ApiOperation("添加部门")
    @PostMapping("/add")
    public Result<String> addDepartment(@RequestBody Department department) {
        // 检查部门编码是否已存在（排除已删除的）
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_code", department.getDeptCode()).eq("deleted", 0);
        Department existingDepartment = departmentService.getOne(queryWrapper);
        if (existingDepartment != null) {
            return Result.error("部门编码已存在");
        }
        
        // 设置默认状态
        if (department.getStatus() == null) {
            department.setStatus(1);
        }
        
        boolean success = departmentService.save(department);
        if (success) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新部门
     */
    @ApiOperation("更新部门")
    @PutMapping("/update")
    public Result<String> updateDepartment(@RequestBody Department department) {
        // 检查部门编码是否重复（排除自己和已删除的）
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_code", department.getDeptCode()).ne("id", department.getId()).eq("deleted", 0);
        Department existingDepartment = departmentService.getOne(queryWrapper);
        if (existingDepartment != null) {
            return Result.error("部门编码已存在");
        }
        
        boolean success = departmentService.updateById(department);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 删除部门
     */
    @ApiOperation("删除部门")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteDepartment(@ApiParam("部门ID") @PathVariable Long id) {
        // 检查是否有未删除的子部门
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id).eq("deleted", 0);
        long count = departmentService.count(queryWrapper);
        if (count > 0) {
            return Result.error("该部门下有子部门，无法删除");
        }
        
        boolean success = departmentService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
} 