package com.zapi.workflow.service.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.workflow.service.model.dto.CcDisplayDTO;
import com.zapi.workflow.service.model.dto.CompletedTaskDisplayDTO;
import com.zapi.workflow.service.model.dto.CompleteTaskDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionQueryDTO;
import com.zapi.workflow.service.model.dto.ProcessInstanceQueryDTO;
import com.zapi.workflow.service.model.dto.ProcessInstanceDisplayDTO;
import com.zapi.workflow.service.model.dto.ProcessInstanceDetailDTO;
import com.zapi.workflow.service.model.dto.RevokeProcessDTO;
import com.zapi.workflow.service.model.dto.TaskDisplayDTO;
import com.zapi.workflow.service.model.dto.StartProcessDTO;
import com.zapi.workflow.service.model.entity.WfProcessDefinition;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import com.zapi.workflow.service.model.entity.WfTask;
import com.zapi.workflow.service.model.entity.WfHistoryActivity;
import com.zapi.workflow.service.service.WfProcessDefinitionService;
import com.zapi.workflow.service.service.WfProcessInstanceService;
import com.zapi.workflow.service.service.WfTaskService;
import com.zapi.workflow.service.service.WfHistoryActivityService;
import com.zapi.workflow.service.service.WfCcRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 工作流管理控制器
 * 提供流程定义、流程实例、任务管理等功能的REST接口
 * 包含流程的创建、启动、审批、查询等完整生命周期管理
 */
@RestController
@RequestMapping("/api/workflow")
@RequiredArgsConstructor
@Api(tags = "工作流管理接口")
public class WorkflowController {

    /**
     * 流程定义服务
     * 负责流程模板的管理
     */
    private final WfProcessDefinitionService processDefinitionService;

    /**
     * 流程实例服务
     * 负责流程运行时的管理
     */
    private final WfProcessInstanceService processInstanceService;

    /**
     * 任务服务
     * 负责审批任务的管理
     */
    private final WfTaskService taskService;

    /**
     * 历史活动服务
     * 负责流程历史记录的管理
     */
    private final WfHistoryActivityService historyActivityService;

    /**
     * 抄送记录服务
     * 负责流程抄送记录的管理
     */
    private final WfCcRecordService ccRecordService;

    @PostMapping("/deploy")
    @ApiOperation("部署流程定义")
    public Result<WfProcessDefinition> deployProcess(@Valid @RequestBody ProcessDefinitionDTO definitionDTO) {
        WfProcessDefinition result = processDefinitionService.deploy(definitionDTO);
        return Result.success("流程定义部署成功", result);
    }

    @GetMapping("/definitions")
    @ApiOperation("分页查询流程定义")
    public Result<IPage<WfProcessDefinition>> pageQueryDefinitions(ProcessDefinitionQueryDTO queryDTO) {
        // 设置分页参数的默认值和限制
        if (queryDTO.getPageNum() == null || queryDTO.getPageNum() < 1) {
            queryDTO.setPageNum(1);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
            queryDTO.setPageSize(10);
        }
        // 限制每页最大数量，防止查询过多数据
        if (queryDTO.getPageSize() > 100) {
            queryDTO.setPageSize(100);
        }

        Page<WfProcessDefinition> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<WfProcessDefinition> result = processDefinitionService.pageQuery(page, queryDTO);
        return Result.success("查询成功", result);
    }

    @GetMapping("/definitions/bound-forms")
    @ApiOperation("获取已绑定的业务表单ID列表")
    public Result<List<String>> getBoundFormIds() {
        List<String> result = processDefinitionService.getBoundFormIds();
        return Result.success("获取已绑定表单列表成功", result);
    }

    @GetMapping("/definitions/{id}")
    @ApiOperation("获取流程定义详情")
    public Result<ProcessDefinitionDTO> getProcessDefinition(@PathVariable String id) {
        ProcessDefinitionDTO result = processDefinitionService.getDefinitionDTOById(id);
        return Result.success("获取流程定义详情成功", result);
    }

