package com.zy.base.mapper;

import java.util.List;
import com.zy.base.domain.DbConfig;

/**
 * 数据源配置Mapper接口
 *
 * @author javachen
 * @date 2022-09-26
 */
public interface DbConfigMapper
{
    /**
     * 查询数据源配置
     *
     * @param id 数据源配置主键
     * @return 数据源配置
     */
    public DbConfig selectDbConfigById(Long id);

    /**
     * 查询数据源配置列表
     *
     * @param dbConfig 数据源配置
     * @return 数据源配置集合
     */
    public List<DbConfig> selectDbConfigList(DbConfig dbConfig);

    /**
     * 精确查询DbConfig中是否有同名数据库名称
     * @param dbConfig 数据源配置
     * @return 数据源配置集合
     */
    public List<DbConfig> selectDbConfigListByName(DbConfig dbConfig);

    /**
     * 新增数据源配置
     *
     * @param dbConfig 数据源配置
     * @return 结果
     */
    public int insertDbConfig(DbConfig dbConfig);

    /**
     * 修改数据源配置
     *
     * @param dbConfig 数据源配置
     * @return 结果
     */
    public int updateDbConfig(DbConfig dbConfig);

    /**
     * 删除数据源配置
     *
     * @param id 数据源配置主键
     * @return 结果
     */
    public int deleteDbConfigById(Long id);

    /**
     * 批量删除数据源配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDbConfigByIds(Long[] ids);

    /**
     * 查询所有数据源配置
     */
    public List<DbConfig> findAll();

}
