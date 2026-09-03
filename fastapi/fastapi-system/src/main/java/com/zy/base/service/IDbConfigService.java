package com.zy.base.service;

import java.util.List;
import com.zy.base.domain.DbConfig;
import com.zy.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 数据源配置Service接口
 *
 * @author javachen
 * @date 2022-09-26
 */
public interface IDbConfigService
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
     * 批量删除数据源配置
     *
     * @param ids 需要删除的数据源配置主键集合
     * @return 结果
     */
    public int deleteDbConfigByIds(Long[] ids);

    /**
     * 删除数据源配置信息
     *
     * @param id 数据源配置主键
     * @return 结果
     */
    public int deleteDbConfigById(Long id);

    /**
     * 查询所有数据源配置
     */
    public List<DbConfig> findAll();

    /**
     * 根据id进行查询,并对结果进行缓存
     */
    public DbConfig findByIdCache(Long id);

    /**
     * 更新数据库连接结果
     */
    public DbConfig updateCheckStatus(DbConfig dbConfig);

}
