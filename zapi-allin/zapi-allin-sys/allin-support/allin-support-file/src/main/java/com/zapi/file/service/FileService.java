package com.zapi.file.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.file.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * 文件服务接口
 * 提供文件上传、下载、管理等功能的统一接口
 */
public interface FileService extends IService<FileInfo> {

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @param path 存储路径
     * @param description 文件描述
     * @param uploadUserId 上传用户ID
     * @param uploadUsername 上传用户名
     * @return 文件信息
     * @throws Exception 上传异常
     */
    FileInfo uploadFile(MultipartFile file, String path, String description, 
                       Long uploadUserId, String uploadUsername) throws Exception;

    /**
     * 上传文件
     *
     * @param path 存储路径
     * @param fileName 文件描述
     * @param uploadUserId 上传用户ID
     * @param uploadUsername 上传用户名
     * @return 文件信息
     * @throws Exception 上传异常
     */
    FileInfo uploadFile( String path, String fileName,
                        Long uploadUserId, String uploadUsername) throws Exception;

    /**
     * 上传文件流
     *
     * @param inputStream 文件输入流
     * @param fileName 文件名
     * @param path 存储路径
     * @param description 文件描述
     * @param uploadUserId 上传用户ID
     * @param uploadUsername 上传用户名
     * @return 文件信息
     * @throws Exception 上传异常
     */
    FileInfo uploadFile(InputStream inputStream, String fileName, String path, 
                       String description, Long uploadUserId, String uploadUsername) throws Exception;

    /**
     * 下载文件
     *
     * @param fileId 文件ID
     * @param response HTTP响应对象
     * @throws Exception 下载异常
     */
    void downloadFile(Long fileId, HttpServletResponse response) throws Exception;

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @param response HTTP响应对象
     * @throws Exception 下载异常
     */
    void downloadFile(String filePath,String fileName, HttpServletResponse response) throws Exception;

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 是否删除成功
     * @throws Exception 删除异常
     */
    boolean deleteFile(Long fileId) throws Exception;

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
     * @param sourceFileId 源文件ID
     * @param targetPath 目标路径
     * @param description 文件描述
     * @param uploadUserId 上传用户ID
     * @param uploadUsername 上传用户名
     * @return 新的文件信息
     * @throws Exception 复制异常
     */
    FileInfo copyFile(Long sourceFileId, String targetPath, String description, 
                     Long uploadUserId, String uploadUsername) throws Exception;

    /**
     * 复制文件
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @param description 文件描述
     * @param uploadUserId 上传用户ID
     * @param uploadUsername 上传用户名
     * @return 新的文件信息
     * @throws Exception 复制异常
     */
    FileInfo copyFile(String sourcePath, String targetPath, String description, 
                     Long uploadUserId, String uploadUsername) throws Exception;

    /**
     * 移动文件
     *
     * @param fileId 文件ID
     * @param targetPath 目标路径
     * @return 是否移动成功
     * @throws Exception 移动异常
     */
    boolean moveFile(Long fileId, String targetPath) throws Exception;

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
     * 分页查询文件列表
     *
     * @param current 当前页
     * @param size 每页大小
     * @param fileName 文件名
     * @param fileType 文件类型
     * @param storageType 存储类型
     * @param uploadUserId 上传用户ID
     * @return 文件列表分页结果
     */
    Page<FileInfo> getFileList(Integer current, Integer size, String fileName, 
                              String fileType, String storageType, Long uploadUserId);

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
     * 获取文件信息
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    FileInfo getFileInfo(Long fileId);

    /**
     * 根据文件路径获取文件信息
     *
     * @param filePath 文件路径
     * @return 文件信息
     */
    FileInfo getFileInfoByPath(String filePath);

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
     * 获取文件字节流
     *
     * @param filePath 文件路径
     * @return 文件字节流
     * @throws Exception 获取异常
     */
    InputStream getFileInputStream(String filePath) throws Exception;

    /**
     * 根据文件ID获取文件字节流
     *
     * @param fileId 文件ID
     * @return 文件字节流
     * @throws Exception 获取异常
     */
    InputStream getFileInputStreamById(Long fileId) throws Exception;
}