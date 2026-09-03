package com.zapi.file.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.file.entity.FileInfo;
import com.zapi.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件管理控制器
 * 提供文件上传、下载、管理等功能的REST接口
 */
@Api(tags = "文件管理")
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     * 
     * @param file 上传的文件
     * @param path 存储路径（可选，默认为根目录）
     * @param description 文件描述（可选）
     * @param uploadUserId 上传用户ID（可选）
     * @param uploadUsername 上传用户名（可选）
     * @return 文件信息
     */
    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public Result<FileInfo> uploadFile(
            @ApiParam("上传文件") @RequestParam("file") MultipartFile file,
            @ApiParam("存储路径") @RequestParam(value = "path", required = false, defaultValue = "") String path,
            @ApiParam("文件描述") @RequestParam(value = "description", required = false) String description,
            @ApiParam("上传用户ID") @RequestParam(value = "uploadUserId", required = false) Long uploadUserId,
            @ApiParam("上传用户名") @RequestParam(value = "uploadUsername", required = false) String uploadUsername) {
        try {
            FileInfo fileInfo = fileService.uploadFile(file, path, description, uploadUserId, uploadUsername);
            return Result.success("文件上传成功", fileInfo);
        } catch (Exception e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 下载文件
     * 
     * @param fileId 文件ID
     * @param response HTTP响应对象
     */
    @ApiOperation("下载文件")
    @GetMapping("/download/{fileId}")
    public void download(
            @ApiParam("文件ID") @PathVariable Long fileId,
            HttpServletResponse response) {
        try {
            fileService.downloadFile(fileId, response);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("下载文件失败：" + e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     * 
     * @param fileId 文件ID
     * @return 删除结果
     */
    @ApiOperation("删除文件")
    @DeleteMapping("/delete/{fileId}")
    public Result<String> deleteFile(@ApiParam("文件ID") @PathVariable Long fileId) {
        try {
            boolean success = fileService.deleteFile(fileId);
            if (success) {
                return Result.success("文件删除成功");
            } else {
                return Result.error("文件删除失败");
            }
        } catch (Exception e) {
            return Result.error("文件删除失败: " + e.getMessage());
        }
    }

    /**
     * 复制文件
     * 
     * @param sourceFileId 源文件ID
     * @param targetPath 目标路径
     * @param description 文件描述（可选）
     * @param uploadUserId 上传用户ID（可选）
     * @param uploadUsername 上传用户名（可选）
     * @return 新的文件信息
     */
    @ApiOperation("复制文件")
    @PostMapping("/copy")
    public Result<FileInfo> copyFile(
            @ApiParam("源文件ID") @RequestParam Long sourceFileId,
            @ApiParam("目标路径") @RequestParam String targetPath,
            @ApiParam("文件描述") @RequestParam(value = "description", required = false) String description,
            @ApiParam("上传用户ID") @RequestParam(value = "uploadUserId", required = false) Long uploadUserId,
            @ApiParam("上传用户名") @RequestParam(value = "uploadUsername", required = false) String uploadUsername) {
        try {
            FileInfo fileInfo = fileService.copyFile(sourceFileId, targetPath, description, uploadUserId, uploadUsername);
            return Result.success("文件复制成功", fileInfo);
        } catch (Exception e) {
            return Result.error("文件复制失败: " + e.getMessage());
        }
    }

    /**
     * 移动文件
     * 
     * @param fileId 文件ID
     * @param targetPath 目标路径
     * @return 移动结果
     */
    @ApiOperation("移动文件")
    @PutMapping("/move")
    public Result<String> moveFile(
            @ApiParam("文件ID") @RequestParam Long fileId,
            @ApiParam("目标路径") @RequestParam String targetPath) {
        try {
            boolean success = fileService.moveFile(fileId, targetPath);
            if (success) {
                return Result.success("文件移动成功");
            } else {
                return Result.error("文件移动失败");
            }
        } catch (Exception e) {
            return Result.error("文件移动失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询文件列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param fileName 文件名（可选，支持模糊查询）
     * @param fileType 文件类型（可选）
     * @param storageType 存储类型（可选）
     * @param uploadUserId 上传用户ID（可选）
     * @return 文件列表分页结果
     */
    @ApiOperation("分页查询文件列表")
    @GetMapping("/list")
    public Result<Page<FileInfo>> getFileList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("文件名") @RequestParam(value = "fileName", required = false) String fileName,
            @ApiParam("文件类型") @RequestParam(value = "fileType", required = false) String fileType,
            @ApiParam("存储类型") @RequestParam(value = "storageType", required = false) String storageType,
            @ApiParam("上传用户ID") @RequestParam(value = "uploadUserId", required = false) Long uploadUserId) {
        try {
            Page<FileInfo> page = fileService.getFileList(current, size, fileName, fileType, storageType, uploadUserId);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error("查询文件列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件列表
     * 
     * @param path 目录路径
     * @return 文件列表
     */
    @ApiOperation("获取目录文件列表")
    @GetMapping("/list/{path}")
    public Result<List<String>> listFiles(@ApiParam("目录路径") @PathVariable String path) {
        try {
            List<String> files = fileService.listFiles(path);
            return Result.success(files);
        } catch (Exception e) {
            return Result.error("获取文件列表失败: " + e.getMessage());
        }
    }

    /**
     * 创建目录
     * 
     * @param path 目录路径
     * @return 创建结果
     */
    @ApiOperation("创建目录")
    @PostMapping("/mkdir")
    public Result<String> createDirectory(@ApiParam("目录路径") @RequestParam String path) {
        try {
            boolean success = fileService.createDirectory(path);
            if (success) {
                return Result.success("目录创建成功");
            } else {
                return Result.error("目录创建失败");
            }
        } catch (Exception e) {
            return Result.error("目录创建失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件信息
     * 
     * @param fileId 文件ID
     * @return 文件信息
     */
    @ApiOperation("获取文件信息")
    @GetMapping("/info/{fileId}")
    public Result<FileInfo> getFileInfo(@ApiParam("文件ID") @PathVariable Long fileId) {
        try {
            FileInfo fileInfo = fileService.getFileInfo(fileId);
            if (fileInfo != null) {
                return Result.success(fileInfo);
            } else {
                return Result.error("文件不存在");
            }
        } catch (Exception e) {
            return Result.error("获取文件信息失败: " + e.getMessage());
        }
    }

    /**
     * 检查文件是否存在
     * 
     * @param filePath 文件路径
     * @return 检查结果
     */
    @ApiOperation("检查文件是否存在")
    @GetMapping("/exists")
    public Result<Boolean> exists(@ApiParam("文件路径") @RequestParam String filePath) {
        try {
            boolean exists = fileService.exists(filePath);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error("检查文件失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件大小
     * 
     * @param filePath 文件路径
     * @return 文件大小（字节）
     */
    @ApiOperation("获取文件大小")
    @GetMapping("/size")
    public Result<Long> getFileSize(@ApiParam("文件路径") @RequestParam String filePath) {
        try {
            long size = fileService.getFileSize(filePath);
            return Result.success(size);
        } catch (Exception e) {
            return Result.error("获取文件大小失败: " + e.getMessage());
        }
    }
} 