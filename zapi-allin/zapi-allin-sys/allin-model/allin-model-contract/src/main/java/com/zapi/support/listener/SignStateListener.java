package com.zapi.support.listener;

import com.zapi.entity.ContractSignInfo;
import com.zapi.entity.StampSealRequest;
import com.zapi.service.ContractSignInfoService;
import com.zapi.support.event.SealSignDataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/***
 * PDF签署文件监听
 * @author zhonglz
 *
 */
@Component
public class SignStateListener {
    private static Logger logger = LoggerFactory.getLogger(SignStateListener.class);

    @Autowired
    private ContractSignInfoService contractSignInfoService;

    @Order(30)
    @EventListener
    public void acceptEvent(SealSignDataEvent event) {
        logger.info("***************PDF签署文件监听开始********************");
        StampSealRequest stampSealRequest = event.getStampSealRequest();

        try {
            // 创建签署记录
            ContractSignInfo signInfo = createSignInfo(stampSealRequest);
            
            // 保存签署记录到数据库
            ContractSignInfo savedSignInfo = contractSignInfoService.createSignInfo(signInfo);
            logger.info("签署记录保存成功，记录ID: {}", savedSignInfo.getId());
            
            // 将签署信息设置回请求对象
            stampSealRequest.setContractSignInfo(savedSignInfo);
            event.setStampSealRequest(stampSealRequest);
            
        } catch (Exception e) {
            logger.error("保存签署记录失败", e);
            throw new RuntimeException("保存签署记录失败: " + e.getMessage());
        }
        
        logger.info("***************PDF签署文件监听结束********************");
    }

    /**
     * 根据StampSealRequest创建ContractSignInfo对象
     */
    private ContractSignInfo createSignInfo(StampSealRequest request) {
        logger.info("开始创建签署记录，签署前文件ID: {}, 印章ID: {}, 证书ID: {}", 
                   request.getSignFileId(), request.getSealId(), request.getCertId());

        return ContractSignInfo.builder()
                // 签署前的文件ID
                .signFrontFileId(request.getSignFileId())
                // 签署后的文件ID（如果有签署后文件UID，需要转换为文件ID）
                .signBackFileId(request.getSignBackFileUid())
                // 印章ID
                .sealId(request.getSealId())
                // 证书ID
                .certId(request.getCertId())
                // 签署算法（从证书信息中获取）
                .signAlgorithm(getSignAlgorithm(request))
                // PDF页码信息
                .signPage(request.getSignPage())
                .pdfPageCount(request.getPdfPageCount())
                .pdfWidth(convertToInteger(request.getPdfWidth()))
                .pdfHeight(convertToInteger(request.getPdfHeight()))
                // 坐标信息
                .coordX(convertToInteger(request.getPageX()))
                .coordY(convertToInteger(request.getPageY()))
                // 像素大小（可以根据需要设置默认值）
                .pixelSize("100x100")
                // 时间信息
                .signTime(new Date())
                .createTime(new Date())
                .build();
    }


    /**
     * 获取签署算法
     */
    private String getSignAlgorithm(StampSealRequest request) {
        if (request.getUserCertInfo() != null && request.getUserCertInfo().getCertType() != null) {
            return request.getUserCertInfo().getCertType().toUpperCase();
        }
        // 默认返回RSA
        return "RSA";
    }

    /**
     * 将BigDecimal转换为Integer
     */
    private Integer convertToInteger(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.intValue();
    }
}
