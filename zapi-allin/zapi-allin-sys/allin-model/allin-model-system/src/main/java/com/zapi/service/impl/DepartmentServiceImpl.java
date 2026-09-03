package com.zapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.dto.DepartmentTreeNode;
import com.zapi.entity.Department;
import com.zapi.mapper.DepartmentMapper;
import com.zapi.service.DepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门Service实现类
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    
    @Override
    public List<DepartmentTreeNode> buildDepartmentTree() {
        // 获取所有未删除的部门
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0).orderByAsc("sort", "id");
        List<Department> allDepartments = this.list(queryWrapper);
        
        // 转换为 DepartmentTreeNode
        List<DepartmentTreeNode> allNodes = allDepartments.stream()
                .map(this::convertToTreeNode)
                .collect(Collectors.toList());
        
        // 构建父部门映射
        Map<Long, String> deptNameMap = allDepartments.stream()
                .collect(Collectors.toMap(Department::getId, Department::getDeptName));
        
        // 设置父部门名称
        allNodes.forEach(node -> {
            if (node.getParentId() != null && node.getParentId() != 0) {
                node.setParentName(deptNameMap.get(node.getParentId()));
            }
        });
        
        // 构建树形结构
        return buildTree(allNodes, 0L);
    }
    
    /**
     * 将 Department 转换为 DepartmentTreeNode
     */
    private DepartmentTreeNode convertToTreeNode(Department department) {
        DepartmentTreeNode node = new DepartmentTreeNode();
        BeanUtils.copyProperties(department, node);
        node.setChildren(new ArrayList<>());
        return node;
    }
    
    /**
     * 递归构建树形结构
     */
    private List<DepartmentTreeNode> buildTree(List<DepartmentTreeNode> allNodes, Long parentId) {
        List<DepartmentTreeNode> children = new ArrayList<>();
        
        for (DepartmentTreeNode node : allNodes) {
            Long nodeParentId = node.getParentId() == null ? 0L : node.getParentId();
            if (nodeParentId.equals(parentId)) {
                // 递归构建子节点
                List<DepartmentTreeNode> subChildren = buildTree(allNodes, node.getId());
                node.setChildren(subChildren);
                node.setHasChildren(!subChildren.isEmpty());
                children.add(node);
            }
        }
        
        return children;
    }
} 