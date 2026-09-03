package com.zapi.dataset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.dataset.model.entity.DataSourceEntity;

import java.util.List;

// 定义服务层接口，扩展IService
public interface DataSourceService extends IService<DataSourceEntity> {
    // 获取所有数据源列表
    List<DataSourceEntity> getAllDataSources();

    // 根据ID获取特定的数据源信息
    DataSourceEntity getDataSourceById(Integer id);

    // 保存新的数据源信息
    boolean saveDataSource(DataSourceEntity dataSource);

    // 更新现有数据源的信息
    boolean updateDataSource(DataSourceEntity dataSource);

    // 删除指定ID的数据源
    boolean deleteDataSource(Integer id);
}
