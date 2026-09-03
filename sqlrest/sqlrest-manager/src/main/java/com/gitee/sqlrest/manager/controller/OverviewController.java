package com.gitee.sqlrest.manager.controller;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.DateCount;
import com.gitee.sqlrest.common.dto.NameCount;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.core.service.OverviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"统计相关接口"})
@RestController
@RequestMapping(value = Constants.MANGER_API_V1 + "/overview")
public class OverviewController {

  @Resource
  private OverviewService overviewService;

  @ApiOperation(value = "计数统计")
  @GetMapping(value = "/counter", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity count() {
    return ResultEntity.success(overviewService.count());
  }

  @ApiOperation(value = "趋势统计")
  @GetMapping(value = "/trend/{days}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity<List<DateCount>> trend(@PathVariable("days") Integer days) {
    return ResultEntity.success(overviewService.trend(days));
  }

  @ApiOperation(value = "HTTP状态统计")
  @GetMapping(value = "/ratio/{days}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity httpStatus(@PathVariable("days") Integer days) {
    return ResultEntity.success(overviewService.httpStatus(days));
  }

  @ApiOperation(value = "路径TOP")
  @GetMapping(value = "/top/path/{days}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity<List<NameCount>> topPath(@PathVariable("days") Integer days, @RequestParam("n") Integer n) {
    return ResultEntity.success(overviewService.topPath(days, n));
  }

  @ApiOperation(value = "地址TOP")
  @GetMapping(value = "/top/addr/{days}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity<List<NameCount>> topAddr(@PathVariable("days") Integer days, @RequestParam("n") Integer n) {
    return ResultEntity.success(overviewService.topAddr(days, n));
  }

  @ApiOperation(value = "客户端TOP")
  @GetMapping(value = "/top/client/{days}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity<List<NameCount>> topClient(@PathVariable("days") Integer days, @RequestParam("n") Integer n) {
    return ResultEntity.success(overviewService.topClient(days, n));
  }

}
