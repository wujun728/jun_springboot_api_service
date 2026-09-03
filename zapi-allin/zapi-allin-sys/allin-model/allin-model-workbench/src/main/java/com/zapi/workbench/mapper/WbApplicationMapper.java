package com.zapi.workbench.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.workbench.entity.WbApplication;
import org.apache.ibatis.annotations.Mapper;

/**
 * 外部应用Mapper接口
 */
@Mapper
public interface WbApplicationMapper extends BaseMapper<WbApplication> {
}