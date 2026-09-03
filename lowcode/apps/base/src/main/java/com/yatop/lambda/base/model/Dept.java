package com.yatop.lambda.base.model;

import cn.hutool.core.lang.tree.Tree;
import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;

@Table(name = "base_dept", displayName = "部门")
public class Dept extends Model<Dept> {

    @Id
    private Long id;

    @Column(label = "部门名称")
    private String name;

    /**
     * 显示顺序
     */
    @Column(label = "排序")
    private Integer orderNum;

    /**
     * 负责人
     */
    @Column(label = "部门负责人")
    private String leader;

    /**
     * 联系电话
     */
    @Column(label = "电话")
    private String phone;

    /**
     * 邮箱
     */
    @Column(label = "邮箱")
    private String email;

    /**
     * 部门状态:0正常,1停用
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 祖级列表
     */
    private String ancestors;


    private String parentName;

    /**
     * 父菜单ID
     */
    private Long parentId;


    private Long getParentId(){
        return getLong("parentId");
    }

    @Service
    public List<Tree<Long>> deptTreeSelect(){
       return null;
    }
}
