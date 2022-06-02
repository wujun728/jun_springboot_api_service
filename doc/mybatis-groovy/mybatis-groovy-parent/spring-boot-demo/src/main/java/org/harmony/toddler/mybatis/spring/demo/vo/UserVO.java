package org.harmony.toddler.mybatis.spring.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * User
 */
@Data
@ApiModel
public class UserVO {

    @ApiModelProperty("UserID")
    private Long id;

    @ApiModelProperty("UserName")
    private String name;
}
