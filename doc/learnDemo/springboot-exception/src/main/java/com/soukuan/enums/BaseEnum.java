package com.soukuan.enums;

/**
 * @author xiebiao
 * @date  2018-07-11  10:05:05
 * @version v1.0
 */
public interface BaseEnum<T> {

    /**
     * 通过描述
     * @return
     */
    String getDesc();

    /**
     * 获取code
     * @return
     */
    String getCode();

    /**
     * 通过code获取描述
     * @param code
     * @return
     */
    String getDescByCode(String code);

}
