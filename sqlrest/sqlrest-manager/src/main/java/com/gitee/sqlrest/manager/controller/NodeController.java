package com.gitee.sqlrest.manager.controller;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.core.dto.TopologyNodeResponse;
import com.gitee.sqlrest.core.service.NodeService;
import io.swagger.annotations.Api;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"节点信息接口"})
@RestController
@RequestMapping(value = Constants.MANGER_API_V1 + "/node")
public class NodeController {

  @Resource
  private NodeService nodeService;

  @GetMapping(value = "/gateway", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity<String> getApiGatewayPrefix() {
    return ResultEntity.success(nodeService.getGatewayAddr());
  }

  @GetMapping(value = "/prefix", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity<String> getApiPrefix() {
    return ResultEntity.success(nodeService.getApiPrefix());
  }

  // https://blog.csdn.net/weixin_39085822/article/details/114287774
  @GetMapping(value = "/topology", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity<TopologyNodeResponse> getNodesTopology() {
    return ResultEntity.success(nodeService.getNodesTopology());
  }
}
