package com.zy.base.mapper;

import java.util.List;
import com.zy.base.domain.AppManager;

/**
 * 应用管理Mapper接口
 *
 * @author ruoyi
 * @date 2022-09-26
 */
public interface AppManagerMapper
{
    /**
     * 查询应用管理
     *
     * @param id 应用管理主键
     * @return 应用管理
     */
    public AppManager selectAppManagerById(Long id);

    /**
     * 查询应用管理列表
     *
     * @param appManager 应用管理
     * @return 应用管理集合
     */
    public List<AppManager> selectAppManagerList(AppManager appManager);

     /**
     * 精确查询AppManager中是否有同名应用名称和应用前缀
     * @param appManager 数据源配置
     * @return 数据源配置集合
     */
    public List<AppManager> selectAppManagerListByNamePrefix(AppManager appManager);

    /**
     * 新增应用管理
     *
     * @param appManager 应用管理
     * @return 结果
     */
    public int insertAppManager(AppManager appManager);

    /**
     * 修改应用管理
     *
     * @param appManager 应用管理
     * @return 结果
     */
    public int updateAppManager(AppManager appManager);

    /**
     * 删除应用管理
     *
     * @param id 应用管理主键
     * @return 结果
     */
    public int deleteAppManagerById(Long id);

    /**
     * 批量删除应用管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAppManagerByIds(Long[] ids);

    /**
     * 查询所有应用信息
     * @return 结果
     */
    public List<AppManager> findAll();

}
