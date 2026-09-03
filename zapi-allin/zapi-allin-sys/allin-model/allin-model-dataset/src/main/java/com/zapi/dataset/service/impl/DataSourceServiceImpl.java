package com.zapi.dataset.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.dataset.mapper.DataSourceMapper;
import com.zapi.dataset.model.entity.DataSourceEntity;
import com.zapi.dataset.service.DataSourceService;
import org.springframework.stereotype.Service;
import java.util.List;

// 实现服务层接口
@Service
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSourceEntity> implements DataSourceService {

    @Override
    public List<DataSourceEntity> getAllDataSources() {
        return this.list();
    }

    @Override
    public DataSourceEntity getDataSourceById(Integer id) {
        return this.getById(id);
    }

    @Override
    public boolean saveDataSource(DataSourceEntity dataSource) {
        return this.save(dataSource);
    }

    @Override
    public boolean updateDataSource(DataSourceEntity dataSource) {
        return this.updateById(dataSource);
    }

    @Override
    public boolean deleteDataSource(Integer id) {
        return this.removeById(id);
    }
}