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
@TableName("calculate_rule")
public class CalculateRuleDO implements Serializable {

    /**
     * 主鍵ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("interface_id")
    private String interfaceId;

    @TableField("bean_name")
    private String beanName;

    @TableField("calculate_rule")
    private String calculateRule;

    @TableField("calculate_type")
    private String calculateType;

    @TableField("extend_info")
    private String extendInfo;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField("modified_time")
    private Date modifiedTime;
}
