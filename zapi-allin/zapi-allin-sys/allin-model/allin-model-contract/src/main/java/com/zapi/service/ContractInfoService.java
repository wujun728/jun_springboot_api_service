package com.zapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.entity.ContractInfo;

import java.util.List;

/**
 * 合同信息Service接口
 */
public interface ContractInfoService extends IService<ContractInfo> {

    /**
     * 分页查询合同信息
     */
    IPage<ContractInfo> pageContracts(Page<ContractInfo> page, Long initiatorId, String contractName, String contractStatus);

    /**
     * 创建合同（草稿状态）
     */
    ContractInfo createContract(ContractInfo contractInfo);

    /**
     * 提交审核
     */
    boolean submitForReview(Long id, Long reviewerId, String reviewerName);

    /**
     * 审核合同
     */
    boolean reviewContract(Long id, Long reviewerId, String reviewerName, boolean approved, String reviewComment);

    /**
     * 签署合同
     */
    boolean signContract(Long id, Long signerId, String signerName);

    /**
     * 归档合同
     */
    boolean archiveContract(Long id);

    /**
     * 删除合同
     */
    boolean deleteContract(Long id);

    /**
     * 批量删除合同
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 根据合同编号查询合同
     */
    ContractInfo getByContractNo(String contractNo);
}

