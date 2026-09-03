package com.gitee.sqlrest.core.service;

import com.gitee.sqlrest.common.exception.CommonException;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.persistence.dao.ApiAssignmentDao;
import com.gitee.sqlrest.persistence.dao.ApiGroupDao;
import com.gitee.sqlrest.persistence.dao.AppClientDao;
import com.gitee.sqlrest.persistence.entity.ApiGroupEntity;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApiGroupService {

  @Resource
  private ApiGroupDao apiGroupDao;
  @Resource
  private ApiAssignmentDao apiAssignmentDao;
  @Resource
  private AppClientDao appClientDao;

  public void createGroup(String name) {
    try {
      apiGroupDao.insert(ApiGroupEntity.builder().name(name).build());
    } catch (DuplicateKeyException e) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_ALREADY_EXISTS, "group name already exists");
    }
  }

  public void updateGroup(Long id, String newName) {
    ApiGroupEntity apiGroupEntity = apiGroupDao.getById(id);
    apiGroupEntity.setName(newName);
    try {
      apiGroupDao.updateById(apiGroupEntity);
    } catch (DuplicateKeyException e) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_ALREADY_EXISTS, "group name already exists");
    }
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteGroup(Long id) {
    if (apiAssignmentDao.existsGroupById(id)) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_ALREADY_USED, "used by api config");
    }
    apiGroupDao.deleteById(id);
    appClientDao.deleteClientAuthByGroupId(id);
  }

  public List<ApiGroupEntity> listAll() {
    return apiGroupDao.listAll();
  }
}
