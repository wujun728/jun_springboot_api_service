package com.gitee.sqlrest.persistence.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sqlrest.common.dto.IdWithName;
import com.gitee.sqlrest.persistence.entity.AppClientEntity;
import com.gitee.sqlrest.persistence.entity.ClientGroupEntity;
import com.gitee.sqlrest.persistence.mapper.AppClientMapper;
import com.gitee.sqlrest.persistence.mapper.ClientGroupMapper;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AppClientDao {

  @Resource
  private AppClientMapper appClientMapper;
  @Resource
  private ClientGroupMapper clientAuthMapper;

  public void insert(AppClientEntity appClientEntity) {
    appClientMapper.insert(appClientEntity);
  }

  public List<AppClientEntity> listAll(String searchText) {
    return appClientMapper.selectList(
        Wrappers.<AppClientEntity>lambdaQuery()
            .like(StringUtils.isNotBlank(searchText), AppClientEntity::getName, searchText)
            .orderByDesc(AppClientEntity::getCreateTime)
    );
  }

  public AppClientEntity getById(Long id) {
    return appClientMapper.selectById(id);
  }

  public AppClientEntity getByAppKey(String appKey) {
    QueryWrapper<AppClientEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(AppClientEntity::getAppKey, appKey);
    return appClientMapper.selectOne(queryWrapper);
  }

  public Set<Long> getAuthGroups(String appKey) {
    AppClientEntity appClientEntity = getByAppKey(appKey);
    if (null == appClientEntity) {
      return Collections.emptySet();
    }

    QueryWrapper<ClientGroupEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(ClientGroupEntity::getClientId, appClientEntity.getId());
    return clientAuthMapper.selectList(queryWrapper)
        .stream().map(ClientGroupEntity::getGroupId)
        .collect(Collectors.toSet());
  }

  public void updateTokenByAppKey(String appKey, String token) {
    AppClientEntity updateEntity = new AppClientEntity();
    updateEntity.setAppKey(appKey);
    updateEntity.setAccessToken(token);

    QueryWrapper<AppClientEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(AppClientEntity::getAppKey, appKey);

    appClientMapper.update(updateEntity, queryWrapper);
  }

  public void deleteClientAuthByGroupId(Long groupId) {
    QueryWrapper<ClientGroupEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(ClientGroupEntity::getGroupId, groupId);
    clientAuthMapper.delete(queryWrapper);
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteById(Long id) {
    appClientMapper.deleteById(id);

    QueryWrapper<ClientGroupEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(ClientGroupEntity::getClientId, id);
    clientAuthMapper.delete(queryWrapper);
  }

  @Transactional(rollbackFor = Exception.class)
  public void saveAuthGroup(Long id, List<Long> groupIds) {
    QueryWrapper<ClientGroupEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(ClientGroupEntity::getClientId, id);
    clientAuthMapper.delete(queryWrapper);

    if (null != groupIds && groupIds.size() > 0) {
      groupIds.forEach(gid -> {
        clientAuthMapper.insert(
            ClientGroupEntity.builder().clientId(id).groupId(gid).build()
        );
      });
    }
  }

  public List<IdWithName> getGroupAuth(Long id){
    return clientAuthMapper.getGroupAuth(id);
  }
}