    @PutMapping("/definitions/{id}")
    @ApiOperation("更新流程定义")
    public Result<WfProcessDefinition> updateProcessDefinition(@PathVariable String id,
                                                             @Valid @RequestBody ProcessDefinitionDTO definitionDTO) {
        // 设置ID确保更新正确的记录
        definitionDTO.setId(id);
        WfProcessDefinition result = processDefinitionService.updateDefinition(definitionDTO);
        return Result.success("流程定义更新成功", result);
    }

    @PutMapping("/definitions/{id}/status")
    @ApiOperation("更新流程定义状态")
    public Result updateProcessStatus(@PathVariable String id, @RequestParam Integer status) {
        processDefinitionService.updateStatus(id, status);
        return Result.success("流程定义状态更新成功");
    }

    @DeleteMapping("/definitions/{id}")
    @ApiOperation("删除流程定义")
    public Result deleteProcessDefinition(@PathVariable String id) {
        processDefinitionService.removeById(id);
        return Result.success("流程定义删除成功");
    }

    @PostMapping("/processes/{definitionId}/start")
    @ApiOperation("启动流程实例")
    public Result<WfProcessInstance> startProcess(@PathVariable String definitionId,
                                                @Valid @RequestBody StartProcessDTO startProcessDTO) {
        startProcessDTO.setProcessDefinitionId(definitionId);
        WfProcessInstance result = processInstanceService.startProcess(startProcessDTO);
        return Result.success("流程实例启动成功", result);
    }

    @PostMapping("/tasks/{taskId}/complete")
    @ApiOperation("完成任务")
    public Result completeTask(@PathVariable String taskId,
                                   @Valid @RequestBody CompleteTaskDTO completeTaskDTO) {
        completeTaskDTO.setTaskId(taskId);
        taskService.completeTask(completeTaskDTO);
        return Result.success("任务完成成功");
    }

    @GetMapping("/tasks/display")
    @ApiOperation("查询用户待办任务（增强显示信息）")
    public Result<IPage<TaskDisplayDTO>> getUserTasksWithDisplayInfo(@RequestParam String assignee,
                                                                    @RequestParam(required = false) String appId,
                                                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<TaskDisplayDTO> page = new Page<>(pageNum, pageSize);
        IPage<TaskDisplayDTO> result = taskService.getUserTasksWithDisplayInfo(page, assignee, appId);
        return Result.success("查询用户待办任务成功", result);
    }

    @GetMapping("/cc/records")
    @ApiOperation("查询抄送我的")
    public Result<IPage<CcDisplayDTO>> getUserCcRecords(@RequestParam String ccUser,
                                                        @RequestParam(required = false) String appId,
                                                        @RequestParam(defaultValue = "1") Integer pageNum,
                                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<CcDisplayDTO> page = new Page<>(pageNum, pageSize);
        IPage<CcDisplayDTO> result = historyActivityService.getUserCcRecordsWithDisplayInfo(page, ccUser, appId);
        return Result.success("查询抄送记录成功", result);
    }

    @GetMapping("/cc/unread/count")
    @ApiOperation("获取未读抄送数量")
    public Result<Long> getUnreadCcCount(@RequestParam Long userId) {
        long count = ccRecordService.getUnreadCount(userId);
        return Result.success("获取未读抄送数量成功", count);
    }

    @GetMapping("/completed/tasks")
    @ApiOperation("查询我的已办")
    public Result<IPage<CompletedTaskDisplayDTO>> getUserCompletedTasks(@RequestParam String operator,
                                                                        @RequestParam(required = false) String appId,
                                                                        @RequestParam(defaultValue = "1") Integer pageNum,
                                                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<CompletedTaskDisplayDTO> page = new Page<>(pageNum, pageSize);
        IPage<CompletedTaskDisplayDTO> result = historyActivityService.getUserCompletedTasksWithDisplayInfo(page, operator, appId);
        return Result.success("查询已办任务成功", result);
    }

