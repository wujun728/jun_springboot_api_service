package com.jun.plugin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jun.plugin.entity.SysDictDetailEntity;

/**
 * 数据字典 服务类
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
public interface SysDictDetailService extends IService<SysDictDetailEntity> {

    /**
     * 分页
     *
     * @param page   page
     * @param dictId dictId
     * @return IPage
     */
    IPage<SysDictDetailEntity> listByPage(Page<SysDictDetailEntity> page, String dictId);
}

