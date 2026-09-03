package com.zapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.entity.SystemStatistics;

import java.time.LocalDate;
import java.util.List;

/**
 * 系统访问统计服务接口
 * 
 * @author ZAPI Team
 */
public interface SystemStatisticsService extends IService<SystemStatistics> {

    /**
     * 记录登录访问
     */
    void recordLogin();

    /**
     * 记录系统点击
     */
    void recordClick();

    /**
     * 获取指定日期的统计数据
     * @param statDate 统计日期
     * @return 统计数据
     */
    SystemStatistics getStatisticsByDate(LocalDate statDate);

    /**
     * 获取今天的统计数据
     * @return 统计数据
     */
    SystemStatistics getTodayStatistics();

    /**
     * 获取最近N天的统计数据
     * @param days 天数
     * @return 统计数据列表
     */
    List<SystemStatistics> getRecentStatistics(int days);

    /**
     * 获取总的统计数据
     * @return 统计数据
     */
    SystemStatistics getTotalStatistics();
}

