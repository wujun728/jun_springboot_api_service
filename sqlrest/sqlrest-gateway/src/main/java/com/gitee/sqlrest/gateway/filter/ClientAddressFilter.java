package com.gitee.sqlrest.gateway.filter;

import cn.hutool.json.JSONUtil;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.core.gateway.FirewallFilterService;
import com.gitee.sqlrest.common.dto.ResultEntity;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ClientAddressFilter implements GlobalFilter, Ordered {

  @Resource
  private FirewallFilterService firewallFilterService;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    ServerHttpResponse response = exchange.getResponse();
    String path = request.getURI().getPath();
    String method = request.getMethod().name();
    String clientHostAddr = request.getRemoteAddress().getHostString();
    if (!firewallFilterService.canAccess(clientHostAddr)) {
      log.info("Forbidden access for client : {}, path : {}, method : {}", clientHostAddr, path, method);
      ResultEntity data = ResultEntity.failed(ResponseErrorCode.ERROR_CLIENT_FORBIDDEN, clientHostAddr);
      String json = JSONUtil.toJsonStr(data);
      DataBuffer wrap = response.bufferFactory().wrap(json.getBytes());
      return response.writeWith(Mono.just(wrap));
    } else {
      log.info("access api from client : {}, path : {}, method : {}", clientHostAddr, path, method);
    }

    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
