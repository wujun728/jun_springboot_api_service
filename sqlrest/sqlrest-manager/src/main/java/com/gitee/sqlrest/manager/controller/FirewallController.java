package com.gitee.sqlrest.manager.controller;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.core.dto.UpdateFirewallRulesRequest;
import com.gitee.sqlrest.core.gateway.FirewallFilterService;
import com.gitee.sqlrest.persistence.entity.FirewallRulesEntity;
import io.swagger.annotations.Api;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"防火墙管理接口"})
@RestController
@RequestMapping(value = Constants.MANGER_API_V1 + "/firewall")
public class FirewallController {

  @Resource
  private FirewallFilterService firewallFilterService;
  
  @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity<FirewallRulesEntity> queryFirewallRules() {
    return ResultEntity.success(firewallFilterService.getFirewallRules());
  }

  @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity saveFirewallRules(@RequestBody UpdateFirewallRulesRequest request) {
    firewallFilterService.updateFirewallRules(request);
    return ResultEntity.success();
  }

}
