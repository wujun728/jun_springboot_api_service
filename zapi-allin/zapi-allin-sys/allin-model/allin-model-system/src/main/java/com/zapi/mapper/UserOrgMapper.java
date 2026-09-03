package com.zapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.entity.UserOrg;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户组织关联 Mapper 接口
 */
@Mapper
public interface UserOrgMapper extends BaseMapper<UserOrg> {
}
