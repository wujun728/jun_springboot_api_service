package com.yuyaogc.lowcode.engine.util;

public enum CascadeType {

    /**
     * 设置关联字段为空
     */
    DEL_SET_NULL,
    /**
     * 被级联删除
     */
    DELETE,

    /**
     * 存在记录则不允许主表删除
     */
    DEL_NO_PERMIT_RELATIVE

}
