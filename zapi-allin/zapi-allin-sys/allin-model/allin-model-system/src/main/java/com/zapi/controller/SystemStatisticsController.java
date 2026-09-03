package com.zapi.controller;

import com.zapi.common.Result;
import com.zapi.entity.SystemStatistics;
import com.zapi.service.SystemStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 系统访问统计Controller
 * 
 * @author ZAPI Team
 */
@Api(tags = "系统访问统计")
@RestController
@RequestMapping("/system/statistics")
public class SystemStatisticsController {

    @Autowired
    private SystemStatisticsService systemStatisticsService;

    /**
     * 获取今天的统计数据
     */
    @ApiOperation("获取今天的统计数据")
    @GetMapping("/today")
    public Result<SystemStatistics> getTodayStatistics() {
        try {
            SystemStatistics statistics = systemStatisticsService.getTodayStatistics();
            if (statistics == null) {
                // 如果今天还没有数据，返回一个初始化的对象
                statistics = new SystemStatistics();
                statistics.setStatDate(LocalDate.now());
                statistics.setLoginCount(0);
                statistics.setClickCount(0);
            }
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取今日统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取指定日期的统计数据
     */
    @ApiOperation("获取指定日期的统计数据")
    @GetMapping("/date/{statDate}")
    public Result<SystemStatistics> getStatisticsByDate(
            @ApiParam("统计日期，格式：yyyy-MM-dd") @PathVariable String statDate) {
        try {
            LocalDate date = LocalDate.parse(statDate);
            SystemStatistics statistics = systemStatisticsService.getStatisticsByDate(date);
            if (statistics == null) {
                statistics = new SystemStatistics();
                statistics.setStatDate(date);
                statistics.setLoginCount(0);
                statistics.setClickCount(0);
            }
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取最近N天的统计数据
     */
    @ApiOperation("获取最近N天的统计数据")
    @GetMapping("/recent/{days}")
    public Result<List<SystemStatistics>> getRecentStatistics(
            @ApiParam("天数") @PathVariable Integer days) {
        try {
            if (days == null || days <= 0) {
                days = 7; // 默认最近7天
            }
            if (days > 90) {
                days = 90; // 最多90天
            }
            List<SystemStatistics> statistics = systemStatisticsService.getRecentStatistics(days);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取最近统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取总的统计数据
     */
    @ApiOperation("获取总的统计数据")
    @GetMapping("/total")
    public Result<SystemStatistics> getTotalStatistics() {
        try {
            SystemStatistics statistics = systemStatisticsService.getTotalStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取总统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 手动记录登录（用于测试）
     */
    @ApiOperation("手动记录登录（测试用）")
    @PostMapping("/record-login")
    public Result<String> recordLogin() {
        try {
            systemStatisticsService.recordLogin();
            return Result.success("登录记录成功");
        } catch (Exception e) {
            return Result.error("登录记录失败：" + e.getMessage());
        }
    }

    /**
     * 手动记录点击（用于测试）
     */
    @ApiOperation("手动记录点击（测试用）")
    @PostMapping("/record-click")
    public Result<String> recordClick() {
        try {
            systemStatisticsService.recordClick();
            return Result.success("点击记录成功");
        } catch (Exception e) {
            return Result.error("点击记录失败：" + e.getMessage());
        }
    }
}

