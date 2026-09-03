package com.zapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.SysDictType;
import com.zapi.mapper.SysDictTypeMapper;
import com.zapi.service.SysDictTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典类型Service实现类
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    /**
     * 根据字典类型查询字典类型信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeByType(String dictType) {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictType::getDictType, dictType).eq(SysDictType::getDeleted, 0);
        return this.getOne(queryWrapper);
    }

    /**
     * 校验字典类型是否唯一
     *
     * @param dictType 字典类型
     * @return 结果 true-唯一 false-不唯一
     */
    @Override
    public boolean checkDictTypeUnique(SysDictType dictType) {
        Long dictId = dictType.getDictId() == null ? -1L : dictType.getDictId();
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictType::getDictType, dictType.getDictType()).eq(SysDictType::getDeleted, 0);
        SysDictType info = this.getOne(queryWrapper);
        if (info != null && !info.getDictId().equals(dictId)) {
            return false;
        }
        return true;
    }

    /**
     * 查询所有字典类型
     *
     * @return 字典类型列表
     */
    @Override
    public List<SysDictType> selectDictTypeAll() {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictType::getDeleted, 0).eq(SysDictType::getStatus, "0");
        queryWrapper.orderByAsc(SysDictType::getDictId);
        return this.list(queryWrapper);
    }
}