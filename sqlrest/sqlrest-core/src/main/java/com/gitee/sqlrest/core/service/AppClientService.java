package com.gitee.sqlrest.core.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.gitee.sqlrest.common.dto.PageResult;
import com.gitee.sqlrest.common.enums.DurationTimeEnum;
import com.gitee.sqlrest.common.enums.ExpireTimeEnum;
import com.gitee.sqlrest.common.exception.CommonException;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.common.util.TokenUtils;
import com.gitee.sqlrest.core.dto.AppClientDetailResponse;
import com.gitee.sqlrest.core.dto.AppClientGroupRequest;
import com.gitee.sqlrest.core.dto.AppClientSaveRequest;
import com.gitee.sqlrest.core.dto.EntityIdNameResponse;
import com.gitee.sqlrest.core.dto.EntitySearchRequest;
import com.gitee.sqlrest.persistence.dao.AppClientDao;
import com.gitee.sqlrest.persistence.entity.AppClientEntity;
import com.gitee.sqlrest.persistence.util.PageUtils;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AppClientService {

  @Resource
  private AppClientDao appClientDao;

  public void create(AppClientSaveRequest request) {
    if (null != appClientDao.getByAppKey(request.getAppKey())) {
      throw new CommonException(ResponseErrorCode.ERROR_INVALID_ARGUMENT, "Duplicate app key :" + request.getAppKey());
    }

    AppClientEntity appClientEntity = new AppClientEntity();
    BeanUtil.copyProperties(request, appClientEntity);
    appClientEntity.setAppSecret(TokenUtils.generateValue());
    appClientEntity.setExpireDuration(request.getExpireTime().getDuration());
    appClientEntity.setExpireAt(request.getExpireTime().getValue() + (System.currentTimeMillis() / 1000));
    appClientEntity.setAccessToken(null);
    appClientDao.insert(appClientEntity);
  }

  public void delete(Long id) {
    appClientDao.deleteById(id);
  }

  public PageResult<AppClientDetailResponse> searchList(EntitySearchRequest request) {
    Supplier<List<AppClientDetailResponse>> method = () -> {
      List<AppClientEntity> list = appClientDao.listAll(request.getSearchText());
      return list.stream().map(appClientEntity -> {
        AppClientDetailResponse response = new AppClientDetailResponse();
        BeanUtil.copyProperties(appClientEntity, response);
        ExpireTimeEnum expireTime = ExpireTimeEnum
            .from(appClientEntity.getExpireDuration(), appClientEntity.getExpireAt());
        Boolean isExpired = (System.currentTimeMillis() / 1000) > response.getExpireAt();
        if (DurationTimeEnum.TIME_VALUE != appClientEntity.getExpireDuration()) {
          isExpired = false;
        }
        response.setExpireType(expireTime.getDescription());
        response.setIsExpired(isExpired);
        if (response.getExpireAt() > 0) {
          long expireAt = response.getExpireAt();
          response.setExpireAtStr(DateUtil.format(new Date(expireAt * 1000), "yyyy-MM-dd HH:mm:ss"));
        }
        return response;
      }).collect(Collectors.toList());
    };

    return PageUtils.getPage(method, request.getPage(), request.getSize());
  }

  public String getSecret(Long id) {
    AppClientEntity entity = appClientDao.getById(id);
    if (null == entity) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_NOT_EXISTS, "id=" + id);
    }
    return entity.getAppSecret();
  }

  public void createGroupAuth(AppClientGroupRequest request) {
    appClientDao.saveAuthGroup(request.getId(), request.getGroupIds());
  }

  public List<EntityIdNameResponse> getGroupAuth(Long id) {
    return appClientDao.getGroupAuth(id).stream()
        .map(item -> new EntityIdNameResponse(item.getId(), item.getName()))
        .collect(Collectors.toList());
  }

}
