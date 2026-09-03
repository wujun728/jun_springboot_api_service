package com.zapi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.entity.ContractSignInfo;
import com.zapi.service.ContractSignInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合同签署信息Controller
 */
@Slf4j
@RestController
@RequestMapping("/contract/sign-info")
@Tag(name = "合同签署信息管理", description = "合同签署信息相关接口")
public class ContractSignInfoController {

    @Autowired
    private ContractSignInfoService contractSignInfoService;

    /**
     * 分页查询签署信息
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询签署信息")
    public IPage<ContractSignInfo> pageSignInfo(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long sealId,
            @RequestParam(required = false) Long certId) {
        Page<ContractSignInfo> page = new Page<>(current, size);
        return contractSignInfoService.pageSignInfo(page, sealId, certId);
    }

    /**
     * 根据合同ID查询签署信息
     */
    @GetMapping("/by-contract/{contractId}")
    @Operation(summary = "根据合同ID查询签署信息")
    public List<ContractSignInfo> getByContractId(@PathVariable Long contractId) {
        return contractSignInfoService.getByContractId(contractId);
    }

    /**
     * 根据印章ID查询签署信息
     */
    @GetMapping("/by-seal/{sealId}")
    @Operation(summary = "根据印章ID查询签署信息")
    public List<ContractSignInfo> getBySealId(@PathVariable Long sealId) {
        return contractSignInfoService.getBySealId(sealId);
    }

    /**
     * 根据证书ID查询签署信息
     */
    @GetMapping("/by-cert/{certId}")
    @Operation(summary = "根据证书ID查询签署信息")
    public List<ContractSignInfo> getByCertId(@PathVariable Long certId) {
        return contractSignInfoService.getByCertId(certId);
    }

    /**
     * 获取签署信息详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取签署信息详情")
    public ContractSignInfo getSignInfoDetail(@PathVariable Long id) {
        return contractSignInfoService.getSignInfoDetail(id);
    }

    /**
     * 创建签署信息
     */
    @PostMapping("/create")
    @Operation(summary = "创建签署信息")
    public ContractSignInfo createSignInfo(@RequestBody ContractSignInfo signInfo) {
        return contractSignInfoService.createSignInfo(signInfo);
    }

    /**
     * 更新签署信息
     */
    @PutMapping("/update")
    @Operation(summary = "更新签署信息")
    public boolean updateSignInfo(@RequestBody ContractSignInfo signInfo) {
        return contractSignInfoService.updateSignInfo(signInfo);
    }

    /**
     * 删除签署信息
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除签署信息")
    public boolean deleteSignInfo(@PathVariable Long id) {
        return contractSignInfoService.deleteSignInfo(id);
    }

    /**
     * 批量删除签署信息
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除签署信息")
    public boolean batchDelete(@RequestBody List<Long> ids) {
        return contractSignInfoService.batchDelete(ids);
    }
}
