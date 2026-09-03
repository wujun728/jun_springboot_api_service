package com.zapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.SystemStatistics;
import com.zapi.mapper.SystemStatisticsMapper;
import com.zapi.service.SystemStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 系统访问统计服务实现类
 * 
 * @author ZAPI Team
 */
@Slf4j
@Service
public class SystemStatisticsServiceImpl extends ServiceImpl<SystemStatisticsMapper, SystemStatistics> 
        implements SystemStatisticsService {

    @Override
    public void recordLogin() {
        try {
            LocalDate today = LocalDate.now();
            int rows = baseMapper.incrementLoginCount(today);
            log.info("记录登录访问成功，日期: {}, 影响行数: {}", today, rows);
        } catch (Exception e) {
            log.error("记录登录访问失败", e);
        }
    }

    @Override
    public void recordClick() {
        try {
            LocalDate today = LocalDate.now();
            int rows = baseMapper.incrementClickCount(today);
            log.debug("记录系统点击成功，日期: {}, 影响行数: {}", today, rows);
        } catch (Exception e) {
            log.error("记录系统点击失败", e);
        }
    }

    @Override
    public SystemStatistics getStatisticsByDate(LocalDate statDate) {
        QueryWrapper<SystemStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stat_date", statDate);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public SystemStatistics getTodayStatistics() {
        return getStatisticsByDate(LocalDate.now());
    }

    @Override
    public List<SystemStatistics> getRecentStatistics(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        
        QueryWrapper<SystemStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("stat_date", startDate, endDate);
        queryWrapper.orderByDesc("stat_date");
        
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public SystemStatistics getTotalStatistics() {
        // 获取所有统计数据并汇总
        List<SystemStatistics> allStatistics = baseMapper.selectList(null);
        
        SystemStatistics total = new SystemStatistics();
        total.setStatDate(LocalDate.now());
        total.setLoginCount(0);
        total.setClickCount(0);
        
        for (SystemStatistics stat : allStatistics) {
            total.setLoginCount(total.getLoginCount() + (stat.getLoginCount() != null ? stat.getLoginCount() : 0));
            total.setClickCount(total.getClickCount() + (stat.getClickCount() != null ? stat.getClickCount() : 0));
        }
        
        return total;
    }
}

