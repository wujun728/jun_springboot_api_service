package com.zapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户印章实体类
 */
@Data
@TableName("sys_user_seal")
public class UserSeal {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 印章名称
     */
    private String sealName;

    /**
     * 印章类型：SQUARE_BORDER-方章有边框，SQUARE_NO_BORDER-方章无边框，SQUARE_3-方章正，ELLIPSE-椭圆章
     */
    private String sealType;

    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 状态：0-停用，1-启用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

