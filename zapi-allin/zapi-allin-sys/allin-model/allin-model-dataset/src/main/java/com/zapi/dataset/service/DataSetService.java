package com.zapi.dataset.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.dataset.model.entity.DataSet;
import com.zapi.dataset.model.vo.DataSetVO;

import java.sql.SQLException;

public interface DataSetService extends IService<DataSet> {
    Page<DataSet> pageList(Integer pageNum, Integer pageSize, String name);

    DataSetVO getDataSetInfoById(DataSet dataset) throws Exception;
}