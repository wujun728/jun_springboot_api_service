package com.gitee.sqlrest.core.service;

import com.gitee.sqlrest.common.exception.CommonException;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.persistence.dao.SystemParamDao;
import com.gitee.sqlrest.persistence.entity.SystemParamEntity;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SystemParamService {

  @Resource
  private SystemParamDao systemParamDao;

  public Object getByParamKey(String key) {
    SystemParamEntity entity = systemParamDao.getByParamKey(key);
    if (null == entity) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_NOT_EXISTS, "NO KEY:" + key);
    }
    Class clazz = entity.getParamType().getClazz();
    String paramValue = entity.getParamValue();
    return clazz.cast(entity.getParamType().getConverter().apply(paramValue));
  }

  public void updateByParamKey(String key, String value) {
    SystemParamEntity entity = systemParamDao.getByParamKey(key);
    if (null == entity) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_NOT_EXISTS, "NO KEY:" + key);
    }
    Class clazz = entity.getParamType().getClazz();
    Object paramValue = clazz.cast(entity.getParamType().getConverter().apply(value));
    if (null == paramValue) {
      throw new CommonException(ResponseErrorCode.ERROR_INTERNAL_ERROR, "Invalid param value:[" + value + "]");
    }
    systemParamDao.updateByParamKey(key, String.valueOf(paramValue));
  }
}
