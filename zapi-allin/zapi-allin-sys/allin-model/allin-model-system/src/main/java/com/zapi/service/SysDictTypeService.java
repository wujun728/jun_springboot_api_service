package com.zapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.entity.SysDictType;

import java.util.List;

/**
 * 字典类型Service接口
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /**
     * 根据字典类型查询字典类型信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    SysDictType selectDictTypeByType(String dictType);

    /**
     * 校验字典类型是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    boolean checkDictTypeUnique(SysDictType dictType);

    /**
     * 查询所有字典类型
     *
     * @return 字典类型列表
     */
    List<SysDictType> selectDictTypeAll();
}