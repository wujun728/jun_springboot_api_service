package com.gitee.sqlrest.persistence.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gitee.sqlrest.persistence.entity.ApiContextEntity;
import com.gitee.sqlrest.persistence.mapper.ApiContextMapper;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ApiContextDao {

  @Resource
  private ApiContextMapper apiContextMapper;

  @Transactional(rollbackFor = Exception.class)
  public void batchInsert(List<ApiContextEntity> records) {
    if (null != records && records.size() > 0) {
      records.forEach(apiContextMapper::insert);
    }
  }

  public List<ApiContextEntity> getByApiConfigId(Long apiId) {
    QueryWrapper<ApiContextEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(ApiContextEntity::getApiId, apiId);
    return apiContextMapper.selectList(queryWrapper);
  }

  public void deleteByApiConfigId(Long apiId) {
    QueryWrapper<ApiContextEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(ApiContextEntity::getApiId, apiId);
    apiContextMapper.delete(queryWrapper);
  }

}
