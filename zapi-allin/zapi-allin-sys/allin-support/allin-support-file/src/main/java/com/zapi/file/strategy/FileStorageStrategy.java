package com.zapi.file.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 文件存储策略接口
 * 定义文件存储的统一接口，支持多种存储方式（本地、OSS、FTP等）
 */
public interface FileStorageStrategy {

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @param path 存储路径
     * @return 文件访问URL
     * @throws Exception 上传异常
     */
    String uploadFile(MultipartFile file, String path) throws Exception;

    /**
     * 上传文件流
     *
     * @param inputStream 文件输入流
     * @param fileName 文件名
     * @param path 存储路径
     * @return 文件访问URL
     * @throws Exception 上传异常
     */
    String uploadFile(InputStream inputStream, String fileName, String path) throws Exception;

    /**
     * 上传文件流 替换源文件
     *
     * @param inputStream 文件输入流
     * @param fileName 文件名
     * @param path 存储路径
     * @return 文件访问URL
     * @throws Exception 上传异常
     */
    String uploadFileTH(InputStream inputStream, String fileName, String path) throws Exception;

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @return 文件输入流
     * @throws Exception 下载异常
     */
    InputStream downloadFile(String filePath) throws Exception;

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否删除成功
     * @throws Exception 删除异常
     */
    boolean deleteFile(String filePath) throws Exception;

    /**
     * 复制文件
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @return 是否复制成功
     * @throws Exception 复制异常
     */
    boolean copyFile(String sourcePath, String targetPath) throws Exception;

    /**
     * 移动文件
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @return 是否移动成功
     * @throws Exception 移动异常
     */
    boolean moveFile(String sourcePath, String targetPath) throws Exception;

    /**
     * 检查文件是否存在
     *
     * @param filePath 文件路径
     * @return 文件是否存在
     * @throws Exception 检查异常
     */
    boolean exists(String filePath) throws Exception;

    /**
     * 获取文件大小
     *
     * @param filePath 文件路径
     * @return 文件大小（字节）
     * @throws Exception 获取异常
     */
    long getFileSize(String filePath) throws Exception;

    /**
     * 获取文件列表
     *
     * @param path 目录路径
     * @return 文件列表
     * @throws Exception 获取异常
     */
    List<String> listFiles(String path) throws Exception;

    /**
     * 创建目录
     *
     * @param path 目录路径
     * @return 是否创建成功
     * @throws Exception 创建异常
     */
    boolean createDirectory(String path) throws Exception;

    /**
     * 获取存储类型
     *
     * @return 存储类型
     */
    String getStorageType();

    /***
     * 根据文件所需路径生成系统存储路径
     * @param path
     * @return
     */
    String createFilePath(String path);

    /***
     * 根据文件所需路径生成系统下载路径
     * @param path
     * @return
     */
    String createFileUrl(String path);

} 