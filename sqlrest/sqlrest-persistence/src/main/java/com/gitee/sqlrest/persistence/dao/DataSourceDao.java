package com.gitee.sqlrest.persistence.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sqlrest.persistence.entity.DataSourceEntity;
import com.gitee.sqlrest.persistence.mapper.DataSourceMapper;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class DataSourceDao {

  @Resource
  private DataSourceMapper dataSourceMapper;

  public void insert(DataSourceEntity dataSourceEntity) {
    dataSourceMapper.insert(dataSourceEntity);
  }

  public DataSourceEntity getById(Long id) {
    return dataSourceMapper.selectById(id);
  }

  public DataSourceEntity getByName(String name) {
    QueryWrapper<DataSourceEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(DataSourceEntity::getName, name);
    return dataSourceMapper.selectOne(queryWrapper);
  }

  public List<DataSourceEntity> listAll(String searchText) {
    return dataSourceMapper.selectList(
        Wrappers.<DataSourceEntity>lambdaQuery()
            .like(StringUtils.hasText(searchText), DataSourceEntity::getName, searchText)
            .orderByDesc(DataSourceEntity::getCreateTime)
    );
  }

  public void updateById(DataSourceEntity databaseConnectionEntity) {
    dataSourceMapper.updateById(databaseConnectionEntity);
  }

  public void deleteById(Long id) {
    dataSourceMapper.deleteById(id);
  }

  public int getTotalCount() {
    return dataSourceMapper.selectCount(null).intValue();
  }
}
