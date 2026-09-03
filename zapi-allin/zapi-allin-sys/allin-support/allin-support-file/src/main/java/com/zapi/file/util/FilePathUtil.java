package com.zapi.file.util;

import cn.hutool.core.util.StrUtil;

/**
 * 文件路径工具类
 * 提供文件路径处理、URL生成等功能
 */
public class FilePathUtil {

    /**
     * 构建完整的文件存储路径
     *
     * @param rootPath 根路径
     * @param path 相对路径
     * @param fileName 文件名
     * @return 完整的存储路径
     */
    public static String buildStoragePath(String rootPath, String path, String fileName) {
        String fullPath = rootPath;
        
        if (StrUtil.isNotBlank(path)) {
            // 确保路径以 / 结尾
            if (!rootPath.endsWith("/") && !path.startsWith("/")) {
                fullPath += "/";
            }
            fullPath += path;
        }
        
        if (StrUtil.isNotBlank(fileName)) {
            // 确保路径以 / 结尾
            if (!fullPath.endsWith("/")) {
                fullPath += "/";
            }
            fullPath += fileName;
        }
        
        return fullPath;
    }

    /**
     * 构建相对路径（用于存储策略）
     *
     * @param path 路径
     * @param fileName 文件名
     * @return 相对路径
     */
    public static String buildRelativePath(String path, String fileName) {
        if (StrUtil.isNotBlank(path)) {
            return path + "/" + fileName;
        }
        return fileName;
    }

    /**
     * 从文件URL中提取文件名
     *
     * @param fileUrl 文件URL
     * @return 文件名
     */
    public static String extractFileNameFromUrl(String fileUrl) {
        if (StrUtil.isBlank(fileUrl)) {
            return "";
        }
        String[] parts = fileUrl.split("/");
        return parts[parts.length - 1];
    }

    /**
     * 生成文件访问URL
     *
     * @param urlPrefix URL前缀
     * @param path 路径
     * @param fileName 文件名
     * @return 文件访问URL
     */
    public static String generateFileUrl(String urlPrefix, String path, String fileName) {
        String url = urlPrefix;
        
        if (StrUtil.isNotBlank(path)) {
            if (!url.endsWith("/")) {
                url += "/";
            }
            url += path.replace("\\", "/");
            if (!url.endsWith("/")) {
                url += "/";
            }
        } else if (!url.endsWith("/")) {
            url += "/";
        }
        
        return url + fileName;
    }

    /**
     * 将完整路径转换为相对路径
     *
     * @param fullPath 完整路径
     * @param rootPath 根路径
     * @return 相对路径
     */
    public static String convertToRelativePath(String fullPath, String rootPath) {
        if (StrUtil.isBlank(fullPath)) {
            return "";
        }
        
        if (fullPath.startsWith(rootPath)) {
            String relativePath = fullPath.substring(rootPath.length());
            // 确保相对路径不以/开头
            if (relativePath.startsWith("/")) {
                relativePath = relativePath.substring(1);
            }
            return relativePath;
        }
        
        // 如果不是以根路径开头，可能已经是相对路径了
        return fullPath;
    }

    /**
     * 标准化路径分隔符（统一使用正斜杠）
     *
     * @param path 路径
     * @return 标准化后的路径
     */
    public static String normalizePath(String path) {
        if (StrUtil.isBlank(path)) {
            return "";
        }
        return path.replace("\\", "/");
    }

    /**
     * 确保路径以指定字符结尾
     *
     * @param path 路径
     * @param suffix 后缀字符
     * @return 处理后的路径
     */
    public static String ensureEndsWith(String path, String suffix) {
        if (StrUtil.isBlank(path)) {
            return suffix;
        }
        if (!path.endsWith(suffix)) {
            return path + suffix;
        }
        return path;
    }

    /**
     * 确保路径以指定字符开头
     *
     * @param path 路径
     * @param prefix 前缀字符
     * @return 处理后的路径
     */
    public static String ensureStartsWith(String path, String prefix) {
        if (StrUtil.isBlank(path)) {
            return prefix;
        }
        if (!path.startsWith(prefix)) {
            return prefix + path;
        }
        return path;
    }

    /**
     * 组合路径片段
     *
     * @param segments 路径片段
     * @return 组合后的路径
     */
    public static String joinPath(String... segments) {
        if (segments == null || segments.length == 0) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < segments.length; i++) {
            String segment = segments[i];
            if (StrUtil.isBlank(segment)) {
                continue;
            }
            
            segment = normalizePath(segment);
            
            if (result.length() > 0 && !result.toString().endsWith("/") && !segment.startsWith("/")) {
                result.append("/");
            }
            
            if (result.length() > 0 && result.toString().endsWith("/") && segment.startsWith("/")) {
                segment = segment.substring(1);
            }
            
            result.append(segment);
        }
        
        return result.toString();
    }
}
