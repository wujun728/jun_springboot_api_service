package com.zapi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.entity.UserSeal;
import com.zapi.service.UserSealService;
import com.zapi.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户印章Controller
 */
@Api(tags = "用户印章管理")
@RestController
@RequestMapping("/system/seal")
public class UserSealController {

    @Autowired
    private UserSealService userSealService;

    /**
     * 分页查询用户印章（只查询当前登录用户的印章）
     */
    @ApiOperation("分页查询用户印章")
    @GetMapping("/page")
    public Result<IPage<UserSeal>> pageUserSeals(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("印章名称") @RequestParam(required = false) String sealName,
            @ApiParam("状态") @RequestParam(required = false) Integer status) {
        // 从系统获取当前登录用户ID
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error("未获取到当前登录用户信息，请重新登录");
        }
        
        Page<UserSeal> page = new Page<>(current, size);
        IPage<UserSeal> result = userSealService.pageUserSeals(page, userId, sealName, status);
        return Result.success(result);
    }

    /**
     * 获取用户所有印章
     */
    @ApiOperation("获取用户所有印章")
    @GetMapping("/user/{userId}")
    public Result<List<UserSeal>> getUserSeals(
            @ApiParam("用户ID") @PathVariable Long userId) {
        List<UserSeal> seals = userSealService.getUserSeals(userId);
        return Result.success(seals);
    }

    /**
     * 获取用户启用的印章
     */
    @ApiOperation("获取用户启用的印章")
    @GetMapping("/user/{userId}/enabled")
    public Result<List<UserSeal>> getEnabledSeals(
            @ApiParam("用户ID") @PathVariable Long userId) {
        List<UserSeal> seals = userSealService.getEnabledSeals(userId);
        return Result.success(seals);
    }

    /**
     * 根据ID获取印章详情
     */
    @ApiOperation("根据ID获取印章详情")
    @GetMapping("/{id}")
    public Result<UserSeal> getSealById(
            @ApiParam("印章ID") @PathVariable Long id) {
        UserSeal seal = userSealService.getById(id);
        if (seal == null) {
            return Result.error("印章不存在");
        }
        return Result.success(seal);
    }

    /**
     * 创建印章（接收JSON格式数据）
     */
    @ApiOperation("创建印章")
    @PostMapping("/create")
    public Result<UserSeal> createSeal(@RequestBody CreateSealRequest request) {
        try {
            // 从系统上下文获取当前登录用户ID
            Long userId = UserContext.getCurrentUserId();
            if (userId == null) {
                return Result.error("未获取到当前登录用户信息，请重新登录");
            }
            
            UserSeal seal = userSealService.createSeal(
                userId,  // 使用从系统获取的用户ID
                request.getSealName(), 
                request.getSealType(), 
                request.getCenterText(), 
                request.getSortOrder(), 
                request.getRemark()
            );
            
            
            return Result.success(seal);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("创建印章失败：" + e.getMessage());
        }
    }

    /**
     * 创建印章请求对象
     */
    public static class CreateSealRequest {
        private String sealName;
        private String sealType;
        private String centerText;
        private Integer sortOrder;
        private String remark;

        public String getSealName() {
            return sealName;
        }

        public void setSealName(String sealName) {
            this.sealName = sealName;
        }

        public String getSealType() {
            return sealType;
        }

        public void setSealType(String sealType) {
            this.sealType = sealType;
        }

        public String getCenterText() {
            return centerText;
        }

        public void setCenterText(String centerText) {
            this.centerText = centerText;
        }

        public Integer getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    /**
     * 更新印章信息
     */
    @ApiOperation("更新印章信息")
    @PutMapping("/{id}")
    public Result<String> updateSeal(
            @ApiParam("印章ID") @PathVariable Long id,
            @RequestBody UserSeal userSeal) {
        userSeal.setId(id);
        boolean success = userSealService.updateById(userSeal);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 更新印章状态
     */
    @ApiOperation("更新印章状态")
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(
            @ApiParam("印章ID") @PathVariable Long id,
            @ApiParam("状态") @RequestParam Integer status) {
        try {
            boolean success = userSealService.updateStatus(id, status);
            return success ? Result.success("更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新印章排序
     */
    @ApiOperation("更新印章排序")
    @PutMapping("/{id}/sort")
    public Result<String> updateSortOrder(
            @ApiParam("印章ID") @PathVariable Long id,
            @ApiParam("排序") @RequestParam Integer sortOrder) {
        try {
            boolean success = userSealService.updateSortOrder(id, sortOrder);
            return success ? Result.success("更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除印章
     */
    @ApiOperation("删除印章")
    @DeleteMapping("/{id}")
    public Result<String> deleteSeal(
            @ApiParam("印章ID") @PathVariable Long id) {
        try {
            boolean success = userSealService.deleteSeal(id);
            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除印章
     */
    @ApiOperation("批量删除印章")
    @DeleteMapping("/batch")
    public Result<String> batchDelete(
            @ApiParam("印章ID列表") @RequestBody List<Long> ids) {
        try {
            boolean success = userSealService.batchDelete(ids);
            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

