package com.gitee.sqlrest.persistence.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gitee.sqlrest.persistence.entity.SystemParamEntity;
import com.gitee.sqlrest.persistence.mapper.SystemParamMapper;
import java.util.Objects;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class SystemParamDao {

  @Resource
  private SystemParamMapper systemParamMapper;

  public SystemParamEntity getByParamKey(String paramKey) {
    QueryWrapper<SystemParamEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(SystemParamEntity::getParamKey, paramKey);
    return systemParamMapper.selectOne(queryWrapper);
  }

  public void updateByParamKey(String paramKey, String paramValue) {
    SystemParamEntity entity = getByParamKey(paramKey);
    if (Objects.nonNull(entity)) {
      entity.setParamValue(paramValue);
      systemParamMapper.updateById(entity);
    }
  }

}
