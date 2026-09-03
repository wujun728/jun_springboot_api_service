package com.gitee.freakchicken.entity;

import lombok.Data;

@Deprecated
@Data
public class ResponseDto {
    String msg;
    Object data;
    boolean success;

    public static ResponseDto apiSuccess(Object data) {
        ResponseDto dto = new ResponseDto();
        dto.setData(data);
        dto.setSuccess(true);
        dto.setMsg("dbapi接口访问成功");
        return dto;
    }

    public static ResponseDto fail(String msg) {
        ResponseDto dto = new ResponseDto();
        dto.setSuccess(false);
        dto.setMsg(msg);
        return dto;

    }
}
