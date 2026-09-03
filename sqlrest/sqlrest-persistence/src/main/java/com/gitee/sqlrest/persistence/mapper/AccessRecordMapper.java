package com.gitee.sqlrest.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.sqlrest.common.dto.DateCount;
import com.gitee.sqlrest.common.dto.NameCount;
import com.gitee.sqlrest.persistence.entity.AccessRecordEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AccessRecordMapper extends BaseMapper<AccessRecordEntity> {

  @Select("SELECT "
      + "  (SELECT count(*) from SQLREST_API_ASSIGNMENT) as `totalCount`, "
      + "  (SELECT count(*) from SQLREST_API_ASSIGNMENT where `open`=1) as `openCount`, "
      + "  (SELECT count(*) from SQLREST_API_ASSIGNMENT where `status`=1) as `publishCount`, "
      + "  (SELECT count(*) from SQLREST_DATASOURCE) as `datasourceCount`")
  Map<String, Integer> selectCount();

  @Select("<script> "
      + "SELECT path as name,count(1) as count from SQLREST_ACCESS_RECORD "
      + "WHERE DATE_SUB( CURDATE(), INTERVAL ${days} DAY ) &lt;= date(create_time) "
      + "GROUP BY path "
      + "ORDER BY count DESC "
      + "LIMIT ${limit} "
      + "</script>")
  List<NameCount> getTopPathAccess(@Param("days") Integer days, @Param("limit") Integer limit);

  @Select("<script> "
      + "SELECT ip_addr as name,count(1) as count from SQLREST_ACCESS_RECORD "
      + "WHERE DATE_SUB( CURDATE(), INTERVAL ${days} DAY ) &lt;= date(create_time) "
      + "GROUP BY ip_addr "
      + "ORDER BY count DESC "
      + "LIMIT ${limit} "
      + "</script>")
  List<NameCount> getTopIpAddrAccess(@Param("days") Integer days, @Param("limit") Integer limit);

  @Select("<script> "
      + "SELECT "
      + "IFNULL((select name from SQLREST_APP_CLIENT t where app_key = client_key),'空') as name, "
      + "count(1) as count "
      + "from SQLREST_ACCESS_RECORD  "
      + "WHERE DATE_SUB( CURDATE(), INTERVAL ${days} DAY ) &lt;= date(create_time) "
      + "GROUP BY client_key "
      + "ORDER BY count DESC "
      + "LIMIT ${limit} "
      + "</script>")
  List<NameCount> getTopAppClientAccess(@Param("days") Integer days, @Param("limit") Integer limit);

  @Select("<script> "
      + "SELECT concat('HTTP(',status,')') as name,count(1) as count from SQLREST_ACCESS_RECORD \n"
      + "WHERE DATE_SUB( CURDATE(), INTERVAL ${days} DAY ) &lt;= date(create_time) \n"
      + "GROUP BY status \n"
      + "ORDER BY count DESC "
      + "</script>")
  List<NameCount> getHttpStatusCount(@Param("days") Integer days);

  @Select("<script> "
      + "SELECT "
      + " DATE_FORMAT(create_time,'%Y-%m-%d') as of_date , "
      + " count(*) as total, "
      + " sum(success) as success "
      + "FROM (  "
      + "  SELECT id,path,case when status=200 then 1 else 0 end as success, create_time FROM SQLREST_ACCESS_RECORD "
      + "  WHERE DATE_SUB( CURDATE(), INTERVAL ${days} DAY ) &lt;= date(create_time) "
      + " ) t  "
      + " GROUP BY of_date"
      + "</script>")
  List<DateCount> getDailyTrend(@Param("days") Integer days);
}
