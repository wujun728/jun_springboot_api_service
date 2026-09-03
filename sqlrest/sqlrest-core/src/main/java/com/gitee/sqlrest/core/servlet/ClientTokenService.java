package com.gitee.sqlrest.core.servlet;

import com.gitee.sqlrest.cache.CacheFactory;
import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.AccessToken;
import com.gitee.sqlrest.common.enums.DurationTimeEnum;
import com.gitee.sqlrest.common.exception.CommonException;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.common.util.TokenUtils;
import com.gitee.sqlrest.persistence.dao.AppClientDao;
import com.gitee.sqlrest.persistence.entity.AppClientEntity;
import java.time.LocalDateTime;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientTokenService {

  @Resource
  private AppClientDao appClientDao;
  @Resource
  private CacheFactory cacheFactory;

  @EventListener(ApplicationReadyEvent.class)
  public void init() {
    LocalDateTime now = LocalDateTime.now();
    try {
      for (AppClientEntity appClient : appClientDao.listAll(null)) {
        log.info("Load client app token from persistence :{}", appClient);
        if (StringUtils.isNotBlank(appClient.getAccessToken())) {
          AccessToken clientToken = AccessToken.builder()
              .realName(appClient.getName())
              .appKey(appClient.getAppKey())
              .accessToken(appClient.getAccessToken())
              .createTimestamp(appClient.getCreateTime().getTime() / 1000)
              .expireSeconds(appClient.getExpireDuration().getValue())
              .build();

          if (DurationTimeEnum.TIME_VALUE.equals(appClient.getExpireDuration())) {
            long expireSeconds = appClient.getExpireAt() - now.getSecond();
            if (expireSeconds >= Constants.CLIENT_TOKEN_DURATION_SECONDS) {
              clientToken.setExpireSeconds(Constants.CLIENT_TOKEN_DURATION_SECONDS);
            } else {
              clientToken.setExpireSeconds(expireSeconds);
            }
          }

          Map tokenClientMap = cacheFactory.getCacheMap(Constants.CACHE_KEY_TOKEN_CLIENT);
          tokenClientMap.put(appClient.getAccessToken(), clientToken);
        }
      }
      log.info("Finish load client app token from persistence.");
    } catch (Exception e) {
      log.error("load client app token failed:{}", e.getMessage(), e);
      throw e;
    }
  }

  public AccessToken generateToken(String clientId, String clientSecret) {
    AppClientEntity appClient = appClientDao.getByAppKey(clientId);
    if (null == appClient || !StringUtils.equals(appClient.getAppSecret(), clientSecret)) {
      return null;
    }
    String token = TokenUtils.generateValue();
    AccessToken clientToken = AccessToken.builder()
        .realName(appClient.getName())
        .appKey(clientId)
        .accessToken(token)
        .createTimestamp(System.currentTimeMillis() / 1000)
        .expireSeconds(Constants.CLIENT_TOKEN_DURATION_SECONDS)
        .build();
    if (DurationTimeEnum.TIME_VALUE.equals(appClient.getExpireDuration())) {
      long expireSeconds = appClient.getExpireAt() - (System.currentTimeMillis() / 1000);
      if (expireSeconds <= 0) {
        throw new CommonException(ResponseErrorCode.ERROR_CLIENT_FORBIDDEN, "app key is timeout");
      }
      if (expireSeconds >= Constants.CLIENT_TOKEN_DURATION_SECONDS) {
        clientToken.setExpireSeconds(Constants.CLIENT_TOKEN_DURATION_SECONDS);
      } else {
        clientToken.setExpireSeconds(expireSeconds);
      }
    }

    if (!DurationTimeEnum.ONLY_ONCE.equals(appClient.getExpireDuration())) {
      // 将token持久化到数据库中，以备重启服务器后原token继续可用
      appClientDao.updateTokenByAppKey(clientId, token);
    }

    Map tokenClientMap = cacheFactory.getCacheMap(Constants.CACHE_KEY_TOKEN_CLIENT);
    tokenClientMap.put(token, clientToken);

    return clientToken;
  }

  public String verifyTokenAndGetAppKey(String tokenStr) {
    if (StringUtils.isBlank(tokenStr)) {
      return null;
    }
    Map<String, AccessToken> tokenClientMap = cacheFactory
        .getCacheMap(Constants.CACHE_KEY_TOKEN_CLIENT);
    AccessToken clientToken = tokenClientMap.get(tokenStr);
    if (null == clientToken) {
      return null;
    }
    Long expireTime = clientToken.getExpireSeconds();
    if (expireTime <= 0) {
      if (0 == expireTime) {
        tokenClientMap.remove(tokenStr);
      }
      return clientToken.getAppKey();
    } else if ((System.currentTimeMillis() / 1000) - clientToken.getCreateTimestamp() > expireTime) {
      log.error("token [{}] expired, clientId: {}", tokenStr, clientToken.getAppKey());
      return null;
    }
    return clientToken.getAppKey();
  }

  public boolean verifyAuthGroup(String clientId, Long groupId) {
    return appClientDao.getAuthGroups(clientId).contains(groupId);
  }
}
