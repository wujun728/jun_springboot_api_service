package com.gitee.sqlrest.executor.flowcontrol;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.common.enums.HttpMethodEnum;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.core.filter.FlowControlManger;
import com.gitee.sqlrest.persistence.dao.ApiAssignmentDao;
import com.gitee.sqlrest.persistence.entity.ApiAssignmentEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SentinelFlowControlManager implements FlowControlManger {

  @Resource
  private ApiAssignmentDao apiAssignmentDao;

  /*每分钟执行一次*/
  @EventListener(ApplicationReadyEvent.class)
  @Scheduled(cron = "${cron.flow.expression:0 0/1 * * * ?}")
  public void loadFlowRules() {
    try {
      doLoadFlowRules();
    } catch (Exception e) {
      log.error("load flow rules failed:{}", e.getMessage(), e);
    }
  }

  private void doLoadFlowRules() {
    List<FlowRule> rules = new ArrayList<>();
    for (ApiAssignmentEntity assignmentEntity : apiAssignmentDao.listFlowControlAll()) {
      if (assignmentEntity.getFlowCount() <= 0) {
        continue;
      }
      HttpMethodEnum method = assignmentEntity.getMethod();
      String path = assignmentEntity.getPath();
      String resourceName = Constants.getResourceName(method.name(), path);
      FlowRule rule = new FlowRule(resourceName);
      if (RuleConstant.FLOW_GRADE_THREAD == assignmentEntity.getFlowGrade()) {
        rule.setGrade(RuleConstant.FLOW_GRADE_THREAD);
      } else {
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
      }
      rule.setCount(assignmentEntity.getFlowCount());
      rule.setId(assignmentEntity.getId());
      rules.add(rule);
    }
    if (rules.size() > 0) {
      FlowRuleManager.loadRules(rules);
      log.info("Success refresh flow rules count: {}", rules.size());
    }
  }

  @Override
  public boolean checkFlowControl(String resourceName, HttpServletResponse response) throws IOException {
    Entry entry = null;
    try {
      entry = SphU.entry(resourceName, 0, EntryType.IN);
      return true;
    } catch (BlockException be) {
      this.handleBlockException(resourceName, response);
      return false;
    } finally {
      if (entry != null) {
        entry.exit(1);
      }
    }
  }

  public void handleBlockException(String resourceName, HttpServletResponse response)
      throws IOException {
    response.setStatus(Constants.SC_TOO_MANY_REQUESTS);
    ResultEntity result = ResultEntity.failed(ResponseErrorCode.ERROR_TOO_MANY_REQUESTS, resourceName);
    response.getWriter().append(JSONUtil.toJsonStr(result));
  }
}
