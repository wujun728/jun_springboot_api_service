package com.zapi.dataset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.dataset.model.entity.DataSet;
import org.apache.ibatis.annotations.Param;

public interface DataSetMapper extends BaseMapper<DataSet> {

    Page<DataSet> selectPage(Page<DataSet> page, @Param("name") String name);
}