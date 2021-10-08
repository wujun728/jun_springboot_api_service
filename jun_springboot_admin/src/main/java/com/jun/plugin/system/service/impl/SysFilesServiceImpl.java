package com.jun.plugin.system.service.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jun.plugin.system.common.comfig.FileUploadProperties;
import com.jun.plugin.system.common.exception.BusinessException;
import com.jun.plugin.system.common.utils.DataResult;
import com.jun.plugin.system.common.utils.DateUtils;
import com.jun.plugin.system.entity.SysFilesEntity;
import com.jun.plugin.system.mapper.SysFilesMapper;
import com.jun.plugin.system.service.SysFilesService;

/**
 * 文件上传 服务类
 *
 * @author wujun
 * @version V1.0
 * @date 2020年3月18日
 */
@EnableConfigurationProperties(FileUploadProperties.class)
@Service("sysFilesService")
public class SysFilesServiceImpl extends ServiceImpl<SysFilesMapper, SysFilesEntity> implements SysFilesService {
    @Resource
    private FileUploadProperties fileUploadProperties;
    
    private static final ThreadLocal<Map<Object, Object>> mapTmp = new ThreadLocal();

    @Override
    public DataResult saveFile(MultipartFile file) {
        //存储文件夹
        String createTime = DateUtils.format(new Date(), DateUtils.DATEPATTERN);
        String newPath = fileUploadProperties.getPath() + createTime + File.separator;
        File uploadDirectory = new File(newPath);
        if (uploadDirectory.exists()) {
            if (!uploadDirectory.isDirectory()) {
                uploadDirectory.delete();
            }
        } else {
            uploadDirectory.mkdir();
        }
        try {
            String fileName = file.getOriginalFilename();
            //id与filename保持一直，删除文件
            String fileNameNew = UUID.randomUUID().toString().replace("-", "") + getFileType(fileName);
            String newFilePathName = newPath + fileNameNew;
            String url = fileUploadProperties.getUrl() + "/" + createTime + "/" + fileNameNew;
            //创建输出文件对象
            File outFile = new File(newFilePathName);
            //拷贝文件到输出文件对象
            FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);
            //保存文件记录
            saveFilesEntity(fileName, newFilePathName, url, "filemanager", "");
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("src", url);
            return DataResult.success(resultMap);
        } catch (Exception e) {
            throw new BusinessException("上传文件失败");
        }
    }

	private void saveFilesEntity(String fileName, String newFilePathName, String url, String biztype, String bizid) {
		SysFilesEntity sysFilesEntity = new SysFilesEntity();
		sysFilesEntity.setFileName(fileName);
		sysFilesEntity.setFilePath(newFilePathName);
		sysFilesEntity.setUrl(url);
		sysFilesEntity.setDictBiztype(String.valueOf(mapTmp.get().get("biztype")));
		sysFilesEntity.setRefBizid(String.valueOf(mapTmp.get().get("bizid")));
		this.save(sysFilesEntity);
	}

    @Override
    public void removeByIdsAndFiles(List<String> ids) {
        List<SysFilesEntity> list = this.listByIds(ids);
        list.forEach(entity -> {
            //如果之前的文件存在，删除
            File file = new File(entity.getFilePath());
            if (file.exists()) {
                file.delete();
            }
        });
        this.removeByIds(ids);

    }

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return 后缀名
     */
    private String getFileType(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

	@Override
	public DataResult saveFile(MultipartFile file, String biztype, String bizid) {
		mapTmp.set(ImmutableMap.builder().put("biztype",biztype).put("bizid", bizid).build());
		return this.saveFile(file);
	}

}