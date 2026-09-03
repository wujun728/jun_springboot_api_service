package com.zapi.workbench.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.workbench.entity.WbAppEntry;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用条目Mapper接口
 */
@Mapper
public interface WbAppEntryMapper extends BaseMapper<WbAppEntry> {
}
