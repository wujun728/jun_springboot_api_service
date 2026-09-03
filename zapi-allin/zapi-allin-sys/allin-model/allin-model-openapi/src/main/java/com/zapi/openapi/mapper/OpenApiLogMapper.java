package com.zapi.openapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.openapi.entity.OpenApiLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 接口调用日志Mapper接口
 */
@Mapper
public interface OpenApiLogMapper extends BaseMapper<OpenApiLog> {

    /**
     * 分页查询日志列表（带配置名称和应用名称）
     */
    Page<OpenApiLog> selectPageWithNames(Page<OpenApiLog> page,
                                          @Param("appId") Long appId,
                                          @Param("configId") Long configId,
                                          @Param("callType") Integer callType,
                                          @Param("status") Integer status);
}
