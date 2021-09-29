package com.jun.plugin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jun.plugin.common.utils.DataResult;
import com.jun.plugin.entity.SysFilesEntity;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传 服务类
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
public interface SysFilesService extends IService<SysFilesEntity> {

    DataResult saveFile(MultipartFile file);

    void removeByIdsAndFiles(List<String> ids);
}

