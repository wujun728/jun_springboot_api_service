package com.gitee.sqlrest.persistence.dao;

import com.gitee.sqlrest.common.enums.OnOffEnum;
import com.gitee.sqlrest.common.enums.WhiteBlackEnum;
import com.gitee.sqlrest.persistence.entity.FirewallRulesEntity;
import com.gitee.sqlrest.persistence.mapper.FirewallRulesMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class FirewallRulesDao {

  private static final Long ID = 1L;

  @Resource
  private FirewallRulesMapper firewallRulesMapper;

  public FirewallRulesEntity getFirewallRules() {
    return firewallRulesMapper.selectById(ID);
  }

  public void turnOn() {
    firewallRulesMapper.updateStatus(ID, OnOffEnum.ON);
  }

  public void turnOff() {
    firewallRulesMapper.updateStatus(ID, OnOffEnum.OFF);
  }

  public void update(OnOffEnum status, WhiteBlackEnum mode, String addresses) {
    firewallRulesMapper.updateById(
        FirewallRulesEntity.builder()
            .id(ID)
            .status(status)
            .mode(mode)
            .addresses(addresses)
            .build());
  }
}
