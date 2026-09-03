package com.gitee.sqlrest.gateway.controller;

import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.core.gateway.FirewallFilterService;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class RefreshCacheController {

  @Resource
  private FirewallFilterService firewallFilterService;

  @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity refresh() {
    firewallFilterService.refresh();
    return ResultEntity.success();
  }

}
