package com.yuyaogc.lowcode.engine.enums;

import java.io.Serializable;

/**
 * 三态布尔值
 */
public enum Bool implements Serializable {

    /**
     * 默认，不指定，使用内置的默认值
     */
    Default,
    /**
     * 真
     */
    True,
    /**
     * 假
     */
    False;

    Bool() {

    }

}