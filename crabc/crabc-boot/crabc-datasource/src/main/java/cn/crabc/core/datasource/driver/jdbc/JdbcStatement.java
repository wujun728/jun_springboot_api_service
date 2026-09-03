/*
 * Copyright 2023, crabc.cn (creabc@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.crabc.core.datasource.driver.jdbc;

import cn.crabc.core.datasource.config.JdbcDataSourceRouter;
import cn.crabc.core.datasource.constant.BaseConstant;
import cn.crabc.core.datasource.enums.ErrorStatusEnum;
import cn.crabc.core.datasource.exception.CustomException;
import cn.crabc.core.datasource.mapper.BaseDataHandleMapper;
import cn.crabc.core.datasource.util.PageInfo;
import cn.crabc.core.spi.StatementMapper;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

public class JdbcStatement implements StatementMapper {
    private static final Logger log = LoggerFactory.getLogger(JdbcStatement.class);
    private final BaseDataHandleMapper baseMapper;
    private static final int PAGE_SIZE = 50;
    private static final int PAGE_NUM = 1;

    public JdbcStatement(BaseDataHandleMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public Map<String, Object> selectOne(String dataSourceId, String schema, String sql, Object params) {
        List<Map<String, Object>> maps = selectList(dataSourceId, schema, sql, params);
        return maps.isEmpty() ? new HashMap<>() : maps.get(0);
    }

    @Override
    public List<Map<String, Object>> selectList(String dataSourceId, String schema, String sql, Object params) {
        PageInfo page = selectPage(dataSourceId, schema, sql, params, PAGE_NUM, PAGE_SIZE);
        return page.getList();
    }

    @Override
    public PageInfo selectPage(String dataSourceId, String schema, String sql, Object params, int pageNum, int pageSize) {
        String execType = null;
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        try {
            Map<String, Object> paramsMap = setParams(dataSourceId, schema, sql, params);
            execType = (String)paramsMap.get(BaseConstant.BASE_API_EXEC_TYPE);
            
            Object pageSetup = paramsMap.get(BaseConstant.PAGE_SETUP);
            int pageCount = pageSetup != null ? Integer.parseInt(pageSetup.toString()) : 0;
            
            if (pageCount != 0 && !checkPage(sql)) {
                PageHelper.startPage(pageNum, pageSize);
            }
            list = baseMapper.executeQuery(paramsMap);

        } catch (Exception e) {
            Throwable cause = e.getCause();
            String errorMsg = cause == null ? e.getMessage() : cause.getMessage();
            log.error("SQL执行失败，请检查SQL是否正常: {}", errorMsg);
            
            if (execType == null) {
                throw new CustomException(51000, errorMsg);
            } else {
                LinkedHashMap<String, Object> errorMap = new LinkedHashMap<>();
                errorMap.put("执行异常", "SQL执行失败：" + errorMsg);
                list.add(errorMap);
            }
        } finally {
            PageHelper.clearPage();
            JdbcDataSourceRouter.remove();
        }
        return new PageInfo<>(list, pageNum, pageSize);
    }

    @Override
    public int insert(String dataSourceId, String schema, String sql, Object params) {
        try {
            Map<String, Object> paramsMap = setParams(dataSourceId, schema, sql, params);
            return baseMapper.executeInsert(paramsMap);
        } catch (Exception e) {
            log.error("SQL执行失败，请检查SQL是否正常", e);
            throw new CustomException(ErrorStatusEnum.API_SQL_ERROR.getCode(), ErrorStatusEnum.API_SQL_ERROR.getMassage());
        } finally {
            JdbcDataSourceRouter.remove();
        }
    }

    @Override
    public int delete(String dataSourceId, String schema, String sql, Object params) {
        try {
            Map<String, Object> paramsMap = setParams(dataSourceId, schema, sql, params);
            return baseMapper.executeDelete(paramsMap);
        } catch (Exception e) {
            log.error("SQL执行失败，请检查SQL是否正常", e);
            throw new CustomException(ErrorStatusEnum.API_SQL_ERROR.getCode(), ErrorStatusEnum.API_SQL_ERROR.getMassage());
        } finally {
            JdbcDataSourceRouter.remove();
        }
    }

    @Override
    public int update(String dataSourceId, String schema, String sql, Object params) {
        try {
            Map<String, Object> paramsMap = setParams(dataSourceId, schema, sql, params);
            return baseMapper.executeUpdate(paramsMap);
        } catch (Exception e) {
            log.error("SQL执行失败，请检查SQL是否正常", e);
            throw new CustomException(ErrorStatusEnum.API_SQL_ERROR.getCode(), ErrorStatusEnum.API_SQL_ERROR.getMassage());
        } finally {
            JdbcDataSourceRouter.remove();
        }
    }

    private Map<String, Object> setParams(String dataSourceId, String schema, String sql, Object params) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(BaseConstant.BASE_SQL, sql);
        if (params instanceof Map) {
            paramsMap.putAll((Map<String, Object>) params);
        }
        
        if (schema != null && !schema.isEmpty()) {
            String dataSourceType = (String)paramsMap.getOrDefault(BaseConstant.DATA_SOURCE_TYPE, "");
            dataSourceId = String.format("%s:%s:%s", dataSourceId, dataSourceType, schema);
        }
        JdbcDataSourceRouter.setDataSourceKey(dataSourceId);
        return paramsMap;
    }

    private boolean checkPage(String sql) {
        String[] patterns = {
            "(?i)limit.*?\\d",      // mysql,tidb
            "(?i)offset.*?\\d",     // postgres, sqlserver2012+
            "(?i)ROWNUM.*?\\d"      // oracle
        };
        
        for (String pattern : patterns) {
            if (Pattern.compile(pattern).matcher(sql).find()) {
                return true;
            }
        }
        return false;
    }
}