    @GetMapping("/instances")
    @ApiOperation("分页查询流程实例")
    public Result<IPage<WfProcessInstance>> pageQueryInstances(ProcessInstanceQueryDTO queryDTO) {
        // 设置分页参数的默认值和限制
        if (queryDTO.getPageNum() == null || queryDTO.getPageNum() < 1) {
            queryDTO.setPageNum(1);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
            queryDTO.setPageSize(10);
        }
        // 限制每页最大数量，防止查询过多数据
        if (queryDTO.getPageSize() > 100) {
            queryDTO.setPageSize(100);
        }

        Page<WfProcessInstance> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<WfProcessInstance> result = processInstanceService.pageQuery(page, queryDTO);
        return Result.success("查询流程实例成功", result);
    }

    @GetMapping("/instances/display")
    @ApiOperation("查询我的申请（增强显示信息）")
    public Result<IPage<ProcessInstanceDisplayDTO>> pageQueryInstancesWithDisplayInfo(ProcessInstanceQueryDTO queryDTO,
                                                                                      @RequestParam String startUser) {
        // 设置分页参数的默认值和限制
        if (queryDTO.getPageNum() == null || queryDTO.getPageNum() < 1) {
            queryDTO.setPageNum(1);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
            queryDTO.setPageSize(10);
        }
        // 限制每页最大数量，防止查询过多数据
        if (queryDTO.getPageSize() > 100) {
            queryDTO.setPageSize(100);
        }

        // 强制设置为当前用户发起的流程实例
        queryDTO.setStartUser(startUser);

        Page<ProcessInstanceDisplayDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<ProcessInstanceDisplayDTO> result = processInstanceService.pageQueryWithDisplayInfo(page, queryDTO);
        return Result.success("查询我的申请成功", result);
    }

    @GetMapping("/instances/{id}")
    @ApiOperation("获取流程实例详情")
    public Result<WfProcessInstance> getProcessInstance(@PathVariable String id) {
        WfProcessInstance result = processInstanceService.getById(id);
        if (result == null) {
            return Result.error("流程实例不存在");
        }
        return Result.success("获取流程实例详情成功", result);
    }

    @GetMapping("/instances/{id}/activities")
    @ApiOperation("获取流程实例历史活动")
    public Result<List<WfHistoryActivity>> getProcessInstanceActivities(@PathVariable String id) {
        // 检查流程实例是否存在
        WfProcessInstance instance = processInstanceService.getById(id);
        if (instance == null) {
            return Result.error("流程实例不存在");
        }

        List<WfHistoryActivity> activities = historyActivityService.getProcessInstanceHistory(id);
        return Result.success("获取流程实例历史活动成功", activities);
    }

    @GetMapping("/instances/{id}/complete-detail")
    @ApiOperation("获取流程实例完整详情")
    public Result<ProcessInstanceDetailDTO> getProcessInstanceCompleteDetail(@PathVariable String id) {
        try {
            ProcessInstanceDetailDTO detailDTO = processInstanceService.getCompleteDetail(id);
            return Result.success("获取流程实例完整详情成功", detailDTO);
        } catch (RuntimeException e) {
            return Result.error("获取流程实例详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/instances/{instanceId}/revoke")
    @ApiOperation("撤回流程实例")
    public Result revokeProcessInstance(@PathVariable String instanceId,
                                        @Valid @RequestBody RevokeProcessDTO revokeProcessDTO) {
        try {
            // 确保请求中的instanceId与路径参数一致
            revokeProcessDTO.setInstanceId(instanceId);
            processInstanceService.revokeProcess(revokeProcessDTO);
            return Result.success("流程撤回成功");
        } catch (RuntimeException e) {
            return Result.error("流程撤回失败: " + e.getMessage());
        }
    }
}
