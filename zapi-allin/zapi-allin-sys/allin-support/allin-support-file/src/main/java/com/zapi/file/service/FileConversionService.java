package com.zapi.file.service;

import com.zapi.file.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件转换服务接口
 * 提供各种文件格式转换功能
 */
public interface FileConversionService {

    /**
     * Word文档转PDF
     * 
     * @param file Word文件
     * @param outputFileName 输出文件名（可选）
     * @param uploadUserId 上传用户ID
     * @param uploadUsername 上传用户名
     * @return 转换后的PDF文件信息
     * @throws Exception 转换异常
     */
    FileInfo convertWordToPdf(MultipartFile file, String outputFileName, 
                             Long uploadUserId, String uploadUsername) throws Exception;

    // OFD转PDF功能已禁用
    /*
    FileInfo convertOfdToPdf(MultipartFile file, String outputFileName, 
                            Long uploadUserId, String uploadUsername) throws Exception;
    */

    /**
     * PDF转Word文档
     * 
     * @param file PDF文件
     * @param outputFileName 输出文件名（可选）
     * @param uploadUserId 上传用户ID
     * @param uploadUsername 上传用户名
     * @return 转换后的Word文件信息
     * @throws Exception 转换异常
     */
    FileInfo convertPdfToWord(MultipartFile file, String outputFileName, 
                             Long uploadUserId, String uploadUsername) throws Exception;

    /**
     * 通用文件转换
     * 根据源文件和目标格式进行转换
     * 
     * @param sourceFile 源文件信息
     * @param targetFormat 目标格式
     * @param outputFileName 输出文件名
     * @param uploadUserId 上传用户ID
     * @param uploadUsername 上传用户名
     * @return 转换后的文件信息
     * @throws Exception 转换异常
     */
    FileInfo convertFile(FileInfo sourceFile, String targetFormat, String outputFileName, 
                        Long uploadUserId, String uploadUsername) throws Exception;

    /**
     * 批量文件转换
     * 
     * @param files 文件数组
     * @param targetFormat 目标格式
     * @param uploadUserId 上传用户ID
     * @param uploadUsername 上传用户名
     * @return 转换后的文件信息列表
     * @throws Exception 转换异常
     */
    List<FileInfo> batchConvert(MultipartFile[] files, String targetFormat, 
                               Long uploadUserId, String uploadUsername) throws Exception;

    /**
     * 获取支持的转换格式
     * 
     * @return 支持的转换格式映射
     */
    Map<String, List<String>> getSupportedFormats();

    /**
     * 获取转换进度
     * 
     * @param taskId 任务ID
     * @return 进度信息
     */
    Map<String, Object> getConversionProgress(String taskId);

    /**
     * 验证文件格式是否支持转换
     * 
     * @param fileName 文件名
     * @param targetFormat 目标格式
     * @return 是否支持转换
     */
    boolean isSupportedConversion(String fileName, String targetFormat);
}
