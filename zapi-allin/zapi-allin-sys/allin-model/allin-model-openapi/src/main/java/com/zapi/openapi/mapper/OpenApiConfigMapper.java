package com.zapi.openapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.openapi.entity.OpenApiConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 开放接口配置Mapper接口
 */
@Mapper
public interface OpenApiConfigMapper extends BaseMapper<OpenApiConfig> {

    /**
     * 分页查询配置列表（带应用名称）
     */
    Page<OpenApiConfig> selectPageWithAppName(Page<OpenApiConfig> page,
                                               @Param("appId") Long appId,
                                               @Param("configName") String configName,
                                               @Param("callType") Integer callType,
                                               @Param("dataType") String dataType,
                                               @Param("status") Integer status);
}
