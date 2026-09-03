package com.zapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.entity.SystemStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;

/**
 * 系统访问统计 Mapper 接口
 * 
 * @author ZAPI Team
 */
@Mapper
public interface SystemStatisticsMapper extends BaseMapper<SystemStatistics> {

    /**
     * 增加登录次数
     * @param statDate 统计日期
     * @return 影响行数
     */
    @Update("INSERT INTO sys_statistics (stat_date, login_count, click_count) " +
            "VALUES (#{statDate}, 1, 0) " +
            "ON DUPLICATE KEY UPDATE login_count = login_count + 1")
    int incrementLoginCount(@Param("statDate") LocalDate statDate);

    /**
     * 增加点击次数
     * @param statDate 统计日期
     * @return 影响行数
     */
    @Update("INSERT INTO sys_statistics (stat_date, login_count, click_count) " +
            "VALUES (#{statDate}, 0, 1) " +
            "ON DUPLICATE KEY UPDATE click_count = click_count + 1")
    int incrementClickCount(@Param("statDate") LocalDate statDate);
}

