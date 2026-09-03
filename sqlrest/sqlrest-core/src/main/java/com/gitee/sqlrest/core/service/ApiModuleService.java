package com.gitee.sqlrest.core.service;

import com.gitee.sqlrest.common.exception.CommonException;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.persistence.dao.ApiAssignmentDao;
import com.gitee.sqlrest.persistence.dao.ApiModuleDao;
import com.gitee.sqlrest.persistence.entity.ApiModuleEntity;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApiModuleService {

  @Resource
  private ApiModuleDao apiModuleDao;
  @Resource
  private ApiAssignmentDao apiAssignmentDao;

  public void createModule(String name) {
    try {
      apiModuleDao.insert(ApiModuleEntity.builder().name(name).build());
    } catch (DuplicateKeyException e) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_ALREADY_EXISTS, "module name already exists");
    }
  }

  public void updateModule(Long id, String newName) {
    ApiModuleEntity moduleEntity = apiModuleDao.getById(id);
    moduleEntity.setName(newName);
    try {
      apiModuleDao.updateById(moduleEntity);
    } catch (DuplicateKeyException e) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_ALREADY_EXISTS, "module name already exists");
    }
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteModule(Long id) {
    if (apiAssignmentDao.existsModuleById(id)) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_ALREADY_USED, "used by api config");
    }
    apiModuleDao.deleteById(id);
  }

  public List<ApiModuleEntity> listAll() {
    return apiModuleDao.listAll();
  }
}
