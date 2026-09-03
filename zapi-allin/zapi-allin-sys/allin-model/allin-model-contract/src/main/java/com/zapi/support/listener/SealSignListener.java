package com.zapi.support.listener;

import com.zapi.entity.StampSealRequest;
import com.zapi.support.event.SealSignDataEvent;
import com.zapi.util.sign.pdf.SignPdf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/***
 * 签章监听
 * @author zhonglz
 *
 */
@Component
public class SealSignListener {

    private static Logger logger = LoggerFactory.getLogger(SealSignListener.class);

    @Order(40)
    @EventListener
    public void acceptEvent(SealSignDataEvent event) {
        logger.info("***************签章监听开始********************");
        StampSealRequest stampSealRequest =event.getStampSealRequest();
        logger.info("签署文件宽度{},高度{}",stampSealRequest.getPdfWidth(),stampSealRequest.getPdfHeight());
        logger.info("盖章位置：x轴{}，y轴{}",stampSealRequest.getPageX(),stampSealRequest.getPageY());
        //重新计算一下 坐标x轴，由于pdf的起始位置是左下角，所以只需要相反位置就好了
        // y轴之所以加50，因为印章大小是100，，，取的中心点位点，一半及50
        // x轴之所以减去50因为印章大小是100，，，取的中心点位点，一半及50
        BigDecimal county = stampSealRequest.getPdfHeight().subtract(stampSealRequest.getPageY()).add(new BigDecimal(50));
        BigDecimal countx = stampSealRequest.getPageX().subtract(new BigDecimal(40));
        logger.info("重新计算盖章位置：x轴{}，y轴{}",countx,county);
        SignPdf sign=new SignPdf();
        try {
            sign.sign(stampSealRequest.getChain(), stampSealRequest.getPrivateKey(),
                    "签名原因", "签名地点",
                    stampSealRequest.getSignFrontFile(),
                    stampSealRequest.getSignBackFile(),
                    stampSealRequest.getSealFile(),
                    stampSealRequest.getUserCertInfo().getCertType(),
                    stampSealRequest.getSignPage(),
                    countx, county,
                    stampSealRequest.getPdfHeight(),
                    stampSealRequest.getPdfWidth());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("签章失败！");
        }
        logger.info("***************签章监听结束********************");
    }
}