package com.jun.plugin.system.common.aop.aspect;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jun.plugin.module.ext.entity.SysDataPower;
import com.jun.plugin.system.entity.SysUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoAndPowers {
//    private Long userId;
//    private String userName;
//    private String nickName;
    private Long deptId;
//    private String deptName;
//    private Map metaData;
//    private String deptTableAlias;
//    private String userTableAlias;
    private String sql;
    private SysUser user;
    private Set<Long> deptIds;
    private List<SysDataPower> userDataPowers;

    public Boolean isSuperAdmin() {
        return user.getId() == "1";
    }

    
}
