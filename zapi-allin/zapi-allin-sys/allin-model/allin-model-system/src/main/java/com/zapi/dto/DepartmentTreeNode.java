package com.zapi.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门树形节点DTO
 */
@Data
public class DepartmentTreeNode {
    
    private Long id;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 部门编码
     */
    private String deptCode;
    
    /**
     * 父部门ID
     */
    private Long parentId;
    
    /**
     * 父部门名称
     */
    private String parentName;
    
    /**
     * 部门描述
     */
    private String description;
    
    /**
     * 负责人
     */
    private String leader;
    
    /**
     * 联系电话
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 子部门列表
     */
    private List<DepartmentTreeNode> children = new ArrayList<>();
    
    /**
     * 是否有子节点
     */
    private Boolean hasChildren;
}

