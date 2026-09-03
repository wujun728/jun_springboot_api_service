package com.zy.base.service.impl;

import com.zy.base.domain.AppManager;
import com.zy.base.domain.DbConfig;
import com.zy.base.mapper.DbConfigMapper;
import com.zy.base.service.IAppManagerService;
import com.zy.base.service.IDbConfigService;
import com.zy.common.core.redis.RedisCache;
import com.zy.common.exception.ServiceException;
import com.zy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据源配置Service业务层处理
 *
 * @author javachen
 * @date 2022-09-26
 */
@Service
public class DbConfigServiceImpl implements IDbConfigService
{
    @Autowired
    private DbConfigMapper dbConfigMapper;

    @Autowired
    private IAppManagerService appManagerService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询数据源配置
     *
     * @param id 数据源配置主键
     * @return 数据源配置
     */
    @Override
    public DbConfig selectDbConfigById(Long id)
    {
        return dbConfigMapper.selectDbConfigById(id);
    }

    /**
     * 查询数据源配置列表
     *
     * @param dbConfig 数据源配置
     * @return 数据源配置
     */
    @Override
    public List<DbConfig> selectDbConfigList(DbConfig dbConfig)
    {
        return dbConfigMapper.selectDbConfigList(dbConfig);
    }

    /**
     * 新增数据源配置
     *
     * @param dbConfig 数据源配置
     * @return 结果
     */
    @Override
    public int insertDbConfig(DbConfig dbConfig)
    {
        DbConfig dbConfigTemp = new DbConfig();
        dbConfigTemp.setName(dbConfig.getName());
        List<DbConfig> dbConfigList = dbConfigMapper.selectDbConfigListByName(dbConfigTemp);
        if(dbConfigList.size()>0){
            throw new ServiceException("数据库名称不能重复");
        }

        dbConfig.setCreateTime(DateUtils.getNowDate());
        dbConfig.setUpdateTime(DateUtils.getNowDate());
        return dbConfigMapper.insertDbConfig(dbConfig);
    }

    /**
     * 修改数据源配置
     *
     * @param dbConfig 数据源配置
     * @return 结果
     */
    @Override
    public int updateDbConfig(DbConfig dbConfig)
    {
        DbConfig dbConfigTemp = new DbConfig();
        dbConfigTemp.setName(dbConfig.getName());
        List<DbConfig> dbConfigList = dbConfigMapper.selectDbConfigListByName(dbConfigTemp);
        if(dbConfigList.size()>1){
            throw new ServiceException("数据库名称不能重复");
        }

        dbConfig.setUpdateTime(DateUtils.getNowDate());

        redisCache.deleteObject("DbConfig:" + dbConfig.getId());
        return dbConfigMapper.updateDbConfig(dbConfig);

    }

    /**
     * 批量删除数据源配置
     *
     * @param ids 需要删除的数据源配置主键
     * @return 结果
     */
    @Override
    public int deleteDbConfigByIds(Long[] ids)
    {
        AppManager appManagerTemp = new AppManager();
        appManagerTemp.setDbConfigId(ids[0]);
        List<AppManager> appManagerList = appManagerService.selectAppManagerList(appManagerTemp);
        if(appManagerList.size()>0){
            throw new ServiceException("当前数据源正在使用");
        }


        return dbConfigMapper.deleteDbConfigByIds(ids);
    }

    /**
     * 删除数据源配置信息
     *
     * @param id 数据源配置主键
     * @return 结果
     */
    @Override
    public int deleteDbConfigById(Long id)
    {
        return dbConfigMapper.deleteDbConfigById(id);
    }

    /**
     * 查询所有数据源配置
     */
    @Override
    public List<DbConfig> findAll(){
        return dbConfigMapper.findAll();
    }

    /**
     * 根据id进行查询,并对结果进行缓存
     */
    @Override
    public DbConfig findByIdCache(Long id){
        DbConfig dbConfig = redisCache.getCacheObject("DbConfig:" + id);
        if(null!=dbConfig){
            return dbConfig;
        }

        dbConfig = dbConfigMapper.selectDbConfigById(id);
        redisCache.setCacheObject("DbConfig:" + id,dbConfig);

        return dbConfig;
    }

    /**
     * 更新数据库连接结果
     */
    @Override
    public DbConfig updateCheckStatus(DbConfig dbConfig){
        dbConfigMapper.updateDbConfig(dbConfig);
        return dbConfig;
    }

}
