package com.zapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.UserSeal;
import com.zapi.file.entity.FileInfo;
import com.zapi.file.service.FileService;
import com.zapi.mapper.UserSealMapper;
import com.zapi.service.UserSealService;
import com.zapi.util.SealUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * 用户印章Service实现类
 */
@Service
public class UserSealServiceImpl extends ServiceImpl<UserSealMapper, UserSeal> implements UserSealService {

    @Autowired
    private FileService fileService;

    @Override
    public IPage<UserSeal> pageUserSeals(Page<UserSeal> page, Long userId, String sealName, Integer status) {
        QueryWrapper<UserSeal> queryWrapper = new QueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (sealName != null && !sealName.trim().isEmpty()) {
            queryWrapper.like("seal_name", sealName);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByAsc("sort_order");
        queryWrapper.orderByDesc("create_time");
        return page(page, queryWrapper);
    }

    @Override
    public List<UserSeal> getUserSeals(Long userId) {
        QueryWrapper<UserSeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByAsc("sort_order");
        queryWrapper.orderByDesc("create_time");
        return list(queryWrapper);
    }

    @Override
    public List<UserSeal> getEnabledSeals(Long userId) {
        QueryWrapper<UserSeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("sort_order");
        return list(queryWrapper);
    }

    @Override
    @Transactional
    public UserSeal createSeal(Long userId, String sealName, String sealType, String centerText, Integer sortOrder, String remark) {
        try {
            
            // 验证必填参数
            if (userId == null) {
                throw new RuntimeException("用户ID不能为空");
            }
            if (sealName == null || sealName.trim().isEmpty()) {
                throw new RuntimeException("印章名称不能为空");
            }
            if (sealType == null || sealType.trim().isEmpty()) {
                throw new RuntimeException("印章类型不能为空");
            }
            
            
            // 生成印章图片
            BufferedImage sealImage = generateSealImage(sealType, sealName, centerText);
            if (sealImage == null) {
                throw new RuntimeException("印章图片生成失败，返回null");
            }
            
            // 将BufferedImage转换为InputStream
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(sealImage, "png", os);
            byte[] imageBytes = os.toByteArray();
            ByteArrayInputStream is = new ByteArrayInputStream(imageBytes);
            
            // 上传印章文件
            String fileName = sealName + "_" + System.currentTimeMillis() + ".png";
            
            FileInfo fileInfo = fileService.uploadFile(
                is, 
                fileName, 
                "seal", 
                "用户印章：" + sealName, 
                userId, 
                null
            );
            
            if (fileInfo == null) {
                throw new RuntimeException("文件上传失败，返回null");
            }
            
            // 创建印章记录
            UserSeal userSeal = new UserSeal();
            userSeal.setUserId(userId);
            userSeal.setSealName(sealName);
            userSeal.setSealType(sealType);
            userSeal.setFileId(fileInfo.getId());
            userSeal.setFileUrl(fileInfo.getFilePath());
            userSeal.setStatus(1); // 默认启用
            userSeal.setSortOrder(sortOrder != null ? sortOrder : 0);
            userSeal.setRemark(remark);
            
            
            boolean saved = save(userSeal);
            if (!saved) {
                throw new RuntimeException("保存印章记录到数据库失败");
            }
            
            return userSeal;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建印章失败：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean updateStatus(Long id, Integer status) {
        UserSeal userSeal = getById(id);
        if (userSeal == null) {
            throw new RuntimeException("印章不存在");
        }
        userSeal.setStatus(status);
        return updateById(userSeal);
    }

    @Override
    @Transactional
    public boolean updateSortOrder(Long id, Integer sortOrder) {
        UserSeal userSeal = getById(id);
        if (userSeal == null) {
            throw new RuntimeException("印章不存在");
        }
        userSeal.setSortOrder(sortOrder);
        return updateById(userSeal);
    }

    @Override
    @Transactional
    public boolean deleteSeal(Long id) {
        UserSeal userSeal = getById(id);
        if (userSeal == null) {
            throw new RuntimeException("印章不存在");
        }
        
        // 删除文件
        if (userSeal.getFileId() != null) {
            try {
                fileService.deleteFile(userSeal.getFileId());
            } catch (Exception e) {
            }
        }
        
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // 删除所有相关文件
        List<UserSeal> seals = listByIds(ids);
        for (UserSeal seal : seals) {
            if (seal.getFileId() != null) {
                try {
                    fileService.deleteFile(seal.getFileId());
                } catch (Exception e) {
                }
            }
        }
        
        return removeByIds(ids);
    }

    /**
     * 根据类型生成印章图片
     */
    private BufferedImage generateSealImage(String sealType, String sealName, String centerText) {
        try {
            BufferedImage image = null;
            
            switch (sealType) {
                case "SQUARE_BORDER":
                    // 方章（有边框）- changfangx
                    image = SealUtils.changfangx(sealName);
                    break;
                case "SQUARE_NO_BORDER":
                    // 方章（无边框）- sealFanz01
                    image = SealUtils.sealFanz01(sealName);
                    break;
                case "SQUARE_3":
                    // 方章正 - sealFanz03
                    image = SealUtils.sealFanz03(80, sealName);
                    break;
                case "ELLIPSE":
                    // 椭圆章 - startEllipseGraphics2D
                    String ellipseCenter = (centerText != null && !centerText.trim().isEmpty()) ? centerText : "";
                    image = SealUtils.startEllipseGraphics2D(sealName, ellipseCenter);
                    break;
                default:
                    // 默认使用方章有边框
                    image = SealUtils.changfangx(sealName);
                    break;
            }
            
            if (image == null) {
                throw new RuntimeException("SealUtils返回null，印章生成失败");
            }
            
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成印章图片失败: " + e.getMessage(), e);
        }
    }
}

