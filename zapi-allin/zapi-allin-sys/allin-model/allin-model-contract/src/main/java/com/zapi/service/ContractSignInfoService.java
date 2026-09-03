package com.zapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.entity.ContractSignInfo;

import java.util.List;

/**
 * 合同签署信息Service接口
 */
public interface ContractSignInfoService extends IService<ContractSignInfo> {

    /**
     * 分页查询签署信息
     */
    IPage<ContractSignInfo> pageSignInfo(Page<ContractSignInfo> page, Long sealId, Long certId);

    /**
     * 根据合同ID查询签署信息
     */
    List<ContractSignInfo> getByContractId(Long contractId);

    /**
     * 根据印章ID查询签署信息
     */
    List<ContractSignInfo> getBySealId(Long sealId);

    /**
     * 根据证书ID查询签署信息
     */
    List<ContractSignInfo> getByCertId(Long certId);

    /**
     * 创建签署信息
     */
    ContractSignInfo createSignInfo(ContractSignInfo signInfo);

    /**
     * 更新签署信息
     */
    boolean updateSignInfo(ContractSignInfo signInfo);

    /**
     * 删除签署信息
     */
    boolean deleteSignInfo(Long id);

    /**
     * 批量删除签署信息
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 获取签署信息详情
     */
    ContractSignInfo getSignInfoDetail(Long id);
}
