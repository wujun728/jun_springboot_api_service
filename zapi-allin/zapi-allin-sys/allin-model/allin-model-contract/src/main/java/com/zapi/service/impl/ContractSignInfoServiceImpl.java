package com.zapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.ContractSignInfo;
import com.zapi.mapper.ContractSignInfoMapper;
import com.zapi.service.ContractSignInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 合同签署信息Service实现类
 */
@Slf4j
@Service
public class ContractSignInfoServiceImpl extends ServiceImpl<ContractSignInfoMapper, ContractSignInfo> implements ContractSignInfoService {

    /**
     * 分页查询签署信息
     */
    @Override
    public IPage<ContractSignInfo> pageSignInfo(Page<ContractSignInfo> page, Long sealId, Long certId) {
        LambdaQueryWrapper<ContractSignInfo> wrapper = new LambdaQueryWrapper<>();
        
        if (sealId != null) {
            wrapper.eq(ContractSignInfo::getSealId, sealId);
        }
        if (certId != null) {
            wrapper.eq(ContractSignInfo::getCertId, certId);
        }
        
        wrapper.orderByDesc(ContractSignInfo::getCreateTime);
        return page(page, wrapper);
    }

    /**
     * 根据合同ID查询签署信息
     */
    @Override
    public List<ContractSignInfo> getByContractId(Long contractId) {
        LambdaQueryWrapper<ContractSignInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractSignInfo::getSignFrontFileId, contractId);
        wrapper.orderByDesc(ContractSignInfo::getCreateTime);
        return list(wrapper);
    }

    /**
     * 根据印章ID查询签署信息
     */
    @Override
    public List<ContractSignInfo> getBySealId(Long sealId) {
        LambdaQueryWrapper<ContractSignInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractSignInfo::getSealId, sealId);
        wrapper.orderByDesc(ContractSignInfo::getCreateTime);
        return list(wrapper);
    }

    /**
     * 根据证书ID查询签署信息
     */
    @Override
    public List<ContractSignInfo> getByCertId(Long certId) {
        LambdaQueryWrapper<ContractSignInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractSignInfo::getCertId, certId);
        wrapper.orderByDesc(ContractSignInfo::getCreateTime);
        return list(wrapper);
    }

    /**
     * 创建签署信息
     */
    @Override
    public ContractSignInfo createSignInfo(ContractSignInfo signInfo) {
        try {
            if (signInfo.getCreateTime() == null) {
                signInfo.setCreateTime(new Date());
            }
            save(signInfo);
            log.info("创建签署信息成功，ID: {}", signInfo.getId());
            return signInfo;
        } catch (Exception e) {
            log.error("创建签署信息失败", e);
            throw new RuntimeException("创建签署信息失败: " + e.getMessage());
        }
    }

    /**
     * 更新签署信息
     */
    @Override
    public boolean updateSignInfo(ContractSignInfo signInfo) {
        try {
            boolean result = updateById(signInfo);
            if (result) {
                log.info("更新签署信息成功，ID: {}", signInfo.getId());
            }
            return result;
        } catch (Exception e) {
            log.error("更新签署信息失败", e);
            throw new RuntimeException("更新签署信息失败: " + e.getMessage());
        }
    }

    /**
     * 删除签署信息
     */
    @Override
    public boolean deleteSignInfo(Long id) {
        try {
            boolean result = removeById(id);
            if (result) {
                log.info("删除签署信息成功，ID: {}", id);
            }
            return result;
        } catch (Exception e) {
            log.error("删除签署信息失败", e);
            throw new RuntimeException("删除签署信息失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除签署信息
     */
    @Override
    public boolean batchDelete(List<Long> ids) {
        try {
            boolean result = removeBatchByIds(ids);
            if (result) {
                log.info("批量删除签署信息成功，数量: {}", ids.size());
            }
            return result;
        } catch (Exception e) {
            log.error("批量删除签署信息失败", e);
            throw new RuntimeException("批量删除签署信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取签署信息详情
     */
    @Override
    public ContractSignInfo getSignInfoDetail(Long id) {
        try {
            ContractSignInfo signInfo = getById(id);
            if (signInfo == null) {
                log.warn("签署信息不存在，ID: {}", id);
            }
            return signInfo;
        } catch (Exception e) {
            log.error("获取签署信息详情失败", e);
            throw new RuntimeException("获取签署信息详情失败: " + e.getMessage());
        }
    }
}
