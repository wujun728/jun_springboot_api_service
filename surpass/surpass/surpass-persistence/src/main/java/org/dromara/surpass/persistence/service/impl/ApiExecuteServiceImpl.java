package org.dromara.surpass.persistence.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.datasource.DataSourceSwitch;
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.surpass.entity.ApiRequestUri;
import org.dromara.surpass.entity.api.ApiParam;
import org.dromara.surpass.entity.api.ApiParamRule;
import org.dromara.surpass.entity.api.ApiVersion;
import org.dromara.surpass.entity.api.dto.ApiParamList;
import org.dromara.surpass.entity.app.AppProxyRules;
import org.dromara.surpass.entity.app.AppResources;
import org.dromara.surpass.enums.ApiFieldDataTypeEnum;
import org.dromara.surpass.enums.ApiSupportPagingEnum;
import org.dromara.surpass.exception.BusinessException;
import org.dromara.surpass.persistence.service.*;
import org.dromara.surpass.proxy.client.HttpProxyClient;
import org.dromara.surpass.proxy.dto.ProxyRequest;
import org.dromara.surpass.proxy.dto.ProxyResponse;
import org.dromara.surpass.util.JsonUtils;
import org.dromara.surpass.web.ResponseTemplateRenderer;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

/**
 * API 执行服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiExecuteServiceImpl implements ApiExecuteService {
    private static final String DEFAULT_PAGE_NUM_KEY = "_pageNum";
    private static final String DEFAULT_PAGE_SIZE_KEY = "_pageSize";
    private static final String CLASSIFY_PROXY = "proxy";

    private final ISqlRepository sqlRepository;
    private final ApiVersionService apiVersionService;
    private final AppResourcesService appResourcesService;
    private final ApiDataSourceService dataSourceService;
    private final AppProxyRulesService appProxyRulesService;
    private final ResponseTemplateRenderer responseRenderer;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpProxyClient httpProxyClient = new HttpProxyClient();

    @Override
    public Object execute(ApiRequestUri apiRequestUri, String method, Map<String, Object> params, boolean isTest) {
        try {
            AppResources byPathAndMethod;
            if (isTest) {
                ApiVersion apiVersion = getApiVersion(params);
                byPathAndMethod = appResourcesService.get(apiVersion.getApiId());
                params = getRequestParams(params);
            } else {
                byPathAndMethod = appResourcesService.findByPathAndMethod(
                        apiRequestUri.getResourcePath(), method, apiRequestUri.getContextPath());
                if (Objects.isNull(byPathAndMethod)) {
                    throw new BusinessException(50001, "API 不存在");
                }
            }

            // 判断是否为代理类型
            if (CLASSIFY_PROXY.equals(byPathAndMethod.getClassify())) {
                return executeProxy(apiRequestUri, method, params,isTest,  byPathAndMethod);
            }

            // 普通 API 执行流程
            return executeSqlApi(apiRequestUri, method, params, isTest, byPathAndMethod);
        } catch (Exception e) {
            log.error("执行 API 失败：{} {}", method, apiRequestUri.getRequestPath(), e);
            throw new BusinessException(50001, "API 执行失败：" + e.getMessage());
        } finally {
            DataSourceSwitch.switchToDefault();
        }
    }

    /**
     * 执行代理请求
     */
    private Object executeProxy(ApiRequestUri apiRequestUri, String method,
                                Map<String, Object> params,boolean isTest, AppResources byPathAndMethod) {
        try {
            // 1. 查询代理规则
            AppProxyRules rule = appProxyRulesService.get(byPathAndMethod.getDatasourceId());

            if (rule == null) {
                throw new BusinessException(50001, "代理规则不存在：" + byPathAndMethod.getDatasourceId());
            }
            if (!"1".equals(rule.getStatus())) {
                throw new BusinessException(50001, "代理规则已禁用：" + byPathAndMethod.getDatasourceId());
            }
            // 2. 构建代理请求
            ProxyRequest proxyRequest = new ProxyRequest();
            // 从配置中解析目标 URL
            String baseUrl = extractBaseUrl(rule.getConfig());
            String targetPath = apiRequestUri.getResourcePath();
            String targetUrl = appendPath(baseUrl, targetPath);
            proxyRequest.setTargetUrl(targetUrl);
            proxyRequest.setMethod(method);
            proxyRequest.setProtocolType(rule.getProtocolType());
            proxyRequest.setConfigJson(rule.getConfig());

            // 设置请求体（从 params 中提取）
            String body = extractBody(params);
            if (body != null && !body.isEmpty()) {
                proxyRequest.setBody(body);
            }

            // 3. 执行代理请求
            ProxyResponse proxyResponse = httpProxyClient.execute(proxyRequest);

            // 4. 返回响应
            Map<String, Object> result = new HashMap<>();
            result.put("statusCode", proxyResponse.getStatusCode());
            result.put("headers", proxyResponse.getHeaders());
            result.put("body", proxyResponse.getBody());
            result.put("executionTime", proxyResponse.getExecutionTime());

            if (!proxyResponse.getSuccess()) {
                result.put("error", proxyResponse.getErrorMessage());
            }

            return result;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("执行代理请求失败：path={}, method={}", apiRequestUri.getRequestPath(), method, e);
            throw new BusinessException(50001, "代理请求失败：" + e.getMessage());
        }
    }

    /**
     * 执行 SQL API
     */
    private Object executeSqlApi(ApiRequestUri apiRequestUri, String method, Map<String, Object> params,
                                  boolean isTest, AppResources byPathAndMethod) throws JsonProcessingException {
        ApiVersion apiVersion;
        Object data = null;

        if (isTest) {
            apiVersion = getApiVersion(params);
        } else {
            apiVersion = apiVersionService.findPublishedVersionByApiId(byPathAndMethod.getId());
            if (Objects.isNull(apiVersion)) {
                throw new BusinessException(50001, "API 未发布");
            }
        }

        // 参数验证和转换
        Map<String, Object> parseredParams = validateAndConvert(params, apiVersion.getParamDefinition());

        // 切换数据源
        dataSourceService.switchDataSource(byPathAndMethod.getDatasourceId());

        // 获取 SQL
        String sql = apiVersion.getSqlTemplate().trim();

        // 判断是否分页
        if (ApiSupportPagingEnum.PAGE.getCode().equals(apiVersion.getSupportsPaging())) {
            int pageNum = Integer.parseInt(params.getOrDefault(DEFAULT_PAGE_NUM_KEY, "1").toString());
            int pageSize = Integer.parseInt(params.getOrDefault(DEFAULT_PAGE_SIZE_KEY, "20").toString());
            JpaPage page = new JpaPage(pageNum, pageSize);
            data = sqlRepository.fetch(sql, page, parseredParams);
        } else if (ApiSupportPagingEnum.LIST.getCode().equals(apiVersion.getSupportsPaging())) {
            return sqlRepository.selectList(sql, parseredParams);
        } else if (ApiSupportPagingEnum.SINGLE.getCode().equals(apiVersion.getSupportsPaging())) {
            JpaPage page = new JpaPage(1, 1);
            List<Map<String, Object>> rows = sqlRepository.fetch(sql, page, parseredParams).getRows();
            if (!rows.isEmpty()) {
                data = rows.get(0);
            }
        } else if (ApiSupportPagingEnum.INSERT.getCode().equals(apiVersion.getSupportsPaging())) {
            int generatedKey = sqlRepository.insert(sql, parseredParams);
            data = Map.of("affectedRows", 1, "generatedKey", generatedKey);
        } else if (ApiSupportPagingEnum.UPDATE.getCode().equals(apiVersion.getSupportsPaging())) {
            int affectedRows = sqlRepository.update(sql, parseredParams);
            data = Map.of("affectedRows", affectedRows);
        } else if (ApiSupportPagingEnum.DELETE.getCode().equals(apiVersion.getSupportsPaging())) {
            int affectedRows = sqlRepository.delete(sql, parseredParams);
            data = Map.of("affectedRows", affectedRows);
        } else {
            throw new BusinessException(50001, "不支持的 SQL 类型");
        }

        return responseRenderer.renderResponse(
                StringUtils.isBlank(apiVersion.getResponseTemplate()) ?
                        responseRenderer.getDefaultResponseTemplate() : apiVersion.getResponseTemplate(), data);
    }

    /**
     * 参数验证和转换
     */
    public Map<String, Object> validateAndConvert(Map<String, Object> params, String paramRuleJson) {
        log.debug("params : {}", params);
        log.debug("paramJson : {}", paramRuleJson);
        Map<String, Object> convertedParams = new HashMap<>();
        String json = "{\"paramList\":" + paramRuleJson.replace("\n", "") + "}";
        ApiParamList paramList = JsonUtils.stringToObject(json, ApiParamList.class);
        log.debug("paramList : {}", paramList);
        for (ApiParam apiParam : paramList.getParamList()) {
            Object value = params.get(apiParam.getName());
            if (value != null) {
                ApiFieldDataTypeEnum typeEnum = ApiFieldDataTypeEnum.fromCode(apiParam.getType());
                Object convertedValue;
                if (typeEnum != null) {
                    convertedValue = typeEnum.convert(value);
                } else {
                    convertedValue = value;
                }
                paramCheck(convertedValue, apiParam.getRules());
                convertedParams.put(apiParam.getName(), convertedValue);
            } else {
                paramCheck(null, apiParam.getRules());
                if (apiParam.getRules().getDefaultValue() != null) {
                    convertedParams.put(apiParam.getName(), apiParam.getRules().getDefaultValue());
                }
            }
        }
        log.debug("converted Params : {}", convertedParams);
        return convertedParams;
    }

    private ApiVersion getApiVersion(Map<String, Object> params) throws JsonProcessingException {
        return objectMapper.readValue(params.get("apiVersion").toString(), ApiVersion.class);
    }

    private Map<String, Object> getRequestParams(Map<String, Object> params) {
        return (Map<String, Object>) params.getOrDefault("data", new HashMap<>());
    }

    /**
     * 从参数中提取请求体
     */
    private String extractBody(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        // 尝试从 params 中提取 body
        Object bodyObj = params.get("body");
        if (bodyObj != null) {
            return bodyObj.toString();
        }
        // 如果没有 body，尝试将整个 params 转为 JSON
        try {
            return objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            log.warn("序列化请求体失败", e);
        }
        return null;
    }

    /**
     * 请求参数校验
     */
    private void paramCheck(Object convertedValue, ApiParamRule rules) {
        if (rules == null) {
            return;
        }

        if (convertedValue == null || "".equals(convertedValue.toString()) || convertedValue.toString().isEmpty()) {
            if (rules.isRequired()) {
                throw new BusinessException(40001, "参数不能为空");
            }
            return;
        }

        String valueStr = convertedValue.toString();

        // 字符串长度校验
        if (rules.getMinLength() != null || rules.getMaxLength() != null) {
            int length = valueStr.length();
            if (rules.getMinLength() != null && rules.getMinLength() > 0 && length < rules.getMinLength()) {
                throw new BusinessException(40002, "参数长度不能小于" + rules.getMinLength());
            }
            if (rules.getMaxLength() != null && rules.getMaxLength() > 0 && length > rules.getMaxLength()) {
                throw new BusinessException(40003, "参数长度不能大于" + rules.getMaxLength());
            }
        }

        // 数值范围校验
        if (rules.getMinValue() != null || rules.getMaxValue() != null) {
            try {
                long value = Long.parseLong(valueStr);
                if (rules.getMinValue() != null && value < rules.getMinValue()) {
                    throw new BusinessException(40004, "参数值不能小于" + rules.getMinValue());
                }
                if (rules.getMaxValue() != null && value > rules.getMaxValue()) {
                    throw new BusinessException(40005, "参数值不能大于" + rules.getMaxValue());
                }
            } catch (NumberFormatException e) {
                throw new BusinessException(40006, "参数值不是有效的数字");
            }
        }

        // 正则表达式校验
        if (rules.getPattern() != null && !rules.getPattern().isEmpty()) {
            if (convertedValue instanceof List) {
                List<?> list = (List<?>) convertedValue;
                for (Object item : list) {
                    if (item != null && !item.toString().matches(rules.getPattern())) {
                        throw new BusinessException(40007, "数组参数中存在格式不符合要求的元素：" + item);
                    }
                }
            } else if (convertedValue.getClass().isArray()) {
                int length = Array.getLength(convertedValue);
                for (int i = 0; i < length; i++) {
                    Object item = Array.get(convertedValue, i);
                    if (item != null && !item.toString().matches(rules.getPattern())) {
                        throw new BusinessException(40007, "数组参数中存在格式不符合要求的元素：" + item);
                    }
                }
            } else {
                if (!valueStr.matches(rules.getPattern())) {
                    throw new BusinessException(40007, "参数格式不符合要求");
                }
            }
        }

        // 枚举值校验
        if (rules.getEnumValues() != null && !rules.getEnumValues().isEmpty()) {
            String[] enumValues = rules.getEnumValues().split(",");
            if (convertedValue instanceof List) {
                List<?> list = (List<?>) convertedValue;
                for (Object item : list) {
                    if (item != null) {
                        boolean found = false;
                        String itemStr = item.toString();
                        for (String enumValue : enumValues) {
                            if (enumValue.trim().equals(itemStr)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            throw new BusinessException(40008, "数组参数中存在不在枚举值范围内的元素：" + itemStr);
                        }
                    }
                }
            } else if (convertedValue.getClass().isArray()) {
                int length = Array.getLength(convertedValue);
                for (int i = 0; i < length; i++) {
                    Object item = Array.get(convertedValue, i);
                    if (item != null) {
                        boolean found = false;
                        String itemStr = item.toString();
                        for (String enumValue : enumValues) {
                            if (enumValue.trim().equals(itemStr)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            throw new BusinessException(40008, "数组参数中存在不在枚举值范围内的元素：" + itemStr);
                        }
                    }
                }
            } else {
                boolean found = false;
                for (String enumValue : enumValues) {
                    if (enumValue.trim().equals(valueStr)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new BusinessException(40008, "参数值必须是：" + rules.getEnumValues());
                }
            }
        }
    }

    /**
     * 从配置 JSON 中提取 baseUrl
     */
    private String extractBaseUrl(String configJson) {
        if (configJson == null || configJson.isEmpty()) {
            return "";
        }
        try {
            JsonNode configNode = objectMapper.readTree(configJson);
            if (configNode.has("targetUrl")) {
                return configNode.get("targetUrl").asText();
            }
            if (configNode.has("url")) {
                return configNode.get("url").asText();
            }
            if (configNode.has("baseUrl")) {
                return configNode.get("baseUrl").asText();
            }
            if (configNode.has("endpoint")) {
                return configNode.get("endpoint").asText();
            }
        } catch (Exception e) {
            log.warn("解析配置 JSON 失败：{}", configJson, e);
        }
        return "";
    }

    /**
     * 拼接路径到 URL
     */
    private String appendPath(String baseUrl, String path) {
        if (path == null || path.isEmpty()) {
            return baseUrl;
        }
        if (baseUrl == null || baseUrl.isEmpty()) {
            return path;
        }
        while (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return baseUrl + path;
    }
}
