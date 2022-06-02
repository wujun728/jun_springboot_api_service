package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description DepartmentDO
 * @Date 2020/4/23 23:20
 * @Created by 王弘博
 */
@Data
@TableName("department")
public class DepartmentDO implements Serializable {

    /**
     * 主鍵ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 部门主键ID
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门名称
     */
    @TableField("department_name")
    private String departmentName;

    /**
     * 显示顺序
     */
    @TableField("order_num")
    private Integer orderNum;

    /**
     * 负责人编号
     */
    @TableField("leader_id")
    private Integer leaderId;

    /**
     * 部门状态（0正常 1停用）
     */
    private String status;

    /**
     * 创建者
     */
    @TableField("create_by")
    private Integer createBy;

    /**
     * 更新者
     */
    @TableField("update_by")
    private Integer updateBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
}
