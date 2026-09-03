package com.gitee.sqlrest.core.gateway;

import static com.gitee.sqlrest.common.consts.Constants.GATEWAY_APPLICATION_NAME;

import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.common.enums.OnOffEnum;
import com.gitee.sqlrest.common.enums.WhiteBlackEnum;
import com.gitee.sqlrest.core.dto.UpdateFirewallRulesRequest;
import com.gitee.sqlrest.persistence.dao.FirewallRulesDao;
import com.gitee.sqlrest.persistence.entity.FirewallRulesEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class FirewallFilterService {

  private volatile FirewallRulesEntity firewallRules;

  @Resource
  private FirewallRulesDao firewallRulesDao;
  @Resource
  private DiscoveryClient discoveryClient;

  @PostConstruct
  public void refresh() {
    this.firewallRules = firewallRulesDao.getFirewallRules();
  }

  public boolean canAccess(String address) {
    if (null == this.firewallRules) {
      refresh();
    }

    if (OnOffEnum.OFF.equals(firewallRules.getStatus())) {
      return true;
    }

    String lists = Optional.ofNullable(firewallRules.getAddresses()).orElse(Strings.EMPTY);
    Set<String> addressSets = Arrays.asList(lists.split("\n"))
        .stream().map(t -> t.trim())
        .filter(t -> StringUtils.isNotBlank(t))
        .collect(Collectors.toSet());
    if (WhiteBlackEnum.WHITE.equals(firewallRules.getMode())) {
      return addressSets.contains(address);
    } else if (WhiteBlackEnum.BLACK.equals(firewallRules.getMode())) {
      return !addressSets.contains(address);
    } else {
      return false;
    }
  }

  public FirewallRulesEntity getFirewallRules() {
    return this.firewallRules;
  }

  public void updateFirewallRules(UpdateFirewallRulesRequest request) {
    firewallRulesDao.update(request.getStatus(), request.getMode(), request.getAddresses());
    refresh();

    // 通过HTTP接口同步到所有的gateway节点(如何保证强一致性呢?)
    RestTemplate restTemplate = new RestTemplate();
    List<ServiceInstance> instances = discoveryClient.getInstances(GATEWAY_APPLICATION_NAME);
    for (ServiceInstance instance : instances) {
      String url = String.format("%s://%s:%s/cache/refresh",
          instance.isSecure() ? "https" : "http",
          instance.getHost(), instance.getPort());
      restTemplate.getForEntity(url, ResultEntity.class);
      log.info("sync ip rule cache to gateway node: {}", instance.getHost());
    }
  }

}
