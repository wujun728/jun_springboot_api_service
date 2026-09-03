package com.zapi.grid.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.grid.model.entity.Grid;

/**
 * 表格Service接口
 *
 * @author zapi
 * @since 2025-01-09
 */
public interface GridService extends IService<Grid> {
    
    /**
     * 根据应用条目ID获取表格
     *
     * @param entryId 应用条目ID
     * @return 表格信息
     */
    Grid getByEntryId(Long entryId);
}
