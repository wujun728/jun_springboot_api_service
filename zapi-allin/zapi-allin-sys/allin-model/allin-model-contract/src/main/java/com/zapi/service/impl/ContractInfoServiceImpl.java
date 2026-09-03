package com.zapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.ContractInfo;
import com.zapi.mapper.ContractInfoMapper;
import com.zapi.service.ContractInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 合同信息Service实现类
 */
@Service
public class ContractInfoServiceImpl extends ServiceImpl<ContractInfoMapper, ContractInfo> implements ContractInfoService {

    @Override
    public IPage<ContractInfo> pageContracts(Page<ContractInfo> page, Long initiatorId, String contractName, String contractStatus) {
        QueryWrapper<ContractInfo> queryWrapper = new QueryWrapper<>();
        if (initiatorId != null) {
            queryWrapper.eq("initiator_id", initiatorId);
        }
        if (contractName != null && !contractName.trim().isEmpty()) {
            queryWrapper.like("contract_name", contractName);
        }
        if (contractStatus != null && !contractStatus.trim().isEmpty()) {
            queryWrapper.eq("contract_status", contractStatus);
        }
        queryWrapper.orderByDesc("create_time");
        return page(page, queryWrapper);
    }

    @Override
    @Transactional
    public ContractInfo createContract(ContractInfo contractInfo) {
        // 生成合同编号（如果未提供）
        if (contractInfo.getContractNo() == null || contractInfo.getContractNo().trim().isEmpty()) {
            contractInfo.setContractNo(generateContractNo());
        }
        
        // 确保状态为草稿
        contractInfo.setContractStatus(ContractInfo.Status.DRAFT);
        
        save(contractInfo);
        return contractInfo;
    }

    @Override
    @Transactional
    public boolean submitForReview(Long id, Long reviewerId, String reviewerName) {
        ContractInfo contractInfo = getById(id);
        if (contractInfo == null) {
            throw new RuntimeException("合同不存在");
        }
        
        if (!ContractInfo.Status.DRAFT.equals(contractInfo.getContractStatus())) {
            throw new RuntimeException("只有草稿状态的合同才能提交审核");
        }
        
        contractInfo.setContractStatus(ContractInfo.Status.REVIEWING);
        contractInfo.setReviewerId(reviewerId);
        contractInfo.setReviewerName(reviewerName);
        
        return updateById(contractInfo);
    }

    @Override
    @Transactional
    public boolean reviewContract(Long id, Long reviewerId, String reviewerName, boolean approved, String reviewComment) {
        ContractInfo contractInfo = getById(id);
        if (contractInfo == null) {
            throw new RuntimeException("合同不存在");
        }
        
        if (!ContractInfo.Status.REVIEWING.equals(contractInfo.getContractStatus())) {
            throw new RuntimeException("只有审核中状态的合同才能进行审核操作");
        }
        
        contractInfo.setReviewerId(reviewerId);
        contractInfo.setReviewerName(reviewerName);
        contractInfo.setReviewTime(LocalDateTime.now());
        contractInfo.setReviewComment(reviewComment);
        
        if (approved) {
            contractInfo.setContractStatus(ContractInfo.Status.PENDING_SIGN);
        } else {
            // 审核不通过，退回草稿状态
            contractInfo.setContractStatus(ContractInfo.Status.DRAFT);
        }
        
        return updateById(contractInfo);
    }

    @Override
    @Transactional
    public boolean signContract(Long id, Long signerId, String signerName) {
        ContractInfo contractInfo = getById(id);
        if (contractInfo == null) {
            throw new RuntimeException("合同不存在");
        }
        
        if (!ContractInfo.Status.PENDING_SIGN.equals(contractInfo.getContractStatus())) {
            throw new RuntimeException("只有待签署状态的合同才能进行签署操作");
        }
        
        contractInfo.setContractStatus(ContractInfo.Status.SIGNED);
        contractInfo.setSignerId(signerId);
        contractInfo.setSignerName(signerName);
        contractInfo.setSignTime(LocalDateTime.now());
        
        return updateById(contractInfo);
    }

    @Override
    @Transactional
    public boolean archiveContract(Long id) {
        ContractInfo contractInfo = getById(id);
        if (contractInfo == null) {
            throw new RuntimeException("合同不存在");
        }
        
        if (!ContractInfo.Status.SIGNED.equals(contractInfo.getContractStatus())) {
            throw new RuntimeException("只有签署完成状态的合同才能归档");
        }
        
        contractInfo.setContractStatus(ContractInfo.Status.ARCHIVED);
        contractInfo.setArchiveTime(LocalDateTime.now());
        
        return updateById(contractInfo);
    }

    @Override
    @Transactional
    public boolean deleteContract(Long id) {
        ContractInfo contractInfo = getById(id);
        if (contractInfo == null) {
            return false;
        }
        
        // 只有草稿状态的合同才能删除
        if (!ContractInfo.Status.DRAFT.equals(contractInfo.getContractStatus())) {
            throw new RuntimeException("只有草稿状态的合同才能删除");
        }
        
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // 检查所有合同是否都是草稿状态
        List<ContractInfo> contracts = listByIds(ids);
        for (ContractInfo contract : contracts) {
            if (!ContractInfo.Status.DRAFT.equals(contract.getContractStatus())) {
                throw new RuntimeException("只能删除草稿状态的合同，合同编号：" + contract.getContractNo());
            }
        }
        
        return removeByIds(ids);
    }

    @Override
    public ContractInfo getByContractNo(String contractNo) {
        QueryWrapper<ContractInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_no", contractNo);
        return getOne(queryWrapper);
    }

    /**
     * 生成合同编号
     * 格式：HT + 年月日 + 6位随机数
     */
    private String generateContractNo() {
        String dateStr = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%06d", (int)(Math.random() * 1000000));
        return "HT" + dateStr + randomStr;
    }
}

