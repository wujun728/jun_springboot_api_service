package com.gitee.sqlrest.persistence.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sqlrest.common.enums.HttpMethodEnum;
import com.gitee.sqlrest.persistence.entity.ApiAssignmentEntity;
import com.gitee.sqlrest.persistence.mapper.ApiAssignmentMapper;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Repository
public class ApiAssignmentDao {

  @Resource
  private ApiAssignmentMapper apiAssignmentMapper;
  @Resource
  private ApiContextDao apiContextDao;

  @Transactional(rollbackFor = Exception.class)
  public void insert(ApiAssignmentEntity apiConfigEntity) {
    apiAssignmentMapper.insert(apiConfigEntity);
    if (null != apiConfigEntity.getContextList() && apiConfigEntity.getContextList().size() > 0) {
      apiConfigEntity.getContextList().forEach(i -> i.setApiId(apiConfigEntity.getId()));
      apiContextDao.batchInsert(apiConfigEntity.getContextList());
    }
  }

  @Transactional(rollbackFor = Exception.class)
  public void update(ApiAssignmentEntity apiConfigEntity) {
    apiAssignmentMapper.updateById(apiConfigEntity);
    apiContextDao.deleteByApiConfigId(apiConfigEntity.getId());
    if (null != apiConfigEntity.getContextList() && apiConfigEntity.getContextList().size() > 0) {
      apiConfigEntity.getContextList().forEach(i -> i.setApiId(apiConfigEntity.getId()));
      apiContextDao.batchInsert(apiConfigEntity.getContextList());
    }
  }

  public ApiAssignmentEntity getById(Long id, boolean withSql) {
    ApiAssignmentEntity apiConfigEntity = apiAssignmentMapper.selectById(id);
    if (null != apiConfigEntity && withSql) {
      apiConfigEntity.setContextList(apiContextDao.getByApiConfigId(apiConfigEntity.getId()));
    }
    return apiConfigEntity;
  }

  public void updateStatus(Long id, Boolean onOff) {
    ApiAssignmentEntity assignmentEntity = apiAssignmentMapper.selectById(id);
    if (null != assignmentEntity) {
      assignmentEntity.setStatus(onOff);
      apiAssignmentMapper.updateById(assignmentEntity);
    }
  }

  public void makeOpen(Long id, Boolean open) {
    ApiAssignmentEntity assignmentEntity = apiAssignmentMapper.selectById(id);
    if (null != assignmentEntity) {
      assignmentEntity.setOpen(open);
      apiAssignmentMapper.updateById(assignmentEntity);
    }
  }

  public ApiAssignmentEntity getByUk(HttpMethodEnum method, String path) {
    return getByUk(method, path, true);
  }

  public ApiAssignmentEntity getByUk(HttpMethodEnum method, String path, boolean withSql) {
    QueryWrapper<ApiAssignmentEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(ApiAssignmentEntity::getMethod, method.name())
        .eq(ApiAssignmentEntity::getPath, path);
    ApiAssignmentEntity apiConfigEntity = apiAssignmentMapper.selectOne(queryWrapper);
    if (withSql) {
      if (null != apiConfigEntity) {
        apiConfigEntity.setContextList(apiContextDao.getByApiConfigId(apiConfigEntity.getId()));
      }
    }
    return apiConfigEntity;
  }

  public List<ApiAssignmentEntity> listAll(Long groupId, Long moduleId, Boolean publish, Boolean open,
      String searchText) {
    return apiAssignmentMapper.selectList(
        Wrappers.<ApiAssignmentEntity>lambdaQuery()
            .eq(Objects.nonNull(groupId), ApiAssignmentEntity::getGroupId, groupId)
            .eq(Objects.nonNull(moduleId), ApiAssignmentEntity::getModuleId, moduleId)
            .eq(Objects.nonNull(publish), ApiAssignmentEntity::getStatus, publish)
            .eq(Objects.nonNull(open), ApiAssignmentEntity::getOpen, open)
            .like(StringUtils.hasText(searchText), ApiAssignmentEntity::getName, searchText)
            .orderByDesc(ApiAssignmentEntity::getCreateTime)
    );
  }

  public List<ApiAssignmentEntity> listAll() {
    return apiAssignmentMapper.selectList(null);
  }

  public List<ApiAssignmentEntity> listFlowControlAll() {
    return apiAssignmentMapper.selectList(
        Wrappers.<ApiAssignmentEntity>lambdaQuery()
            .eq(ApiAssignmentEntity::getStatus, true)
            .eq(ApiAssignmentEntity::getFlowStatus, true)
            .isNotNull(ApiAssignmentEntity::getFlowGrade)
            .isNotNull(ApiAssignmentEntity::getFlowCount)
    );
  }

  public boolean existsDataSourceById(Long dataSourceId) {
    QueryWrapper<ApiAssignmentEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(ApiAssignmentEntity::getDatasourceId, dataSourceId);
    return apiAssignmentMapper.selectCount(queryWrapper) > 0;
  }

  public boolean existsGroupById(Long groupId) {
    QueryWrapper<ApiAssignmentEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(ApiAssignmentEntity::getGroupId, groupId);
    return apiAssignmentMapper.selectCount(queryWrapper) > 0;
  }

  public boolean existsModuleById(Long moduleId) {
    QueryWrapper<ApiAssignmentEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(ApiAssignmentEntity::getModuleId, moduleId);
    return apiAssignmentMapper.selectCount(queryWrapper) > 0;
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteById(Long id) {
    apiContextDao.deleteByApiConfigId(id);
    apiAssignmentMapper.deleteById(id);
  }

}
