package com.jun.plugin.module.ext.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jun.plugin.module.ext.entity.ExtLog;
import com.jun.plugin.module.ext.vo.LogStatisticsTop10Vo;
import com.jun.plugin.module.ext.vo.LogStatisticsVo;

/**
 * <p>
 * 日志 Mapper 接口
 * </p>
 *
 * @author laker
 * @since 2021-08-16
 */
public interface ExtLogMapper extends BaseMapper<ExtLog> {


    @Select("select DATE_FORMAT(create_time,'%Y-%m-%d') date,count(*) value from ext_log where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= create_time  group by date ORDER BY create_time ")
    List<LogStatisticsVo> selectStatistics7Day();

    @Select("SELECT\n" +
            "\tw.ip,\n" +
            "\tcity,\n" +
            "\tcount( * ) \n" +
            "\tVALUE\t\n" +
            "FROM\n" +
            "\text_log w \n" +
            "WHERE\n" +
            "\tDATE_SUB( CURDATE( ), INTERVAL 1 day ) <= w.create_time \n" +
            "GROUP BY\n" +
            "\tw.ip \n" +
            "ORDER BY\n" +
            "\t\n" +
            "VALUE\n" +
            "DESC \n" +
            "LIMIT 10")
    List<LogStatisticsTop10Vo> selectStatisticsVisitsTop10IP();

    @Select("SELECT count(DISTINCT ip) from ext_log")
    int selectDistinctIp();

}
