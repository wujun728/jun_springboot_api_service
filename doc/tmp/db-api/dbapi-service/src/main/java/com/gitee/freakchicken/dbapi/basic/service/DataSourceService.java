package com.gitee.freakchicken.dbapi.basic.service;

import com.gitee.freakchicken.dbapi.basic.dao.ApiConfigMapper;
import com.gitee.freakchicken.dbapi.basic.dao.DataSourceMapper;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;
import com.gitee.freakchicken.dbapi.basic.util.DESUtils;
import com.gitee.freakchicken.dbapi.basic.util.PoolManager;
import com.gitee.freakchicken.dbapi.basic.util.UUIDUtil;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: dbApi
 * @description:
 * @author:
 * @create: 2021-01-20 10:43
 **/
@Service
@Slf4j
public class DataSourceService {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    MetaDataCacheManager metaDataCacheManager;

    @Autowired
    DataSourceMapper dataSourceMapper;
    @Autowired
    ApiConfigMapper apiConfigMapper;

    @Transactional
    public void add(DataSource dataSource) {
        dataSource.setId(UUIDUtil.id());
        dataSource.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        dataSource.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        //新增数据源对密码加密
        try {
            dataSource.setPassword(DESUtils.encrypt(dataSource.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataSourceMapper.insert(dataSource);
    }

    //    @CacheEvict(value = "datasource", key = "#dataSource.id")
    @Transactional
    public void update(DataSource dataSource) {
        dataSource.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //如果修改了密码, 需要对密码加密
        if (dataSource.isEdit_password()){
            try {
                dataSource.setPassword(DESUtils.encrypt(dataSource.getPassword()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dataSourceMapper.updateById(dataSource);
        PoolManager.removeJdbcConnectionPool(dataSource.getId());
        cacheManager.getCache("datasource").evictIfPresent(dataSource.getId());

        //如果是集群模式，清除每个apiServer节点内的元数据ehcache缓存
        metaDataCacheManager.cleanDatasourceMetaCacheIfCluster(dataSource.getId());
    }

    //    @CacheEvict(value = "datasource", key = "#id")
    @Transactional
    public ResponseDto delete(String id) {
        int i = apiConfigMapper.countByDatasoure(id);
        if (i == 0) {
            dataSourceMapper.deleteById(id);

            PoolManager.removeJdbcConnectionPool(id);
            cacheManager.getCache("datasource").evictIfPresent(id);

            //如果是集群模式，清除每个apiServer节点内的元数据ehcache缓存

            metaDataCacheManager.cleanDatasourceMetaCacheIfCluster(id);
            return ResponseDto.successWithMsg("delete success");
        } else {
            return ResponseDto.fail("datasource has been used, can not delete");
        }
    }

    @Cacheable(value = "datasource", key = "#id", unless = "#result == null")
    public DataSource detail(String id) {
        DataSource dataSource = dataSourceMapper.selectById(id);
        return dataSource;
    }

    public List<DataSource> getAll() {
        List<DataSource> list = dataSourceMapper.selectList(null);
        List<DataSource> collect = list.stream().sorted(Comparator.comparing(DataSource::getUpdateTime).reversed()).collect(Collectors.toList());
        return collect;
    }

    public String getDBType(Integer id) {
        return dataSourceMapper.selectById(id).getType();
    }

    public List<DataSource> selectBatch(List<String> ids) {
        List<DataSource> dataSources = dataSourceMapper.selectBatchIds(ids);
        return dataSources;
    }

    @Transactional
    public void insertBatch(List<DataSource> list) {
        list.forEach(t -> {
            t.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            dataSourceMapper.insert(t);
        });
    }
}
