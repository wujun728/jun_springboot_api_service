package com.zapi.file.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.file.config.FileConfig;
import com.zapi.file.entity.FileInfo;
import com.zapi.file.factory.FileStorageFactory;
import com.zapi.file.mapper.FileInfoMapper;
import com.zapi.file.service.FileService;
import com.zapi.file.strategy.FileStorageStrategy;
import com.zapi.file.util.FilePathUtil;
import com.zapi.file.util.FileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件服务实现类
 */
@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileService {

    @Autowired
    private FileConfig fileConfig;
    
    @Autowired
    private FileStorageFactory fileStorageFactory;

    @Override
    public FileInfo uploadFile(MultipartFile file, String path, String description, 
                             Long uploadUserId, String uploadUsername) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 检查文件大小
        long maxSize = fileConfig.getUpload().getMaxFileSize() * 1024 * 1024; // 转换为字节
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("文件大小超过限制");
        }
        // 获取存储策略
        FileStorageStrategy storageStrategy = fileStorageFactory.getStorageStrategy();
        // 使用存储策略上传文件
        String fileUrl = storageStrategy.uploadFile(file, path);
        // 从URL中提取文件名
        String fileName = FilePathUtil.extractFileNameFromUrl(fileUrl);
        String originalFileName = file.getOriginalFilename();
        String fileExtension = FileTypeUtil.getFileExtension(originalFileName);
        // 创建文件信息
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(fileName);
        fileInfo.setOriginalFileName(originalFileName);
        fileInfo.setFilePath(fileName);
        fileInfo.setFileUrl(fileUrl);
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileType(FileTypeUtil.getFriendlyFileType(fileExtension));
        fileInfo.setFileExtension(fileExtension);
        fileInfo.setStorageType(storageStrategy.getStorageType());

        fileInfo.setStatus(1);
        fileInfo.setUploadUserId(uploadUserId);
        fileInfo.setUploadUsername(uploadUsername);
        fileInfo.setDescription(description);
        fileInfo.setCreateTime(LocalDateTime.now());
        fileInfo.setUpdateTime(LocalDateTime.now());

        // 保存到数据库
        save(fileInfo);

        log.info("文件上传成功: {}", fileName);
        return fileInfo;
    }

    @Override
    public FileInfo uploadFile(String path, String fileName,
                               Long uploadUserId, String uploadUsername) throws Exception {
        // 获取存储策略
        FileStorageStrategy storageStrategy = fileStorageFactory.getStorageStrategy();
        // 使用存储策略上传文件
        String fileUrl = path;
        String fileExtension = FileTypeUtil.getFileExtension(fileName);
        // 创建文件信息
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(fileName);
        fileInfo.setOriginalFileName(fileName);
        fileInfo.setFilePath(storageStrategy.createFilePath(path));
        fileInfo.setFileUrl(storageStrategy.createFileUrl(fileName));
        fileInfo.setFileType(FileTypeUtil.getFriendlyFileType(fileExtension));
        fileInfo.setFileExtension(fileExtension);
        fileInfo.setStorageType(storageStrategy.getStorageType());
        fileInfo.setStatus(1);
        fileInfo.setUploadUserId(uploadUserId);
        fileInfo.setUploadUsername(uploadUsername);
        fileInfo.setCreateTime(LocalDateTime.now());
        fileInfo.setUpdateTime(LocalDateTime.now());

        // 保存到数据库
        save(fileInfo);

        log.info("文件上传成功: {}", fileName);
        return fileInfo;
    }

    @Override
    public FileInfo uploadFile(InputStream inputStream, String fileName, String path, 
                             String description, Long uploadUserId, String uploadUsername) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("输入流不能为空");
        }

        // 获取存储策略
        FileStorageStrategy storageStrategy = fileStorageFactory.getStorageStrategy();
        
        // 使用存储策略上传文件
        String fileUrl = storageStrategy.uploadFile(inputStream, fileName, path);
        
        // 从URL中提取文件名
        String uniqueFileName = FilePathUtil.extractFileNameFromUrl(fileUrl);
        String fileExtension = FileTypeUtil.getFileExtension(fileName);
        
        // 获取文件大小
        long fileSize = 0;
        
        try {
            fileSize = storageStrategy.getFileSize(buildStoragePath(path, uniqueFileName));
        } catch (Exception e) {
            log.warn("获取文件大小失败: {}", e.getMessage());
        }

        // 创建文件信息
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(uniqueFileName);
        fileInfo.setOriginalFileName(fileName);
        // 设置完整的文件路径（包含子目录）
        fileInfo.setFilePath(buildStoragePath(path, uniqueFileName));
        fileInfo.setFileUrl(fileUrl);
        fileInfo.setFileSize(fileSize);
        fileInfo.setFileType(FileTypeUtil.getFriendlyFileType(fileExtension));
        fileInfo.setFileExtension(fileExtension);
        fileInfo.setStorageType(storageStrategy.getStorageType());
        fileInfo.setStatus(1);
        fileInfo.setUploadUserId(uploadUserId);
        fileInfo.setUploadUsername(uploadUsername);
        fileInfo.setDescription(description);
        fileInfo.setCreateTime(LocalDateTime.now());
        fileInfo.setUpdateTime(LocalDateTime.now());
        // 保存到数据库
        save(fileInfo);
        log.info("文件上传成功: {}", uniqueFileName);
        return fileInfo;
    }

    @Override
    public void downloadFile(Long fileId, HttpServletResponse response) throws Exception {
        log.info("下载文件请求: fileId={}", fileId);
        
        FileInfo fileInfo = getById(fileId);
        if (fileInfo == null) {
            log.error("文件信息不存在: fileId={}", fileId);
            throw new IllegalArgumentException("文件不存在，ID: " + fileId);
        }
        
        log.info("文件信息: id={}, fileName={}, filePath={}, fileUrl={}", 
            fileInfo.getId(), fileInfo.getFileName(), fileInfo.getFilePath(), fileInfo.getFileUrl());

        downloadFile(fileInfo.getFilePath(), fileInfo.getFileName(), response);
    }

    @Override
    public void downloadFile(String filePath, String fileName, HttpServletResponse response) throws Exception {
        log.info("下载文件: filePath={}, fileName={}", filePath, fileName);
        
        // 获取存储策略
        FileStorageStrategy storageStrategy = fileStorageFactory.getStorageStrategy();
        log.info("使用存储策略: {}", storageStrategy.getStorageType());

        // 检查文件是否存在
        if (!storageStrategy.exists(filePath)) {
            log.error("物理文件不存在: filePath={}", filePath);
            throw new IllegalArgumentException("物理文件不存在: " + filePath);
        }
        
        log.info("文件存在，开始下载");
        
        // 设置响应头
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + 
                URLEncoder.encode(fileName, "UTF-8"));
        
        try {
            long fileSize = storageStrategy.getFileSize(filePath);
            response.setContentLength((int) fileSize);
            log.info("文件大小: {} bytes", fileSize);
        } catch (Exception e) {
            log.warn("获取文件大小失败: {}", e.getMessage());
        }

        // 使用存储策略下载文件
        try (InputStream inputStream = storageStrategy.downloadFile(filePath)) {
            // 将输入流复制到输出流
            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalBytes = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
            response.getOutputStream().flush();
            log.info("文件下载完成，共传输 {} bytes", totalBytes);
        } catch (Exception e) {
            log.error("文件传输失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean deleteFile(Long fileId) throws Exception {
        FileInfo fileInfo = getById(fileId);
        if (fileInfo == null) {
            log.warn("文件不存在: fileId={}", fileId);
            return false;
        }

        String filePath = fileInfo.getFilePath();
        
        try {
            // 先删除物理文件
            boolean fileDeleted = deleteFile(filePath);
            
            // 如果物理文件删除成功，再删除数据库记录
            if (fileDeleted) {
                boolean dbDeleted = removeById(fileId);
                if (dbDeleted) {
                    log.info("文件及数据库记录删除成功: fileId={}, filePath={}", fileId, filePath);
                    return true;
                } else {
                    // 数据库删除失败，记录错误但不恢复物理文件（避免数据不一致）
                    log.error("物理文件删除成功但数据库记录删除失败: fileId={}, filePath={}", fileId, filePath);
                    return false;
                }
            } else {
                log.error("物理文件删除失败: fileId={}, filePath={}", fileId, filePath);
                return false;
            }
        } catch (Exception e) {
            log.error("删除文件时发生异常: fileId={}, filePath={}, error={}", fileId, filePath, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean deleteFile(String filePath) throws Exception {
        // 获取存储策略
        FileStorageStrategy storageStrategy = fileStorageFactory.getStorageStrategy();
        
        // 将完整路径转换为相对路径（如果是本地存储）
        String relativePath = filePath;
        // 检查文件是否存在
        if (!storageStrategy.exists(relativePath)) {
            log.warn("物理文件不存在: {}", filePath);
            return false;
        }

        try {
            // 先查找数据库中的文件记录
            FileInfo fileInfo = getFileInfoByPath(filePath);
            
            // 使用存储策略删除物理文件
            boolean fileDeleted = storageStrategy.deleteFile(relativePath);
            if (fileDeleted) {
                // 如果物理文件删除成功，且数据库中有记录，则删除数据库记录
                if (fileInfo != null) {
                    boolean dbDeleted = removeById(fileInfo.getId());
                    if (dbDeleted) {
                        log.info("文件及数据库记录删除成功: filePath={}, fileId={}", filePath, fileInfo.getId());
                    } else {
                        log.error("物理文件删除成功但数据库记录删除失败: filePath={}, fileId={}", filePath, fileInfo.getId());
                        return false;
                    }
                } else {
                    log.info("物理文件删除成功（数据库中无记录）: filePath={}", filePath);
                }
                return true;
            } else {
                log.error("物理文件删除失败: filePath={}", filePath);
                return false;
            }
        } catch (Exception e) {
            log.error("删除文件时发生异常: filePath={}, error={}", filePath, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 批量删除文件
     *
     * @param fileIds 文件ID列表
     * @return 删除成功的文件数量
     * @throws Exception 删除异常
     */
    public int batchDeleteFiles(List<Long> fileIds) throws Exception {
        if (fileIds == null || fileIds.isEmpty()) {
            return 0;
        }
        
        int successCount = 0;
        int failCount = 0;
        
        for (Long fileId : fileIds) {
            try {
                if (deleteFile(fileId)) {
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
                log.error("批量删除文件失败: fileId={}, error={}", fileId, e.getMessage());
            }
        }
        
        log.info("批量删除文件完成: 成功={}, 失败={}, 总数={}", successCount, failCount, fileIds.size());
        return successCount;
    }

    @Override
    public FileInfo copyFile(Long sourceFileId, String targetPath, String description, 
                           Long uploadUserId, String uploadUsername) throws Exception {
        FileInfo sourceFileInfo = getById(sourceFileId);
        if (sourceFileInfo == null) {
            throw new IllegalArgumentException("源文件不存在");
        }

        return copyFile(sourceFileInfo.getFilePath(), targetPath, description, uploadUserId, uploadUsername);
    }

    @Override
    public FileInfo copyFile(String sourcePath, String targetPath, String description, 
                           Long uploadUserId, String uploadUsername) throws Exception {
        // 获取存储策略
        FileStorageStrategy storageStrategy = fileStorageFactory.getStorageStrategy();
        
        // 检查源文件是否存在
        if (!storageStrategy.exists(sourcePath)) {
            throw new IllegalArgumentException("源文件不存在");
        }

        // 生成目标文件名
        String fileName = new File(sourcePath).getName();
        String targetFileName = IdUtil.fastSimpleUUID() + FileTypeUtil.getFileExtension(fileName);
        String fullTargetPath = FilePathUtil.buildRelativePath(targetPath, targetFileName);

        // 使用存储策略复制文件
        boolean copied = storageStrategy.copyFile(sourcePath, fullTargetPath);
        if (!copied) {
            throw new RuntimeException("文件复制失败");
        }
        
        // 获取文件大小
        long fileSize = storageStrategy.getFileSize(fullTargetPath);
        
        // 生成文件URL
        String fileUrl = generateFileUrl(targetPath, targetFileName);

        // 创建新的文件信息
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(targetFileName);
        fileInfo.setOriginalFileName(fileName);
        fileInfo.setFilePath(buildStoragePath(targetPath, targetFileName));
        fileInfo.setFileUrl(fileUrl);
        fileInfo.setFileSize(fileSize);
        fileInfo.setFileType(FileTypeUtil.getFriendlyFileType(FileTypeUtil.getFileExtension(fileName)));
        fileInfo.setFileExtension(FileTypeUtil.getFileExtension(fileName));
        fileInfo.setStorageType(storageStrategy.getStorageType());
        
        // 计算MD5（仅本地存储）
        if ("LOCAL".equals(storageStrategy.getStorageType())) {
            File targetFile = new File(buildStoragePath(targetPath, targetFileName));
            fileInfo.setFileMd5(DigestUtil.md5Hex(targetFile));
        }
        
        fileInfo.setStatus(1);
        fileInfo.setUploadUserId(uploadUserId);
        fileInfo.setUploadUsername(uploadUsername);
        fileInfo.setDescription(description);
        fileInfo.setCreateTime(LocalDateTime.now());
        fileInfo.setUpdateTime(LocalDateTime.now());

        // 保存到数据库
        save(fileInfo);

        log.info("文件复制成功: {} -> {}", sourcePath, fullTargetPath);
        return fileInfo;
    }

    @Override
    public boolean moveFile(Long fileId, String targetPath) throws Exception {
        FileInfo fileInfo = getById(fileId);
        if (fileInfo == null) {
            return false;
        }

        return moveFile(fileInfo.getFilePath(), targetPath);
    }

    @Override
    public boolean moveFile(String sourcePath, String targetPath) throws Exception {
        // 获取存储策略
        FileStorageStrategy storageStrategy = fileStorageFactory.getStorageStrategy();
        
        // 检查源文件是否存在
        if (!storageStrategy.exists(sourcePath)) {
            return false;
        }

        // 生成目标文件名
        String fileName = new File(sourcePath).getName();
        String targetFileName = IdUtil.fastSimpleUUID() + FileTypeUtil.getFileExtension(fileName);
        String fullTargetPath = FilePathUtil.buildRelativePath(targetPath, targetFileName);

        // 使用存储策略移动文件
        boolean moved = storageStrategy.moveFile(sourcePath, fullTargetPath);
        if (moved) {
            log.info("文件移动成功: {} -> {}", sourcePath, fullTargetPath);
        }
        return moved;
    }

    @Override
    public Page<FileInfo> getFileList(Integer current, Integer size, String fileName, 
                                    String fileType, String storageType, Long uploadUserId) {
        Page<FileInfo> page = new Page<>(current, size);
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(fileName)) {
            queryWrapper.like("file_name", fileName);
        }
        if (StrUtil.isNotBlank(fileType)) {
            queryWrapper.eq("file_type", fileType);
        }
        if (StrUtil.isNotBlank(storageType)) {
            queryWrapper.eq("storage_type", storageType);
        }
        if (uploadUserId != null) {
            queryWrapper.eq("upload_user_id", uploadUserId);
        }

        queryWrapper.orderByDesc("create_time");

        return page(page, queryWrapper);
    }

    @Override
    public List<String> listFiles(String path) throws Exception {
        // 获取存储策略
        FileStorageStrategy storageStrategy = fileStorageFactory.getStorageStrategy();
        
        // 使用存储策略获取文件列表
        return storageStrategy.listFiles(path);
    }

    @Override
    public boolean createDirectory(String path) throws Exception {
        // 获取存储策略
        FileStorageStrategy storageStrategy = fileStorageFactory.getStorageStrategy();
        
        // 使用存储策略创建目录
        boolean created = storageStrategy.createDirectory(path);
        if (created) {
            log.info("目录创建成功: {}", path);
        }
        return created;
    }

    @Override
    public FileInfo getFileInfo(Long fileId) {
        return getById(fileId);
    }

    @Override
    public FileInfo getFileInfoByPath(String filePath) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_path", filePath);
        return getOne(queryWrapper);
    }

    @Override
    public boolean exists(String filePath) throws Exception {
        // 获取存储策略
        FileStorageStrategy storageStrategy = fileStorageFactory.getStorageStrategy();
        
        // 将完整路径转换为相对路径（如果是本地存储）
        String relativePath = filePath;
        // 使用存储策略检查文件是否存在
        return storageStrategy.exists(relativePath);
    }

    @Override
    public long getFileSize(String filePath) throws Exception {
        // 获取存储策略
        FileStorageStrategy storageStrategy = fileStorageFactory.getStorageStrategy();
        // 将完整路径转换为相对路径（如果是本地存储）
        String relativePath = filePath;
        // 使用存储策略获取文件大小
        return storageStrategy.getFileSize(relativePath);
    }

    @Override
    public InputStream getFileInputStream(String filePath) throws Exception {
        log.info("获取文件输入流: filePath={}", filePath);
        
        // 获取存储策略
        FileStorageStrategy storageStrategy = fileStorageFactory.getStorageStrategy();
        
        // 检查文件是否存在
        if (!storageStrategy.exists(filePath)) {
            log.error("物理文件不存在: filePath={}", filePath);
            throw new IllegalArgumentException("物理文件不存在: " + filePath);
        }
        
        // 使用存储策略获取文件输入流
        return storageStrategy.downloadFile(filePath);
    }

    @Override
    public InputStream getFileInputStreamById(Long fileId) throws Exception {
        log.info("根据文件ID获取文件输入流: fileId={}", fileId);
        
        FileInfo fileInfo = getById(fileId);
        if (fileInfo == null) {
            log.error("文件信息不存在: fileId={}", fileId);
            throw new IllegalArgumentException("文件不存在，ID: " + fileId);
        }
        
        return getFileInputStream(fileInfo.getFilePath());
    }

    /**
     * 构建存储路径
     */
    private String buildStoragePath(String path, String fileName) {
        return FilePathUtil.buildStoragePath(fileConfig.getLocal().getRootPath(), path, fileName);
    }
    
    /**
     * 生成文件访问URL
     */
    private String generateFileUrl(String path, String fileName) {
        return FilePathUtil.generateFileUrl(fileConfig.getLocal().getUrlPrefix(), path, fileName);
    }

} 