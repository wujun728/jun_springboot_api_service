package com.zapi.support.listener;

import cn.hutool.core.util.ObjectUtil;
import com.zapi.entity.StampSealRequest;
import com.zapi.file.entity.FileInfo;
import com.zapi.file.service.FileService;
import com.zapi.support.event.SealSignDataEvent;
import com.zapi.util.UserContext;
import com.zapi.util.UuidUtils;
import com.zapi.util.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/***
 * 签署文件监听
 * @author zhonglz
 *
 */
@Component
public class FileSignListener {

    private static Logger logger = LoggerFactory.getLogger(FileSignListener.class);

    @Autowired
    private FileService fileService;


    @Value("${zapi.file.local.root-path}")
    private String storagePath;

    @Order(30)
    @EventListener
    public void acceptEvent(SealSignDataEvent event) {
        logger.info("***************签署文件监听开始********************");
        StampSealRequest stampSealRequest =event.getStampSealRequest();


        FileInfo sysFileInfo =fileService.getFileInfo(stampSealRequest.getSignFileId());
        if(ObjectUtil.isEmpty(sysFileInfo)) {
            throw new RuntimeException("签署文件信息获取失败");
        }
        try {
            stampSealRequest.setSignFrontFile(FileUtils.inputStream2byte(fileService.getFileInputStream(sysFileInfo.getFilePath())));
            // 定义一个文件签署地址
            String fileUid = UuidUtils.uuid();
            String fileName = fileUid+".pdf";
            String filePath = storagePath+"/sign/"+fileName;
            logger.info("设置签署后的文件路径："+filePath);

            stampSealRequest.setSignBackFile(filePath);
            // 从系统上下文获取当前登录用户ID
            Long userId = UserContext.getCurrentUserId();
            FileInfo fileInfo = fileService.uploadFile(filePath,fileName,userId,null);
            stampSealRequest.setSignBackFileUid(fileInfo.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("签署文件获取失败");
        }

        event.setStampSealRequest(stampSealRequest);
        logger.info("***************签署文件监听结束********************");
    }
}