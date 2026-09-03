package com.gitee.sqlrest.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.gitee.sqlrest.common.dto.AccessToken;
import com.gitee.sqlrest.common.exception.CommonException;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.common.util.CacheUtils;
import com.gitee.sqlrest.common.util.PasswordUtils;
import com.gitee.sqlrest.common.util.TokenUtils;
import com.gitee.sqlrest.core.dto.SystemUserDetailResponse;
import com.gitee.sqlrest.persistence.dao.SystemUserDao;
import com.gitee.sqlrest.persistence.entity.SystemUserEntity;
import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SystemUserService {

  @Resource
  private SystemUserDao systemUserDao;

  public AccessToken login(String username, String password) {
    SystemUserEntity user = systemUserDao.findByUsername(username);
    if (Objects.isNull(user)) {
      throw new CommonException(ResponseErrorCode.ERROR_USER_NOT_EXISTS, username);
    }

    String encryptPassword = PasswordUtils.encryptPassword(password, user.getSalt());
    if (!encryptPassword.equals(user.getPassword())) {
      throw new CommonException(ResponseErrorCode.ERROR_USER_PASSWORD_WRONG, username);
    }

    String token = TokenUtils.generateValue();
    CacheUtils.put(token, user);
    AccessToken accessTokenWrapper = new AccessToken(user.getRealName(), user.getUsername(), token,
        System.currentTimeMillis() / 1000, CacheUtils.CACHE_DURATION_SECONDS);
    return accessTokenWrapper;
  }

  public void logout(HttpServletRequest request) {
    String token = TokenUtils.getRequestToken(request);
    if (StringUtils.isNotBlank(token)) {
      CacheUtils.remove(token);
    }
  }

  public SystemUserDetailResponse getUserDetailById(Long id) {
    SystemUserEntity user = systemUserDao.getById(id);
    if (Objects.isNull(user)) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_NOT_EXISTS, "id=" + id);
    }
    SystemUserDetailResponse detailResponse = new SystemUserDetailResponse();
    BeanUtil.copyProperties(user, detailResponse);
    return detailResponse;
  }

  public SystemUserDetailResponse getUserDetailByUsername(String username) {
    SystemUserEntity user = findByUsername(username);
    if (Objects.isNull(user)) {
      throw new CommonException(ResponseErrorCode.ERROR_RESOURCE_NOT_EXISTS, "username=" + username);
    }

    SystemUserDetailResponse detailResponse = new SystemUserDetailResponse();
    BeanUtil.copyProperties(user, detailResponse);
    return detailResponse;
  }

  public void changeOwnPassword(HttpServletRequest request, String oldPassword, String newPassword) {
    String username = request.getAttribute("username").toString();
    SystemUserEntity systemUserEntity = findByUsername(username);
    if (Objects.isNull(systemUserEntity)) {
      throw new CommonException(ResponseErrorCode.ERROR_USER_NOT_EXISTS, username);
    }

    String encryptOldPassword = PasswordUtils
        .encryptPassword(oldPassword, systemUserEntity.getSalt());
    if (!encryptOldPassword.equals(systemUserEntity.getPassword())) {
      throw new CommonException(ResponseErrorCode.ERROR_USER_PASSWORD_WRONG, username);
    }

    String encryptNewPassword = PasswordUtils
        .encryptPassword(newPassword, systemUserEntity.getSalt());
    systemUserDao.updateUserPassword(username, encryptNewPassword);
  }

  public SystemUserEntity findByUsername(String username) {
    return systemUserDao.findByUsername(username);
  }
}
