package com.zapi.grid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.grid.model.entity.Grid;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表格Mapper接口
 *
 * @author zapi
 * @since 2025-01-09
 */
@Mapper
public interface GridMapper extends BaseMapper<Grid> {
}
