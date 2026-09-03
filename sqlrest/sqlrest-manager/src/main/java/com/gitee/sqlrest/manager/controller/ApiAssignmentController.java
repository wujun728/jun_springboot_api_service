package com.gitee.sqlrest.manager.controller;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.PageResult;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.common.enums.DataTypeFormatEnum;
import com.gitee.sqlrest.common.enums.NamingStrategyEnum;
import com.gitee.sqlrest.core.dto.ApiAssignmentBaseResponse;
import com.gitee.sqlrest.core.dto.ApiAssignmentSaveRequest;
import com.gitee.sqlrest.core.dto.ApiDebugExecuteRequest;
import com.gitee.sqlrest.core.dto.AssignmentSearchRequest;
import com.gitee.sqlrest.core.dto.NameValueBaseResponse;
import com.gitee.sqlrest.core.dto.NameValueRemarkResponse;
import com.gitee.sqlrest.core.service.ApiAssignmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"API配置接口"})
@RestController
@RequestMapping(value = Constants.MANGER_API_V1 + "/assignment")
public class ApiAssignmentController {

  @Resource
  private ApiAssignmentService apiAssignmentService;

  @ApiOperation(value = "获取自动提示列表")
  @GetMapping(value = "/completions", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity completions() {
    return ResultEntity.success(apiAssignmentService.completions());
  }

  @ApiOperation(value = "响应属性命名策略")
  @GetMapping(value = "/response-naming-strategy", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity responseNamingStrategy() {
    return ResultEntity.success(
        Arrays.stream(NamingStrategyEnum.values())
            .map(
                e ->
                    NameValueBaseResponse.builder()
                        .key(e)
                        .value(e.getDescription())
                        .build()
            ).collect(Collectors.toList())
    );
  }

  @ApiOperation(value = "响应数据类型格式")
  @GetMapping(value = "/response-type-format", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity responseTypeFormat() {
    return ResultEntity.success(
        Arrays.stream(DataTypeFormatEnum.values())
            .map(
                e ->
                    NameValueRemarkResponse.builder()
                        .key(e)
                        .value(e.getDefault())
                        .remark(e.getClassName())
                        .build()
            ).collect(Collectors.toList())
    );
  }

  @ApiOperation(value = "获取SQL中的入参列表")
  @PostMapping(value = "/parse", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity parse(@RequestParam("sql") String sql) {
    return ResultEntity.success(apiAssignmentService.parseSqlParams(sql));
  }

  @ApiOperation(value = "调试API配置")
  @PostMapping(value = "/debug", produces = MediaType.APPLICATION_JSON_VALUE)
  public void debug(@RequestBody ApiDebugExecuteRequest request, HttpServletResponse response) {
    apiAssignmentService.debugExecute(request, response);
  }

  @ApiOperation(value = "添加API配置")
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity create(@RequestBody ApiAssignmentSaveRequest request) {
    Long id = apiAssignmentService.createAssignment(request);
    return ResultEntity.success(id);
  }

  @ApiOperation(value = "更新API配置")
  @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity update(@RequestBody ApiAssignmentSaveRequest request) {
    apiAssignmentService.updateAssignment(request);
    return ResultEntity.success();
  }

  @ApiOperation(value = "查看API配置")
  @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity detail(@PathVariable("id") Long id) {
    return ResultEntity.success(apiAssignmentService.detailAssignment(id));
  }

  @ApiOperation(value = "测试API执行")
  @PostMapping(value = "/test/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public void test(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
    apiAssignmentService.testAssignment(id, request, response);
  }

  @ApiOperation(value = "删除API配置")
  @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity delete(@PathVariable("id") Long id) {
    apiAssignmentService.deleteAssignment(id);
    return ResultEntity.success();
  }

  @ApiOperation(value = "查询API配置列表")
  @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public PageResult<ApiAssignmentBaseResponse> listAll(@RequestBody AssignmentSearchRequest request) {
    return apiAssignmentService.listAll(request);
  }

  @ApiOperation(value = "开放")
  @PutMapping(value = "/open/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity deploy(@PathVariable("id") Long id, @RequestParam("open") Boolean open) {
    apiAssignmentService.makeOpen(id, open);
    return ResultEntity.success();
  }

  @ApiOperation(value = "发布")
  @PutMapping(value = "/deploy/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity deploy(@PathVariable("id") Long id) {
    apiAssignmentService.deployAssignment(id);
    return ResultEntity.success();
  }

  @ApiOperation(value = "下线")
  @PutMapping(value = "/retire/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResultEntity retire(@PathVariable("id") Long id) {
    apiAssignmentService.retireAssignment(id);
    return ResultEntity.success();
  }
}
