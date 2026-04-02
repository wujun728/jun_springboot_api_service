package com.gitee.freakchicken.dbapi.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.dao.ApiConfigMapper;
import com.gitee.freakchicken.dbapi.domain.DataSource;
import com.gitee.freakchicken.dbapi.domain.Token;
import com.gitee.freakchicken.dbapi.service.*;
import com.gitee.freakchicken.dbapi.util.IPUtil;
import com.gitee.freakchicken.dbapi.util.JdbcUtil;
import com.gitee.freakchicken.dbapi.util.SqlEngineUtil;
import io.github.wujun728.sql.SqlMeta;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * api拦截器
 *
 * @author jiangqiang
 * @date 2019年3月19日下午4:30:56
 */
@Component
@Slf4j
public class ApiInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        String originIp = IPUtil.getOriginIp(request);
        log.debug(originIp);

        String method = request.getMethod();
        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(5);
        log.debug(servletPath);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        // 跨域设置
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization");//这里很重要，要不然js header不能跨域携带  Authorization属性
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");

        PrintWriter out = null;
        try {

            //js跨域的预检请求，不经过处理逻辑
            if (method.equals("OPTIONS")) {
                response.setStatus(HttpServletResponse.SC_OK);
                return false;
            }
            out = response.getWriter();
            boolean checkIP = checkIP(originIp);
            if (!checkIP) {
                out.append(JSON.toJSONString(ResponseDto.fail("非法ip(" + originIp + ")，禁止访问")));
                return false;
            }
            ResponseDto responseDto = process(servletPath, request, response);

            log.debug(JSON.toJSONString(responseDto));
            out.append(JSON.toJSONString(responseDto));
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
            return false;
        } finally {
            if (out != null)
                out.close();
        }

    }

    @Autowired
    ApiConfigService apiConfigService;
    @Autowired
    DataSourceService dataSourceService;
    @Autowired
    ApiService apiService;
    @Autowired
    TokenService tokenService;
    @Autowired
    IPService ipService;
    @Autowired
    ApiConfigMapper apiConfigMapper;

    public boolean checkIP(String originIp) {
        Map<String, String> map = ipService.detail();
        String status = map.get("status");
        if (status.equals("on")) {
            String mode = map.get("mode");
            if (mode.equals("black")) {
                String blackIP = map.get("blackIP");
                if (!ipService.check(mode, blackIP, originIp)) {
                    log.warn("ip黑名单拦截");
                    return false;
                }
            } else if (mode.equals("white")) {
                String whiteIP = map.get("whiteIP");
                if (!ipService.check(mode, whiteIP, originIp)) {
                    log.warn("ip白名单检查不通过");
                    return false;
                }
            }
        }
        return true;
    }

    public ResponseDto process(String path, HttpServletRequest request, HttpServletResponse response) {
        ApiConfig config = null;
        try {
            // 校验接口是否存在
            config = apiConfigService.getConfig(path);
            if (config == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return ResponseDto.fail("该接口不存在！！");
            }
            // 如果是私有接口，校验权限
            List<String> authGroups = null;
            if (config.getPrevilege() == 0) {
                String tokenStr = request.getHeader("Authorization");
                log.debug(tokenStr);
                if (StringUtils.isBlank(tokenStr)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return ResponseDto.fail("header中token缺失，私有接口禁止访问！！");
                } else {
                    Token token = tokenService.getToken(tokenStr);
                    if (token == null) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return ResponseDto.fail("token无效，私有接口禁止访问！！");
                    } else {
                        if (token.getExpire() != null && token.getExpire() < System.currentTimeMillis()) {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            return ResponseDto.fail("token过期，私有接口禁止访问！！");
                        } else {
                            authGroups = tokenService.getAuthGroups(token.getId());
                            if (!checkAuth(authGroups, config.getGroupId())) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                return ResponseDto.fail("该token无权访问此接口");
                            }
                        }
                    }
                }
            }

            DataSource datasource = dataSourceService.detail(config.getDatasourceId());
            if (datasource == null) {
                response.setStatus(500);
                return ResponseDto.fail("数据源不存在！！");
            }

            // 数据源权限校验：私有接口需验证数据源属于 token 授权的组
            if (config.getPrevilege() == 0 && authGroups != null) {
                int count = apiConfigMapper.countByDatasourceAndGroups(config.getDatasourceId(), authGroups);
                if (count == 0) {
                    log.warn("数据源权限校验失败: datasourceId={}, authGroups={}", config.getDatasourceId(), authGroups);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return ResponseDto.fail("无权访问该数据源");
                }
            }

            Map<String, Object> sqlParam;
            try {
                sqlParam = apiService.getSqlParam(request, config);
            } catch (IllegalArgumentException e) {
                log.warn("请求参数类型转换失败: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ResponseDto.fail(e.getMessage());
            }

            // 1. 参数校验（ParameterValidator）
            if (StringUtils.isNoneBlank(config.getParameterValidator())) {
                try {
                    ParameterValidator validator = PluginManager.getParameterValidator(config.getParameterValidator());
                    boolean valid = validator.validate(sqlParam, config, null, config.getParameterValidatorParams());
                    if (!valid) {
                        return ResponseDto.fail("参数校验不通过");
                    }
                } catch (Exception e) {
                    log.error("参数校验失败", e);
                    return ResponseDto.fail("参数校验失败：" + e.getMessage());
                }
            }

            // 2. 参数预处理（ParamProcessPlugin）
            if (StringUtils.isNoneBlank(config.getParamProcessPlugin())) {
                try {
                    ParamProcessPlugin paramProcessPlugin = PluginManager.getParamProcessPlugin(config.getParamProcessPlugin());
                    paramProcessPlugin.process(sqlParam, config, config.getParamProcessPluginParams());
                } catch (Exception e) {
                    log.error("参数预处理失败", e);
                    return ResponseDto.fail("参数预处理失败：" + e.getMessage());
                }
            }

            // 3. 从缓存获取数据
            if (StringUtils.isNoneBlank(config.getCachePlugin())) {
                try {
                    CachePlugin cachePlugin = PluginManager.getCachePlugin(config.getCachePlugin());
                    Object o = cachePlugin.get(config, sqlParam, config.getCachePluginParams());
                    if (o != null) {
                        ResponseDto cachedResponse = ResponseDto.apiSuccess(o);
                        // 缓存命中也执行全局转换
                        return applyGlobalTransform(config, cachedResponse);
                    }
                } catch (Exception e) {
                    log.error("获取缓存失败", e);
                }
            }

            // 4. 执行SQL
            String sql = config.getSql();
            SqlMeta sqlMeta = SqlEngineUtil.getEngine().parse(sql, sqlParam);
            ResponseDto responseDto = JdbcUtil.executeSql(datasource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());

            // 5. 数据转换（TransformPlugin）
            if (StringUtils.isNoneBlank(config.getTransformPlugin()) && responseDto.isSuccess()) {
                try {
                    TransformPlugin transformPlugin = PluginManager.getTransformPlugin(config.getTransformPlugin());
                    Object transform = transformPlugin.transform(responseDto.getData(), config.getTransformPluginParams());
                    responseDto.setData(transform);
                } catch (Exception e) {
                    log.error("数据转换失败", e);
                }
            }

            // 6. 设置缓存
            if (StringUtils.isNoneBlank(config.getCachePlugin()) && responseDto.isSuccess()) {
                try {
                    CachePlugin cachePlugin = PluginManager.getCachePlugin(config.getCachePlugin());
                    cachePlugin.set(config, sqlParam, responseDto.getData(), config.getCachePluginParams());
                } catch (Exception e) {
                    log.error("缓存设置失败", e);
                }
            }

            // 7. 全局响应转换（GlobalTransformPlugin）
            responseDto = applyGlobalTransform(config, responseDto);

            if (!responseDto.isSuccess()) {
                response.setStatus(500);
            }
            return responseDto;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 8. 异常时告警（AlarmPlugin）
            if (config != null && StringUtils.isNoneBlank(config.getAlarmPlugin())) {
                try {
                    AlarmPlugin alarmPlugin = PluginManager.getAlarmPlugin(config.getAlarmPlugin());
                    alarmPlugin.alarm(e, config, request, config.getAlarmPluginParams());
                } catch (Exception ae) {
                    log.error("告警插件执行失败", ae);
                }
            }
            ResponseDto fail = ResponseDto.fail(e.getMessage() == null ? e.toString() : e.getMessage());
            return fail;
        }
    }

    private ResponseDto applyGlobalTransform(ApiConfig config, ResponseDto responseDto) {
        if (StringUtils.isNoneBlank(config.getGlobalTransformPlugin())) {
            try {
                GlobalTransformPlugin globalPlugin = PluginManager.getGlobalTransformPlugin(config.getGlobalTransformPlugin());
                Object transformed = globalPlugin.transform(responseDto, config.getGlobalTransformPluginParams());
                if (transformed instanceof ResponseDto) {
                    return (ResponseDto) transformed;
                }
                // 如果全局转换返回的不是ResponseDto，则包装为成功的ResponseDto
                ResponseDto newDto = new ResponseDto();
                newDto.setSuccess(responseDto.isSuccess());
                newDto.setMsg(responseDto.getMsg());
                newDto.setData(transformed);
                return newDto;
            } catch (Exception e) {
                log.error("全局响应转换失败", e);
            }
        }
        return responseDto;
    }

    public boolean checkAuth(List<String> authGroups, String group) {
        for (String authGroup : authGroups) {
            if (authGroup.equals(group)) {
                return true;
            }
        }
        return false;
    }

}
