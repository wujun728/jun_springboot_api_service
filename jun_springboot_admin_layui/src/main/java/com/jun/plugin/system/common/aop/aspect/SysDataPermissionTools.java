package com.jun.plugin.system.common.aop.aspect;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jun.plugin.system.common.exception.BusinessException;
import com.jun.plugin.system.common.utils.Constant;
import com.jun.plugin.system.entity.BaseEntity;
import com.jun.plugin.system.entity.SysDept;
import com.jun.plugin.system.entity.SysRole;
import com.jun.plugin.system.entity.SysRoleDeptEntity;
import com.jun.plugin.system.entity.SysUser;
import com.jun.plugin.system.service.DeptService;
import com.jun.plugin.system.service.HttpSessionService;
import com.jun.plugin.system.service.RoleService;
import com.jun.plugin.system.service.SysRoleDeptService;
import com.jun.plugin.system.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SysDataPermissionTools {
    @Resource
    HttpSessionService sessionService;
    @Resource
    RoleService roleService;
    @Resource
    SysRoleDeptService sysRoleDeptService;
    @Resource
    DeptService deptService;
    @Resource
    UserService userService;
    
    public  UserInfoAndPowers getCurrentUserInfo() {
    	//获取当前登陆人
        String userId = sessionService.getCurrentUserId();
        //获取当前登陆人角色, 如果无角色, 那么不限制
        List<SysRole> sysRoles = roleService.getRoleInfoByUserId(userId);
        if (CollectionUtils.isEmpty(sysRoles) || sysRoles.size() == 0) {
//            return;
        }
        //角色未配置数据权限范围, 那么不限制
        List<SysRole> list = sysRoles.parallelStream().filter(one -> null != one.getDataScope()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list) || list.size() == 0) {
//            return;
        }
        //如果存在某角色配置了全部范围， 那么不限制
        if (list.stream().anyMatch(sysRole -> Constant.DATA_SCOPE_ALL.equals(sysRole.getDataScope()))) {
//            return;
        }
        //获取绑定的人
        List<String> userIds = this.getUserIdsByRoles(list, userId);
//        Object params = joinPoint.getArgs()[0];
//        if (params instanceof BaseEntity) {
//            BaseEntity baseEntity = (BaseEntity) params;
//            baseEntity.setCreateIds(userIds);
//        }
        
      //本人
        SysUser sysUser = userService.getById(userId);
        //本部门
        SysDept sysDept = deptService.getById(sysUser.getDeptId());
        //部门ids， 定义哪些部门最终拥有权限查看
        LinkedList<Object> deptList = new LinkedList<>();
        //用户ids，定义列表中哪些人创建的可查看
        LinkedList<Object> userIdList = new LinkedList<>();
        //根据数据权限范围分组， 不同的数据范围不同的逻辑处理
        Map<Integer, List<SysRole>> dataScopeMap = sysRoles.parallelStream().collect(Collectors.groupingBy(SysRole::getDataScope));
        dataScopeMap.forEach((k, v) -> {
            if (Constant.DATA_SCOPE_CUSTOM.equals(k)) {
                //自定义
                //根据角色id，获取所有自定义关联的部门id
                QueryWrapper<SysRoleDeptEntity> queryWrapper = Wrappers.<SysRoleDeptEntity>query().select("dept_id").in("role_id", v.parallelStream().map(SysRole::getId).collect(Collectors.toList()));
                deptList.addAll(sysRoleDeptService.listObjs(queryWrapper));
            } else if (Constant.DATA_SCOPE_DEPT_AND_CHILD.equals(k)) {
                //本部门及以下
                if (sysDept != null && StringUtils.isNotBlank(sysDept.getDeptNo())) {
                    //获取本部门以下所有关联的部门
                    QueryWrapper<SysDept> queryWrapper = Wrappers.<SysDept>query().select("id").like("relation_code", sysDept.getDeptNo());
                    deptList.addAll(deptService.listObjs(queryWrapper));
                }
            } else if (Constant.DATA_SCOPE_DEPT.equals(k)) {
                //本部门
                if (sysDept != null && StringUtils.isNotBlank(sysDept.getId())) {
                    deptList.add(sysDept.getId());
                }
            } else if (Constant.DATA_SCOPE_DEPT_SELF.equals(k)) {
                //自己
                userIdList.add(userId);
            }
        });
        if (!CollectionUtils.isEmpty(deptList)) {
            QueryWrapper<SysUser> queryWrapper = Wrappers.<SysUser>query().select("id").in("dept_id", deptList);
            userIdList.addAll(userService.listObjs(queryWrapper));
        }
        //如果配置了角色数据范围， 最终没有查到userId， 那么返回无数据
        if (CollectionUtils.isEmpty(userIdList)) {
            throw new BusinessException("无数据");
        }
        userIdList.parallelStream().map(Object::toString).collect(Collectors.toList());
        
        
        // --------------------------------------------
        UserInfoAndPowers userInfoAndPowers = null;
        try {
            userInfoAndPowers = new UserInfoAndPowers();
            userInfoAndPowers.setUser(userService.getById(userId));
            userInfoAndPowers.setDeptIds(null);;
            userInfoAndPowers.setSql(null);
            userInfoAndPowers.setUserDataPowers(userService.getById(userId).getUserDataPowers());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return userInfoAndPowers;
    }
    
 
 

    /**
     * 获取最终的用户id
     *
     * @param sysRoles 角色
     * @param userId   当前用户id
     * @return 用户id集合
     */
    private List<String> getUserIdsByRoles(List<SysRole> sysRoles, String userId) {
        //本人
        SysUser sysUser = userService.getById(userId);
        //本部门
        SysDept sysDept = deptService.getById(sysUser.getDeptId());
        //部门ids， 定义哪些部门最终拥有权限查看
        LinkedList<Object> deptList = new LinkedList<>();
        //用户ids，定义列表中哪些人创建的可查看
        LinkedList<Object> userIdList = new LinkedList<>();
        //根据数据权限范围分组， 不同的数据范围不同的逻辑处理
        Map<Integer, List<SysRole>> dataScopeMap = sysRoles.parallelStream().collect(Collectors.groupingBy(SysRole::getDataScope));
        dataScopeMap.forEach((k, v) -> {
            if (Constant.DATA_SCOPE_CUSTOM.equals(k)) {
                //自定义
                //根据角色id，获取所有自定义关联的部门id
                QueryWrapper<SysRoleDeptEntity> queryWrapper = Wrappers.<SysRoleDeptEntity>query().select("dept_id").in("role_id", v.parallelStream().map(SysRole::getId).collect(Collectors.toList()));
                deptList.addAll(sysRoleDeptService.listObjs(queryWrapper));
            } else if (Constant.DATA_SCOPE_DEPT_AND_CHILD.equals(k)) {
                //本部门及以下
                if (sysDept != null && StringUtils.isNotBlank(sysDept.getDeptNo())) {
                    //获取本部门以下所有关联的部门
                    QueryWrapper<SysDept> queryWrapper = Wrappers.<SysDept>query().select("id").like("relation_code", sysDept.getDeptNo());
                    deptList.addAll(deptService.listObjs(queryWrapper));
                }
            } else if (Constant.DATA_SCOPE_DEPT.equals(k)) {
                //本部门
                if (sysDept != null && StringUtils.isNotBlank(sysDept.getId())) {
                    deptList.add(sysDept.getId());
                }
            } else if (Constant.DATA_SCOPE_DEPT_SELF.equals(k)) {
                //自己
                userIdList.add(userId);
            }
        });
        if (!CollectionUtils.isEmpty(deptList)) {
            QueryWrapper<SysUser> queryWrapper = Wrappers.<SysUser>query().select("id").in("dept_id", deptList);
            userIdList.addAll(userService.listObjs(queryWrapper));
        }
        //如果配置了角色数据范围， 最终没有查到userId， 那么返回无数据
        if (CollectionUtils.isEmpty(userIdList)) {
            throw new BusinessException("无数据");
        }
        return userIdList.parallelStream().map(Object::toString).collect(Collectors.toList());
    }

}
