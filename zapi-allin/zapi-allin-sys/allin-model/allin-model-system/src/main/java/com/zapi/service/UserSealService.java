package com.zapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.entity.UserSeal;

import java.util.List;

/**
 * 用户印章Service接口
 */
public interface UserSealService extends IService<UserSeal> {

    /**
     * 分页查询用户印章
     */
    IPage<UserSeal> pageUserSeals(Page<UserSeal> page, Long userId, String sealName, Integer status);

    /**
     * 获取用户所有印章
     */
    List<UserSeal> getUserSeals(Long userId);

    /**
     * 获取用户启用的印章
     */
    List<UserSeal> getEnabledSeals(Long userId);

    /**
     * 创建印章
     */
    UserSeal createSeal(Long userId, String sealName, String sealType, String centerText, Integer sortOrder, String remark);

    /**
     * 更新印章状态
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 更新印章排序
     */
    boolean updateSortOrder(Long id, Integer sortOrder);

    /**
     * 删除印章
     */
    boolean deleteSeal(Long id);

    /**
     * 批量删除印章
     */
    boolean batchDelete(List<Long> ids);
}

