package com.zy.base.service.impl;

import com.zy.base.domain.AppManager;
import com.zy.base.mapper.AppManagerMapper;
import com.zy.base.service.IAppManagerService;
import com.zy.common.core.redis.RedisCache;
import com.zy.common.exception.ServiceException;
import com.zy.common.utils.DateUtils;
import com.zy.imanager.domain.SelectIntfaceManager;
import com.zy.imanager.service.ISelectIntfaceManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应用管理Service业务层处理
 *
 * @author ruoyi
 * @date 2022-09-26
 */
@Service
public class AppManagerServiceImpl implements IAppManagerService
{
    @Autowired
    private AppManagerMapper appManagerMapper;

    @Autowired
    private ISelectIntfaceManagerService selectIntfaceManagerService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询应用管理
     *
     * @param id 应用管理主键
     * @return 应用管理
     */
    @Override
    public AppManager selectAppManagerById(Long id)
    {
        return appManagerMapper.selectAppManagerById(id);
    }

    /**
     * 查询应用管理列表
     *
     * @param appManager 应用管理
     * @return 应用管理
     */
    @Override
    public List<AppManager> selectAppManagerList(AppManager appManager)
    {
        return appManagerMapper.selectAppManagerList(appManager);
    }

    /**
     * 新增应用管理
     *
     * @param appManager 应用管理
     * @return 结果
     */
    @Override
    public int insertAppManager(AppManager appManager)
    {
        AppManager appManagerTemp = new AppManager();
        appManagerTemp.setName(appManager.getName());
        List<AppManager> appManagerList = appManagerMapper.selectAppManagerListByNamePrefix(appManagerTemp);
        if(appManagerList.size()>0){
            throw new ServiceException("应用名称不能重复");
        }

        appManagerTemp = new AppManager();
        appManagerTemp.setPrefix(appManager.getPrefix());
        appManagerList = appManagerMapper.selectAppManagerListByNamePrefix(appManagerTemp);
        if(appManagerList.size()>0){
            throw new ServiceException("应用前缀不能重复");
        }

        appManager.setCreateTime(DateUtils.getNowDate());
        appManager.setUpdateTime(DateUtils.getNowDate());
        return appManagerMapper.insertAppManager(appManager);
    }

    /**
     * 修改应用管理
     *
     * @param appManager 应用管理
     * @return 结果
     */
    @Override
    public int updateAppManager(AppManager appManager)
    {
        AppManager appManagerTemp = new AppManager();
        appManagerTemp.setName(appManager.getName());
        List<AppManager> dbConfigList = appManagerMapper.selectAppManagerListByNamePrefix(appManagerTemp);
        if(dbConfigList.size()>1){
            throw new ServiceException("应用名称不能重复");
        }

        appManagerTemp = new AppManager();
        appManagerTemp.setPrefix(appManager.getPrefix());
        dbConfigList = appManagerMapper.selectAppManagerListByNamePrefix(appManagerTemp);
        if(dbConfigList.size()>1){
            throw new ServiceException("应用前缀不能重复");
        }

        appManager.setUpdateTime(DateUtils.getNowDate());

        redisCache.deleteObject("AppManager:" + appManager.getId());

        int resultInt = appManagerMapper.updateAppManager(appManager);

        selectIntfaceManagerService.updateAppManagerInfo(appManager);

        return resultInt;
    }

    /**
     * 批量删除应用管理
     *
     * @param ids 需要删除的应用管理主键
     * @return 结果
     */
    @Override
    public int deleteAppManagerByIds(Long[] ids)
    {

        SelectIntfaceManager selectIntfaceManagerTemp = new SelectIntfaceManager();
        selectIntfaceManagerTemp.setAppManagerId(ids[0]);

        List<SelectIntfaceManager> selectIntfaceManagerList = selectIntfaceManagerService.selectSelectIntfaceManagerList(selectIntfaceManagerTemp);

        if(selectIntfaceManagerList.size()>0){
            throw new ServiceException("当前应用正在使用");
        }

        return appManagerMapper.deleteAppManagerByIds(ids);
    }

    /**
     * 删除应用管理信息
     *
     * @param id 应用管理主键
     * @return 结果
     */
    @Override
    public int deleteAppManagerById(Long id)
    {
        return appManagerMapper.deleteAppManagerById(id);
    }

    /**
     * 查询所有应用信息
     * @return 结果
     */
    @Override
    public List<AppManager> findAll()
    {
        return appManagerMapper.findAll();
    }

    /**
     * 根据id进行查询,并对结果进行缓存
     */
    @Override
    public AppManager findByIdCache(Long id){
            AppManager appManager = redisCache.getCacheObject("AppManager:" + id);
            if(null!=appManager){
                return appManager;
            }

            appManager = appManagerMapper.selectAppManagerById(id);
            redisCache.setCacheObject("AppManager:" + id,appManager);

            return appManager;
    }

}
