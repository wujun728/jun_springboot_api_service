package com.zapi.file.strategy.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zapi.file.config.FileConfig;
import com.zapi.file.strategy.FileStorageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地文件存储策略实现类
 * 将文件存储在本地文件系统中
 */
@Slf4j
@Component("localFileStorageStrategy")
public class LocalFileStorageStrategy implements FileStorageStrategy {

    @Autowired
    private FileConfig fileConfig;

    @Override
    public String uploadFile(MultipartFile file, String path) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String fileName = IdUtil.fastSimpleUUID() + "." + fileExtension;

        // 构建完整路径
        String fullPath = buildFullPath(path, fileName);
        File targetFile = new File(fullPath);

        // 确保目录存在
        FileUtil.mkParentDirs(targetFile);

        // 保存文件
        file.transferTo(targetFile);

        // 返回文件访问URL
        return buildFileUrl(path, fileName);
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileName, String path) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("文件输入流不能为空");
        }

        // 生成唯一文件名
        String fileExtension = getFileExtension(fileName);
        String uniqueFileName = IdUtil.fastSimpleUUID() + "." + fileExtension;

        // 构建完整路径
        String fullPath = buildFullPath(path, uniqueFileName);
        File targetFile = new File(fullPath);

        // 确保目录存在
        FileUtil.mkParentDirs(targetFile);

        // 保存文件
        Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // 返回文件访问URL
        return buildFileUrl(path, uniqueFileName);
    }
    @Override
    public String uploadFileTH(InputStream inputStream, String fileName, String path) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("文件输入流不能为空");
        }

        // 生成唯一文件名
        //String fileExtension = getFileExtension(fileName);
        //String uniqueFileName = IdUtil.fastSimpleUUID() + "." + fileExtension;

        // 构建完整路径
        String fullPath = buildFullPath(path, fileName);
        File targetFile = new File(fullPath);

        // 确保目录存在
        FileUtil.mkParentDirs(targetFile);

        // 保存文件
        Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // 返回文件访问URL
        return buildFileUrl(path, fileName);
    }

    @Override
    public InputStream downloadFile(String filePath) throws Exception {
        String fullPath = buildFullPath(filePath);
        File file = new File(fullPath);

        if (!file.exists()) {
            throw new IOException("文件不存在: " + filePath);
        }

        return new FileInputStream(file);
    }

    @Override
    public boolean deleteFile(String filePath) throws Exception {
        String fullPath = buildFullPath(filePath);
        File file = new File(fullPath);

        if (!file.exists()) {
            log.warn("文件不存在，无法删除: {}", filePath);
            return false;
        }

        return FileUtil.del(file);
    }

    @Override
    public boolean copyFile(String sourcePath, String targetPath) throws Exception {
        String sourceFullPath = buildFullPath(sourcePath);
        String targetFullPath = buildFullPath(targetPath);

        File sourceFile = new File(sourceFullPath);
        File targetFile = new File(targetFullPath);

        if (!sourceFile.exists()) {
            throw new IOException("源文件不存在: " + sourcePath);
        }

        // 确保目标目录存在
        FileUtil.mkParentDirs(targetFile);

        // 复制文件
        FileUtil.copy(sourceFile, targetFile, true);
        return true;
    }

    @Override
    public boolean moveFile(String sourcePath, String targetPath) throws Exception {
        String sourceFullPath = buildFullPath(sourcePath);
        String targetFullPath = buildFullPath(targetPath);

        File sourceFile = new File(sourceFullPath);
        File targetFile = new File(targetFullPath);

        if (!sourceFile.exists()) {
            throw new IOException("源文件不存在: " + sourcePath);
        }

        // 确保目标目录存在
        FileUtil.mkParentDirs(targetFile);

        // 移动文件
        FileUtil.move(sourceFile, targetFile, true);
        return targetFile.exists();
    }

    @Override
    public boolean exists(String filePath) throws Exception {
        String fullPath = buildFullPath(filePath);
        File file = new File(fullPath);
        return file.exists();
    }

    @Override
    public long getFileSize(String filePath) throws Exception {
        String fullPath = buildFullPath(filePath);
        File file = new File(fullPath);

        if (!file.exists()) {
            throw new IOException("文件不存在: " + filePath);
        }

        return file.length();
    }

    @Override
    public List<String> listFiles(String path) throws Exception {
        String fullPath = buildFullPath(path);
        File directory = new File(fullPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IOException("目录不存在: " + path);
        }

        List<String> fileList = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // 返回相对路径
                    String relativePath = file.getPath().substring(
                        fileConfig.getLocal().getRootPath().length() + 1
                    );
                    fileList.add(relativePath.replace(File.separator, "/"));
                }
            }
        }

        return fileList;
    }

    @Override
    public boolean createDirectory(String path) throws Exception {
        String fullPath = buildFullPath(path);
        File directory = new File(fullPath);

        if (directory.exists()) {
            return directory.isDirectory();
        }

        return directory.mkdirs();
    }

    @Override
    public String getStorageType() {
        return "LOCAL";
    }

    /***
     * 根据文件所需路径生成系统存储路径
     * @param path
     * @return
     */
    @Override
    public String createFilePath(String path){
        return buildFullPath(path, null);
    }

    /***
     * 根据文件所需路径生成系统下载路径
     * @param path
     * @return
     */
    @Override
    public String createFileUrl(String path){

        String urlPrefix = fileConfig.getLocal().getUrlPrefix();
        String url = urlPrefix;

        if (StrUtil.isNotBlank(path)) {
            // 确保URL路径分隔符正确
            if (!url.endsWith("/") && !path.startsWith("/")) {
                url += "/";
            }
            url = url + path.replace(File.separator, "/");
            if (!url.endsWith("/")) {
                url += "/";
            }
        } else if (!url.endsWith("/")) {
            url += "/";
        }
        return url+path;
    }


    /**
     * 构建完整文件路径
     *
     * @param path 相对路径
     * @return 完整路径
     */
    private String buildFullPath(String path) {
        return buildFullPath(path, null);
    }

    /**
     * 构建完整文件路径
     *
     * @param path 相对路径或绝对路径
     * @param fileName 文件名
     * @return 完整路径
     */
    private String buildFullPath(String path, String fileName) {
        String rootPath = fileConfig.getLocal().getRootPath();
        
        // 如果path已经是完整的绝对路径（以rootPath开头），直接使用
        if (StrUtil.isNotBlank(path) && path.startsWith(rootPath)) {
            // path已经是完整路径，直接返回
            if (StrUtil.isBlank(fileName)) {
                return path;
            }
            // 如果还有fileName，拼接上去
            String fullPath = path;
            if (!fullPath.endsWith(File.separator) && !fullPath.endsWith("/")) {
                fullPath += File.separator;
            }
            return fullPath + fileName;
        }
        
        // path是相对路径，需要拼接rootPath
        String fullPath = rootPath;

        if (StrUtil.isNotBlank(path)) {
            // 确保路径分隔符正确
            if (!fullPath.endsWith(File.separator) && !fullPath.endsWith("/") 
                && !path.startsWith(File.separator) && !path.startsWith("/")) {
                fullPath += File.separator;
            }
            fullPath = fullPath + path;
        }

        if (StrUtil.isNotBlank(fileName)) {
            // 确保路径分隔符正确
            if (!fullPath.endsWith(File.separator) && !fullPath.endsWith("/")) {
                fullPath += File.separator;
            }
            fullPath = fullPath + fileName;
        }

        return fullPath;
    }

    /**
     * 构建文件访问URL
     *
     * @param path 相对路径
     * @param fileName 文件名
     * @return 文件访问URL
     */
    private String buildFileUrl(String path, String fileName) {
        String urlPrefix = fileConfig.getLocal().getUrlPrefix();
        String url = urlPrefix;

        if (StrUtil.isNotBlank(path)) {
            // 确保URL路径分隔符正确
            if (!url.endsWith("/") && !path.startsWith("/")) {
                url += "/";
            }
            url = url + path.replace(File.separator, "/");
            if (!url.endsWith("/")) {
                url += "/";
            }
        } else if (!url.endsWith("/")) {
            url += "/";
        }

        return url + fileName;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return "";
        }

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
} 