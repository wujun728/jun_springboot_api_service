package com.zapi.grid.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.grid.mapper.GridMapper;
import com.zapi.grid.model.entity.Grid;
import com.zapi.grid.service.GridService;
import org.springframework.stereotype.Service;

/**
 * 表格Service实现类
 *
 * @author zapi
 * @since 2025-01-09
 */
@Service
public class GridServiceImpl extends ServiceImpl<GridMapper, Grid> implements GridService {

    @Override
    public Grid getByEntryId(Long entryId) {
        return lambdaQuery()
                .eq(Grid::getEntryId, entryId)
                .eq(Grid::getDelFlag, 0)
                .one();
    }
}
