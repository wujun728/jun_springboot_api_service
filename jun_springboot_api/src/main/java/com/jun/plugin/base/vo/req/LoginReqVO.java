package com.jun.plugin.base.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: LoginReqVO
 * TODO:类文件简单描述
 * @author Wujun
 * @Version: 0.0.1
 */
@Data
public class LoginReqVO {

    @ApiModelProperty(value = "账号")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;
}
