package com.gitee.sqlrest.persistence.dao;

import com.gitee.sqlrest.persistence.entity.ApiModuleEntity;
import com.gitee.sqlrest.persistence.mapper.ApiModuleMapper;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class ApiModuleDao {

  @Resource
  private ApiModuleMapper apiModuleMapper;

  public void insert(ApiModuleEntity entity) {
    apiModuleMapper.insert(entity);
  }

  public ApiModuleEntity getById(Long id) {
    return apiModuleMapper.selectById(id);
  }

  public List<ApiModuleEntity> listAll() {
    return apiModuleMapper.selectList(null);
  }

  public void updateById(ApiModuleEntity entity) {
    apiModuleMapper.updateById(entity);
  }

  public void deleteById(Long id) {
    apiModuleMapper.deleteById(id);
  }
}
