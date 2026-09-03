package com.zy.base.service;

import java.util.List;
import com.zy.base.domain.AppManager;
import com.zy.base.domain.DbConfig;

/**
 * 应用管理Service接口
 *
 * @author ruoyi
 * @date 2022-09-26
 */
public interface IAppManagerService {
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
     * 批量删除应用管理
     *
     * @param ids 需要删除的应用管理主键集合
     * @return 结果
     */
    public int deleteAppManagerByIds(Long[] ids);

    /**
     * 删除应用管理信息
     *
     * @param id 应用管理主键
     * @return 结果
     */
    public int deleteAppManagerById(Long id);

    /**
     * 查询所有应用信息
     * @return 结果
     */
    public List<AppManager> findAll();

    /**
     * 根据id进行查询,并对结果进行缓存
     */
    public AppManager findByIdCache(Long id);

}
