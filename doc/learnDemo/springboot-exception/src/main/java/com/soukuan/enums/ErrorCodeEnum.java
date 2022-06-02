package com.soukuan.enums;

import lombok.Getter;

/**
 * @author xiebiao
 * @date  2018-07-11  10:05:05
 * @version v1.0
 */
@Getter
public enum ErrorCodeEnum implements BaseEnum<ErrorCodeEnum> {
    /**
     * 用户不存在.
     */
    UNDEFINED_ERROR("undefined_error","未知错误"),
    /**
     * 用户不存在.
     */
    USER_NOT_FOUND("user_not_found","用户不存在"),
    /**
     * 用户状态异常.
     */
    USER_STATUS_FAILD("user_status_faild","用户状态异常");

    private String code;

    private String desc;

    ErrorCodeEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getDescByCode(String code) {
        for (ErrorCodeEnum ece : ErrorCodeEnum.values()) {
            if(ece.code.equals(code)){
                return ece.getDesc();
            }
        }
        return ErrorCodeEnum.USER_STATUS_FAILD.getDesc();
    }
}
