package com.zapi.workflow.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.workflow.api.model.entity.FlowTestOrder;
import com.zapi.workflow.api.service.FlowTestOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程测试订单控制器
 * 提供订单的REST API接口
 */
@Api(tags = "流程测试订单管理")
@RestController
@RequestMapping("/api/flow-test-order")
public class FlowTestOrderController {
    
    @Autowired
    private FlowTestOrderService flowTestOrderService;
    
    /**
     * 创建订单
     */
    @ApiOperation("创建订单")
    @PostMapping
    public Result<FlowTestOrder> create(@RequestBody FlowTestOrder order) {
        try {
            FlowTestOrder createdOrder = flowTestOrderService.createOrder(order);
            return Result.success("创建订单成功", createdOrder);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据ID查询订单
     */
    @ApiOperation("根据ID查询订单")
    @GetMapping("/{id}")
    public Result<FlowTestOrder> getById(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long id) {
        try {
            FlowTestOrder order = flowTestOrderService.getById(id);
            if (order == null) {
                return Result.error("订单不存在");
            }
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据订单号查询订单
     */
    @ApiOperation("根据订单号查询订单")
    @GetMapping("/by-order-no/{orderNo}")
    public Result<FlowTestOrder> getByOrderNo(
            @ApiParam(value = "订单号", required = true) @PathVariable String orderNo) {
        try {
            FlowTestOrder order = flowTestOrderService.getByOrderNo(orderNo);
            if (order == null) {
                return Result.error("订单不存在");
            }
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新订单
     */
    @ApiOperation("更新订单")
    @PutMapping("/{id}")
    public Result<FlowTestOrder> update(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long id,
            @RequestBody FlowTestOrder order) {
        try {
            order.setId(id);
            FlowTestOrder updatedOrder = flowTestOrderService.updateOrder(order);
            return Result.success("更新订单成功", updatedOrder);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除订单
     */
    @ApiOperation("删除订单")
    @DeleteMapping("/{id}")
    public Result<String> delete(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long id) {
        try {
            boolean success = flowTestOrderService.removeById(id);
            if (success) {
                return Result.success("删除订单成功", "删除成功");
            } else {
                return Result.error("删除订单失败，订单可能不存在");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 批量删除订单
     */
    @ApiOperation("批量删除订单")
    @DeleteMapping("/batch")
    public Result<String> batchDelete(@RequestBody List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return Result.error("请选择要删除的订单");
            }
            boolean success = flowTestOrderService.removeByIds(ids);
            if (success) {
                return Result.success("批量删除订单成功", "删除成功");
            } else {
                return Result.error("批量删除订单失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询订单列表
     */
    @ApiOperation("分页查询订单列表")
    @GetMapping("/page")
    public Result<IPage<FlowTestOrder>> page(
            @ApiParam(value = "页码", required = true) @RequestParam(defaultValue = "1") Integer current,
            @ApiParam(value = "每页大小", required = true) @RequestParam(defaultValue = "10") Integer size,
            @ApiParam(value = "订单名称（模糊查询）") @RequestParam(required = false) String orderName) {
        try {
            Page<FlowTestOrder> page = new Page<>(current, size);
            IPage<FlowTestOrder> result = flowTestOrderService.pageQuery(page, orderName);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 查询所有订单
     */
    @ApiOperation("查询所有订单")
    @GetMapping("/all")
    public Result<List<FlowTestOrder>> getAll() {
        try {
            List<FlowTestOrder> orders = flowTestOrderService.list();
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 检查订单号是否存在
     */
    @ApiOperation("检查订单号是否存在")
    @GetMapping("/check-order-no")
    public Result<Boolean> checkOrderNo(
            @ApiParam(value = "订单号", required = true) @RequestParam String orderNo,
            @ApiParam(value = "排除的订单ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = flowTestOrderService.existsByOrderNo(orderNo, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
