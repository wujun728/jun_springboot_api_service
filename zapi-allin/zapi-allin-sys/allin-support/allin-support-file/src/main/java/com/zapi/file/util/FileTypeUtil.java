package com.zapi.file.util;

import cn.hutool.core.util.StrUtil;

/**
 * 文件类型工具类
 * 提供文件扩展名识别、类型判断等功能
 */
public class FileTypeUtil {

    /**
     * 从文件名中获取扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名（包含点号，如 .jpg）
     */
    public static String getFileExtension(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex) : "";
    }

    /**
     * 根据扩展名获取文件类型大类
     *
     * @param extension 文件扩展名（包含点号）
     * @return 文件类型大类名称
     */
    public static String getFriendlyFileType(String extension) {
        if (StrUtil.isBlank(extension)) {
            return "其他";
        }

        String ext = extension.toLowerCase();
        switch (ext) {
            // 图片类
            case ".jpg":
            case ".jpeg":
            case ".png":
            case ".gif":
            case ".bmp":
            case ".webp":
            case ".svg":
            case ".ico":
            case ".tiff":
            case ".tif":
                return "图片";
            
            // 文档类
            case ".doc":
            case ".docx":
            case ".pdf":
            case ".txt":
            case ".rtf":
            case ".md":
            case ".odt":
                return "文档";
            
            // 表格类
            case ".xls":
            case ".xlsx":
            case ".csv":
            case ".ods":
                return "表格";
            
            // 演示文稿类
            case ".ppt":
            case ".pptx":
            case ".odp":
                return "演示文稿";
            
            // 压缩文件类
            case ".zip":
            case ".rar":
            case ".7z":
            case ".tar":
            case ".gz":
            case ".bz2":
            case ".xz":
                return "压缩包";
            
            // 音频类
            case ".mp3":
            case ".wav":
            case ".flac":
            case ".aac":
            case ".ogg":
            case ".wma":
            case ".m4a":
                return "音频";
            
            // 视频类
            case ".mp4":
            case ".avi":
            case ".mkv":
            case ".mov":
            case ".wmv":
            case ".flv":
            case ".webm":
            case ".m4v":
                return "视频";
            
            // 代码类
            case ".java":
            case ".js":
            case ".html":
            case ".htm":
            case ".css":
            case ".xml":
            case ".json":
            case ".sql":
            case ".py":
            case ".php":
            case ".cpp":
            case ".c":
            case ".h":
            case ".cs":
            case ".go":
            case ".rb":
            case ".swift":
            case ".kt":
                return "代码";
            
            // 配置文件类
            case ".yml":
            case ".yaml":
            case ".properties":
            case ".ini":
            case ".conf":
            case ".config":
                return "配置";
            
            // 日志文件类
            case ".log":
                return "日志";
            
            // 可执行文件类
            case ".exe":
            case ".msi":
            case ".dmg":
            case ".pkg":
            case ".deb":
            case ".rpm":
                return "程序";
            
            default:
                return "其他";
        }
    }

    /**
     * 根据扩展名获取MIME类型
     *
     * @param extension 文件扩展名（包含点号）
     * @return MIME类型
     */
    public static String getContentType(String extension) {
        if (StrUtil.isBlank(extension)) {
            return "application/octet-stream";
        }

        String ext = extension.toLowerCase();
        switch (ext) {
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".gif":
                return "image/gif";
            case ".bmp":
                return "image/bmp";
            case ".webp":
                return "image/webp";
            case ".svg":
                return "image/svg+xml";
            case ".ico":
                return "image/x-icon";
            case ".pdf":
                return "application/pdf";
            case ".doc":
                return "application/msword";
            case ".docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case ".xls":
                return "application/vnd.ms-excel";
            case ".xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case ".ppt":
                return "application/vnd.ms-powerpoint";
            case ".pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case ".txt":
                return "text/plain";
            case ".html":
            case ".htm":
                return "text/html";
            case ".css":
                return "text/css";
            case ".js":
                return "application/javascript";
            case ".json":
                return "application/json";
            case ".xml":
                return "application/xml";
            case ".zip":
                return "application/zip";
            case ".rar":
                return "application/vnd.rar";
            case ".7z":
                return "application/x-7z-compressed";
            case ".mp3":
                return "audio/mpeg";
            case ".wav":
                return "audio/wav";
            case ".mp4":
                return "video/mp4";
            case ".avi":
                return "video/x-msvideo";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * 判断是否为图片文件
     *
     * @param extension 文件扩展名
     * @return true if 是图片文件
     */
    public static boolean isImageFile(String extension) {
        return "图片".equals(getFriendlyFileType(extension));
    }

    /**
     * 判断是否为文档文件
     *
     * @param extension 文件扩展名
     * @return true if 是文档文件
     */
    public static boolean isDocumentFile(String extension) {
        String type = getFriendlyFileType(extension);
        return "文档".equals(type) || "表格".equals(type) || "演示文稿".equals(type);
    }

    /**
     * 判断是否为媒体文件（音频或视频）
     *
     * @param extension 文件扩展名
     * @return true if 是媒体文件
     */
    public static boolean isMediaFile(String extension) {
        String type = getFriendlyFileType(extension);
        return "音频".equals(type) || "视频".equals(type);
    }

    /**
     * 判断是否为压缩文件
     *
     * @param extension 文件扩展名
     * @return true if 是压缩文件
     */
    public static boolean isArchiveFile(String extension) {
        return "压缩包".equals(getFriendlyFileType(extension));
    }

    /**
     * 判断是否为代码文件
     *
     * @param extension 文件扩展名
     * @return true if 是代码文件
     */
    public static boolean isCodeFile(String extension) {
        return "代码".equals(getFriendlyFileType(extension));
    }
}
