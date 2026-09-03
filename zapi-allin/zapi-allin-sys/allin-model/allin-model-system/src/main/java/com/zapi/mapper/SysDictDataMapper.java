package com.zapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.entity.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 字典数据Mapper接口
 */
@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    @Select("SELECT * FROM sys_dict_data WHERE dict_type = #{dictType} AND status = '0' AND deleted = 0 ORDER BY dict_sort ASC")
    List<SysDictData> selectDictDataByType(@Param("dictType") String dictType);
}