package com.zapi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.entity.ContractInfo;
import com.zapi.service.ContractInfoService;
import com.zapi.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合同信息Controller
 */
@Api(tags = "合同管理")
@RestController
@RequestMapping("/contract/info")
public class ContractInfoController {

    @Autowired
    private ContractInfoService contractInfoService;

    /**
     * 分页查询合同信息
     */
    @ApiOperation("分页查询合同信息")
    @GetMapping("/page")
    public Result<IPage<ContractInfo>> pageContracts(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("合同名称") @RequestParam(required = false) String contractName,
            @ApiParam("合同状态") @RequestParam(required = false) String contractStatus) {
        try {
            // 从系统获取当前登录用户ID
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("未获取到当前登录用户信息，请重新登录");
            }
            
            Page<ContractInfo> page = new Page<>(current, size);
            IPage<ContractInfo> result = contractInfoService.pageContracts(page, userId, contractName, contractStatus);
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取合同详情
     */
    @ApiOperation("根据ID获取合同详情")
    @GetMapping("/{id}")
    public Result<ContractInfo> getContractById(
            @ApiParam("合同ID") @PathVariable Long id) {
        ContractInfo contract = contractInfoService.getById(id);
        if (contract == null) {
            return Result.error("合同不存在");
        }
        return Result.success(contract);
    }

    /**
     * 创建合同（草稿）
     */
    @ApiOperation("创建合同")
    @PostMapping("/create")
    public Result<ContractInfo> createContract(@RequestBody ContractInfo contractInfo) {
        try {
            // 从系统获取当前登录用户ID
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("未获取到当前登录用户信息，请重新登录");
            }
            
            contractInfo.setInitiatorId(userId);
            ContractInfo result = contractInfoService.createContract(contractInfo);
            return Result.success("合同创建成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("创建合同失败：" + e.getMessage());
        }
    }

    /**
     * 更新合同
     */
    @ApiOperation("更新合同")
    @PutMapping("/{id}")
    public Result<String> updateContract(
            @ApiParam("合同ID") @PathVariable Long id,
            @RequestBody ContractInfo contractInfo) {
        try {
            ContractInfo existing = contractInfoService.getById(id);
            if (existing == null) {
                return Result.error("合同不存在");
            }
            
            // 只有草稿状态的合同才能修改
            if (!ContractInfo.Status.DRAFT.equals(existing.getContractStatus())) {
                return Result.error("只有草稿状态的合同才能修改");
            }
            
            contractInfo.setId(id);
            contractInfoService.updateById(contractInfo);
            return Result.success("合同更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新合同失败：" + e.getMessage());
        }
    }

    /**
     * 提交审核
     */
    @ApiOperation("提交审核")
    @PostMapping("/{id}/submit")
    public Result<String> submitForReview(
            @ApiParam("合同ID") @PathVariable Long id,
            @RequestBody SubmitReviewRequest request) {
        try {
            boolean success = contractInfoService.submitForReview(id, request.getReviewerId(), request.getReviewerName());
            return success ? Result.success("提交审核成功") : Result.error("提交审核失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("提交审核失败：" + e.getMessage());
        }
    }

    /**
     * 审核合同
     */
    @ApiOperation("审核合同")
    @PostMapping("/{id}/review")
    public Result<String> reviewContract(
            @ApiParam("合同ID") @PathVariable Long id,
            @RequestBody ReviewRequest request) {
        try {
            Long reviewerId = getCurrentUserId();
            if (reviewerId == null) {
                return Result.error("未获取到当前登录用户信息，请重新登录");
            }
            
            // TODO: 获取审核人姓名
            String reviewerName = "审核人"; // 可以从用户服务获取
            
            boolean success = contractInfoService.reviewContract(
                id, reviewerId, reviewerName, request.getApproved(), request.getReviewComment());
            return success ? Result.success("审核成功") : Result.error("审核失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("审核失败：" + e.getMessage());
        }
    }

    /**
     * 签署合同
     */
    @ApiOperation("签署合同")
    @PostMapping("/{id}/sign")
    public Result<String> signContract(
            @ApiParam("合同ID") @PathVariable Long id) {
        try {
            Long signerId = getCurrentUserId();
            if (signerId == null) {
                return Result.error("未获取到当前登录用户信息，请重新登录");
            }
            
            // TODO: 获取签署人姓名
            String signerName = "签署人"; // 可以从用户服务获取
            
            boolean success = contractInfoService.signContract(id, signerId, signerName);
            return success ? Result.success("签署成功") : Result.error("签署失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("签署失败：" + e.getMessage());
        }
    }

    /**
     * 归档合同
     */
    @ApiOperation("归档合同")
    @PostMapping("/{id}/archive")
    public Result<String> archiveContract(
            @ApiParam("合同ID") @PathVariable Long id) {
        try {
            boolean success = contractInfoService.archiveContract(id);
            return success ? Result.success("归档成功") : Result.error("归档失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("归档失败：" + e.getMessage());
        }
    }

    /**
     * 删除合同
     */
    @ApiOperation("删除合同")
    @DeleteMapping("/{id}")
    public Result<String> deleteContract(
            @ApiParam("合同ID") @PathVariable Long id) {
        try {
            boolean success = contractInfoService.deleteContract(id);
            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除合同
     */
    @ApiOperation("批量删除合同")
    @DeleteMapping("/batch")
    public Result<String> batchDelete(
            @ApiParam("合同ID列表") @RequestBody List<Long> ids) {
        try {
            boolean success = contractInfoService.batchDelete(ids);
            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 提交审核请求对象
     */
    public static class SubmitReviewRequest {
        private Long reviewerId;
        private String reviewerName;

        public Long getReviewerId() {
            return reviewerId;
        }

        public void setReviewerId(Long reviewerId) {
            this.reviewerId = reviewerId;
        }

        public String getReviewerName() {
            return reviewerName;
        }

        public void setReviewerName(String reviewerName) {
            this.reviewerName = reviewerName;
        }
    }

    /**
     * 审核请求对象
     */
    public static class ReviewRequest {
        private Boolean approved;
        private String reviewComment;

        public Boolean getApproved() {
            return approved;
        }

        public void setApproved(Boolean approved) {
            this.approved = approved;
        }

        public String getReviewComment() {
            return reviewComment;
        }

        public void setReviewComment(String reviewComment) {
            this.reviewComment = reviewComment;
        }
    }

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        return UserContext.getCurrentUserId();
    }
}

