package com.zapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.Role;
import com.zapi.mapper.RoleMapper;
import com.zapi.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * 角色Service实现类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
} 