package com.zapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.dto.DepartmentTreeNode;
import com.zapi.entity.Department;

import java.util.List;

/**
 * 部门Service接口
 */
public interface DepartmentService extends IService<Department> {
    
    /**
     * 构建部门树形结构
     * @return 部门树形结构列表
     */
    List<DepartmentTreeNode> buildDepartmentTree();
} 