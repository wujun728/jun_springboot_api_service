package com.zapi.dataset.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.dataset.config.DataSourceConfig;
import com.zapi.dataset.mapper.DataSetMapper;
import com.zapi.dataset.model.entity.DataSet;
import com.zapi.dataset.model.entity.DataSourceEntity;
import com.zapi.dataset.model.vo.DataSetVO;
import com.zapi.dataset.model.vo.FieldInfoVO;
import com.zapi.dataset.service.DataSetService;
import com.zapi.dataset.service.DataSourceService;
import com.zapi.dataset.support.service.DatabaseMetaService;
import com.zapi.dataset.support.service.DatabaseMetaServiceFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

@Service
public class DataSetServiceImpl extends ServiceImpl<DataSetMapper, DataSet> implements DataSetService {

    @Resource
    private DataSourceService dataSourceService;

    @Override
    public Page<DataSet> pageList(Integer pageNum, Integer pageSize, String name) {
        Page<DataSet> page = new Page<>(pageNum, pageSize);
        return this.baseMapper.selectPage(page, name);
    }

    @Override
    public DataSetVO getDataSetInfoById(DataSet dataset) throws Exception {
        // 通过 baseMapper 根据数据集的 ID 查询数据集信息
        DataSet info = this.baseMapper.selectById(dataset.getId());
        if (info == null) {
            // 如果未查询到数据集信息，返回 null
            return null;
        }
        // 通过 dataSourceService 根据数据集的数据源 ID 查询数据源信息
        DataSourceEntity dataSource = dataSourceService.getDataSourceById(Math.toIntExact(info.getDataSourceId()));
        if (dataSource == null) {
            // 如果未查询到数据源信息，返回 null
            return null;
        }
        // 根据数据源的数据库类型获取对应的数据库元数据服务
        DatabaseMetaService metaService = DatabaseMetaServiceFactory.getService(dataSource.getDbType());
        // 创建数据源配置对象
        DataSourceConfig config = new DataSourceConfig();
        // 设置数据源的连接 URL
        config.setUrl(dataSource.getUrl());
        // 设置数据源的用户名
        config.setUsername(dataSource.getUsername());
        // 设置数据源的密码
        config.setPassword(dataSource.getPassword());
        // 设置数据源的模式名
        config.setSchema(dataSource.getSchemaName());
        // 设置数据源的表名
        config.setTableName(info.getSourceTable());
        // 通过数据库元数据服务获取表的列信息，并封装到 FieldInfoVO 列表中
        List<FieldInfoVO> fieldInfoVOList = metaService.getTableColumnList(config);
        // 创建 DataSetVO 对象
        DataSetVO dataSetVO = new DataSetVO();
        // 将查询到的数据集信息复制到 DataSetVO 对象中
        BeanUtils.copyProperties(info, dataSetVO);
        // 设置 DataSetVO 对象的字段列表
        dataSetVO.setFields(fieldInfoVOList);
        // 返回封装好的 DataSetVO 对象
        return dataSetVO;
    }
}