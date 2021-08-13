package com.jun.plugin.base.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: LoginRespVO
 * TODO:类文件简单描述
 * @author Wujun
 * @Version: 0.0.1
 */
@Data
public class LoginRespVO {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "凭证")
    private String sessionId;
}
