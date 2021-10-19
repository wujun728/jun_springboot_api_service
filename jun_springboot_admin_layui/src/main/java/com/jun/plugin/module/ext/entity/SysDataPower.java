package com.jun.plugin.module.ext.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jun.plugin.system.common.aop.aspect.DataFilterTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统数据权限
 * </p>
 *
 * @author laker
 * @since 2021-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDataPower implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据权限id
     */
    @TableId(value = "data_power_id", type = IdType.AUTO)
    private Long dataPowerId;

    /**
     * mapper名称
     */
    private String mapper;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 权限类型
     */
    private DataFilterTypeEnum filterType;

    private LocalDateTime createTime;


}
