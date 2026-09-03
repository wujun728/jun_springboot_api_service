package com.yatop.lambda.base.model.jwt;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.IntStream;


public class PortalUtil {
    private final static Logger log = LoggerFactory.getLogger(PortalUtil.class);
    // token缓存前缀
    public static final String TOKEN_CACHE_PREFIX = "lambda.portal.cache.token.";

    /**
     * 缓存查询摸板，先查缓存，如果缓存查询失败再从数据库查询
     *
     * @return T
     */
    @SuppressWarnings("unchecked")

    public static ThreadLocal<Map<String, Object>> currentTheadLocal = new InheritableThreadLocal();


    public static Map<String, Object> getUser(String token) {
        return JWTUtil.getUser(PortalUtil.decryptToken(token));
    }


    /**
     * token 加密
     *
     * @param token token
     * @return 加密后的 token
     */
    public static String encryptToken(String token) {
        try {
            EncryptUtil encryptUtil = new EncryptUtil(TOKEN_CACHE_PREFIX);
            return encryptUtil.encrypt(token);
        } catch (Exception e) {
            log.info("token加密失败：", e);
            return null;
        }
    }

    /**
     * token 解密
     *
     * @param encryptToken 加密后的 token
     * @return 解密后的 token
     */
    public static String decryptToken(String encryptToken) {
        try {
            EncryptUtil encryptUtil = new EncryptUtil(TOKEN_CACHE_PREFIX);
            return encryptUtil.decrypt(encryptToken);
        } catch (Exception e) {
            log.info("token解密失败：", e);
            return null;
        }
    }

    /**
     * 驼峰转下划线
     *
     * @param value 待转换值
     * @return 结果
     */
    private static String camelToUnderscore(String value) {
        if (StringUtils.isBlank(value))
            return "";
        String[] arr = StringUtils.splitByCharacterTypeCamelCase(value);
        if (arr.length == 0)
            return "";
        StringBuilder result = new StringBuilder();
        IntStream.range(0, arr.length).forEach(i -> {
            if (i != arr.length - 1)
                result.append(arr[i]).append("_");
            else
                result.append(arr[i]);
        });
        return StringUtils.lowerCase(result.toString());
    }
}
