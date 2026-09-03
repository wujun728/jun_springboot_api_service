package com.zapi.support.listener;

import com.zapi.entity.StampSealRequest;
import com.zapi.entity.UserCertInfo;
import com.zapi.file.service.FileService;
import com.zapi.service.UserCertInfoService;
import com.zapi.support.event.SealSignDataEvent;
import com.zapi.util.cert.sm2.CertificateUtils;
import com.zapi.util.file.FileUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;


/**
 * 证书签名验证监听
 * 用于处理盖章操作中的证书验证和私钥提取
 * @author zhonglz
 */
@Component
public class CertSignListener {

    private static Logger logger = LoggerFactory.getLogger(CertSignListener.class);

    @Autowired
    private UserCertInfoService userCertInfoService;

    @Autowired
    private FileService fileService;

    /**
     * 监听盖章事件，进行证书验证和处理
     * @param event 盖章数据事件
     */
    @Order(10)
    @EventListener
    public void acceptEvent(SealSignDataEvent event) {
        logger.info("***************证书签名验证监听开始********************");
        StampSealRequest stampSealRequest = event.getStampSealRequest();
        // 根据证书ID查询出相对应的证书信息
        UserCertInfo userCertInfo = userCertInfoService.getById(stampSealRequest.getCertId());
        stampSealRequest.setUserCertInfo(userCertInfo);
        if(!stampSealRequest.getCertPwd().equals(userCertInfo.getCertPwd())) {
            throw new RuntimeException("证书密码输入错误");
        }
        try {
            // 根据证书文件id查询出文件详细信息 从文件服务获取文件的输入流，然后转换为字节数组
            InputStream fileInputStream = fileService.getFileInputStreamById(userCertInfo.getCertUrl());
            // 将文件下载转换成 byte[] 字节数组
            byte[] certFileByte = null;
            if (fileInputStream != null) {
                certFileByte = FileUtils.inputStream2byte(fileInputStream);
                // 关闭原始流
                fileInputStream.close();
            }
            
            if (certFileByte == null) {
                throw new RuntimeException("证书文件读取失败，文件内容为空");
            }
            
            stampSealRequest.setCertFile(certFileByte);
            
            if(userCertInfo.getCertType().equals("rsa")||userCertInfo.getCertType().equals("RSA")) {
                /// 国际rsa证书
                KeyStore ks = KeyStore.getInstance("JKS");
                // 使用字节数组创建新的输入流
                ByteArrayInputStream certInputStream = new ByteArrayInputStream(certFileByte);
                // jks文件密码，根据实际情况修改
                ks.load(certInputStream, userCertInfo.getCertPwd().toCharArray());
                String alias = (String) ks.aliases().nextElement();
                PrivateKey pk = (PrivateKey) ks.getKey(alias,userCertInfo.getCertPwd().toCharArray());
                Certificate[] chain = ks.getCertificateChain(alias);
                stampSealRequest.setChain(chain);
                stampSealRequest.setPrivateKey(pk);
                certInputStream.close();
            }else if(userCertInfo.getCertType().equals("sm2")||userCertInfo.getCertType().equals("SM2")) {
                /// 国密sm2证书
                X509Certificate x509Certificate = CertificateUtils.getX509CertificateFromPfx(certFileByte, userCertInfo.getCertPwd());
                BCECPrivateKey pk = CertificateUtils.getBCECPrivateKeyFromPfx(certFileByte, userCertInfo.getCertPwd());
                Certificate[] chain = new Certificate[]{x509Certificate};
                stampSealRequest.setChain(chain);
                stampSealRequest.setPrivateKey(pk);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("证书文件获取失败");
        }
        event.setStampSealRequest(stampSealRequest);
        logger.info("***************证书签名验证监听结束********************");
    }

}
