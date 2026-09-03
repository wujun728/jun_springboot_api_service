package com.zapi.support.listener;

import cn.hutool.core.util.ObjectUtil;
import com.zapi.entity.StampSealRequest;
import com.zapi.entity.UserSeal;
import com.zapi.file.entity.FileInfo;
import com.zapi.file.service.FileService;
import com.zapi.service.UserSealService;
import com.zapi.support.event.SealSignDataEvent;
import com.zapi.util.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/***
 * 印章处理监听
 * @author zhonglz
 *
 */
@Component
public class SealInfoListener {

    private static Logger logger = LoggerFactory.getLogger(SealInfoListener.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private UserSealService userSealService;

    @Order(20)
    @EventListener
    public void acceptEvent(SealSignDataEvent event) {
        logger.info("***************印章处理监听开始********************");
        StampSealRequest stampSealRequest =event.getStampSealRequest();
        /// 获取印章信息
        UserSeal userSeal =userSealService.getById(stampSealRequest.getSealId());
        FileInfo sysFileInfo =fileService.getFileInfo(userSeal.getFileId());
        if(ObjectUtil.isEmpty(sysFileInfo)) {
            throw new RuntimeException("印章文件信息获取失败");
        }
        try {
            stampSealRequest.setSealFile(FileUtils.inputStream2byte(fileService.getFileInputStream(sysFileInfo.getFilePath())));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("印章文件获取失败");
        }
        event.setStampSealRequest(stampSealRequest);
        logger.info("***************印章处理监听结束********************");
    }
}
