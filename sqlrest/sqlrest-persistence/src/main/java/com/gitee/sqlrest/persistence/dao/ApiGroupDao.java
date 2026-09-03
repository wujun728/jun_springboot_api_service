package com.gitee.sqlrest.persistence.dao;

import com.gitee.sqlrest.persistence.entity.ApiGroupEntity;
import com.gitee.sqlrest.persistence.mapper.ApiGroupMapper;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class ApiGroupDao {

  @Resource
  private ApiGroupMapper apiGroupMapper;

  public void insert(ApiGroupEntity entity) {
    apiGroupMapper.insert(entity);
  }

  public ApiGroupEntity getById(Long id) {
    return apiGroupMapper.selectById(id);
  }

  public List<ApiGroupEntity> listAll() {
    return apiGroupMapper.selectList(null);
  }

  public void updateById(ApiGroupEntity entity) {
    apiGroupMapper.updateById(entity);
  }

  public void deleteById(Long id) {
    apiGroupMapper.deleteById(id);
  }
}
