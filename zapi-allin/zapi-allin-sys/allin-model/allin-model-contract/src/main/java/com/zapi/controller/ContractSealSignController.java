package com.zapi.controller;

import com.zapi.common.Result;
import com.zapi.entity.StampSealRequest;
import com.zapi.support.event.SealSignDataEvent;
import com.zapi.util.SpringContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 合同印章盖章签名Controller
 * 用于处理合同的印章盖章和签名操作
 */
@Api(tags = "合同印章盖章签名")
@RestController
@RequestMapping("/contract/seal-sign")
public class ContractSealSignController {

    /**
     * 对合同进行印章盖章
     */
    @ApiOperation("对合同盖印章")
    @PostMapping("/{contractId}/stamp-seal")
    public Result<Long> stampSeal(
            @RequestBody StampSealRequest request) {
        try {
            SealSignDataEvent sealSignDataEvent = new SealSignDataEvent(this, request);
            SpringContextUtil.getApplicationContext().publishEvent(sealSignDataEvent);
            return Result.success("盖章成功",sealSignDataEvent.getStampSealRequest().getSignBackFileUid());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("盖章失败：" + e.getMessage());
        }
    }



}