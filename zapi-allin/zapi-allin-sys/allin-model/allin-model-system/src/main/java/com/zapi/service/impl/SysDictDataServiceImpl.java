package com.zapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.SysDictData;
import com.zapi.mapper.SysDictDataMapper;
import com.zapi.service.SysDictDataService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 字典数据Service实现类
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        return baseMapper.selectDictDataByType(dictType);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getDictValue, dictValue)
                .eq(SysDictData::getDeleted, 0);
        SysDictData dictData = this.getOne(queryWrapper);
        if (dictData != null) {
            return dictData.getDictLabel();
        }
        return "";
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode) {
        return this.getById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    @Override
    public boolean deleteDictDataByIds(Long[] dictCodes) {
        return this.removeByIds(Arrays.asList(dictCodes));
    }

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public boolean insertDictData(SysDictData dictData) {
        return this.save(dictData);
    }

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public boolean updateDictData(SysDictData dictData) {
        return this.updateById(dictData);
    }
}