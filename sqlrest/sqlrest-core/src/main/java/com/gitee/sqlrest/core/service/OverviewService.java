package com.gitee.sqlrest.core.service;

import com.gitee.sqlrest.common.dto.DateCount;
import com.gitee.sqlrest.common.dto.NameCount;
import com.gitee.sqlrest.persistence.mapper.AccessRecordMapper;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class OverviewService {

  @Resource
  private AccessRecordMapper accessRecordMapper;

  public Map<String, Integer> count() {
    return accessRecordMapper.selectCount();
  }

  public List<DateCount> trend(Integer days) {
    return accessRecordMapper.getDailyTrend(days > 0 ? days - 1 : days);
  }

  public List<NameCount> httpStatus(Integer days) {
    return accessRecordMapper.getHttpStatusCount(days > 0 ? days - 1 : days);
  }

  public List<NameCount> topPath(Integer days, Integer n) {
    return accessRecordMapper.getTopPathAccess(days > 0 ? days - 1 : days, n);
  }

  public List<NameCount> topAddr(Integer days, Integer n) {
    return accessRecordMapper.getTopIpAddrAccess(days > 0 ? days - 1 : days, n);
  }

  public List<NameCount> topClient(Integer days, Integer n) {
    return accessRecordMapper.getTopAppClientAccess(days > 0 ? days - 1 : days, n);
  }
}
