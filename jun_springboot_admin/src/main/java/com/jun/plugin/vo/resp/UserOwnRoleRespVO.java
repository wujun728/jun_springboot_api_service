package com.jun.plugin.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

import com.jun.plugin.entity.SysRole;

/**
 * UserOwnRoleRespVO
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
public class UserOwnRoleRespVO {
    @ApiModelProperty("所有角色集合")
    private List<SysRole> allRole;
    @ApiModelProperty(value = "用户所拥有角色集合")
    private List<String> ownRoles;
}
