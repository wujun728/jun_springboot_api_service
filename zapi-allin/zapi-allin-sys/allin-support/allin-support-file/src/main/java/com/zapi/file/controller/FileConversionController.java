package com.zapi.file.controller;

import com.zapi.common.Result;
import com.zapi.file.entity.FileInfo;
import com.zapi.file.service.FileConversionService;
import com.zapi.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件转换控制器
 * 提供文件格式转换功能的REST接口
 * 支持word转pdf、ofd转pdf、pdf转word等常见格式转换
 */
@Api(tags = "文件转换")
@RestController
@RequestMapping("/file/conversion")
public class FileConversionController {

    @Autowired
    private FileConversionService fileConversionService;
    
    @Autowired
    private FileService fileService;

    /**
     * Word文档转PDF
     * 支持doc、docx格式转换为pdf
     * 
     * @param file 上传的Word文件
     * @param outputFileName 输出文件名（可选，默认使用原文件名）
     * @param uploadUserId 上传用户ID（可选）
     * @param uploadUsername 上传用户名（可选）
     * @return 转换后的PDF文件信息
     */
    @ApiOperation("Word文档转PDF")
    @PostMapping("/word-to-pdf")
    public Result<FileInfo> wordToPdf(
            @ApiParam("Word文件") @RequestParam("file") MultipartFile file,
            @ApiParam("输出文件名") @RequestParam(value = "outputFileName", required = false) String outputFileName,
            @ApiParam("上传用户ID") @RequestParam(value = "uploadUserId", required = false) Long uploadUserId,
            @ApiParam("上传用户名") @RequestParam(value = "uploadUsername", required = false) String uploadUsername) {
        try {
            // 验证文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || 
                (!originalFilename.toLowerCase().endsWith(".doc") && 
                 !originalFilename.toLowerCase().endsWith(".docx"))) {
                return Result.error("只支持.doc和.docx格式的Word文档");
            }

            FileInfo convertedFile = fileConversionService.convertWordToPdf(
                file, outputFileName, uploadUserId, uploadUsername);
            return Result.success("Word转PDF成功", convertedFile);
        } catch (Exception e) {
            return Result.error("Word转PDF失败：" + e.getMessage());
        }
    }

    // OFD转PDF功能已禁用
    // 如需使用，请使用专门的OFD处理服务
    /*
    @ApiOperation("OFD文档转PDF")
    @PostMapping("/ofd-to-pdf")
    public Result<FileInfo> ofdToPdf(
            @ApiParam("OFD文件") @RequestParam("file") MultipartFile file,
            @ApiParam("输出文件名") @RequestParam(value = "outputFileName", required = false) String outputFileName,
            @ApiParam("上传用户ID") @RequestParam(value = "uploadUserId", required = false) Long uploadUserId,
            @ApiParam("上传用户名") @RequestParam(value = "uploadUsername", required = false) String uploadUsername) {
        return Result.error("OFD转PDF功能已禁用");
    }
    */

    /**
     * PDF转Word文档
     * 将PDF格式文件转换为Word格式（docx）
     * 
     * @param file 上传的PDF文件
     * @param outputFileName 输出文件名（可选）
     * @param uploadUserId 上传用户ID（可选）
     * @param uploadUsername 上传用户名（可选）
     * @return 转换后的Word文件信息
     */
    @ApiOperation("PDF转Word文档")
    @PostMapping("/pdf-to-word")
    public Result<FileInfo> pdfToWord(
            @ApiParam("PDF文件") @RequestParam("file") MultipartFile file,
            @ApiParam("输出文件名") @RequestParam(value = "outputFileName", required = false) String outputFileName,
            @ApiParam("上传用户ID") @RequestParam(value = "uploadUserId", required = false) Long uploadUserId,
            @ApiParam("上传用户名") @RequestParam(value = "uploadUsername", required = false) String uploadUsername) {
        try {
            // 验证文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
                return Result.error("只支持.pdf格式的文档");
            }

            FileInfo convertedFile = fileConversionService.convertPdfToWord(
                file, outputFileName, uploadUserId, uploadUsername);
            return Result.success("PDF转Word成功", convertedFile);
        } catch (Exception e) {
            return Result.error("PDF转Word失败：" + e.getMessage());
        }
    }

    /**
     * 通过文件ID进行转换
     * 支持已上传到文件服务器的文件进行转换
     * 
     * @param fileId 源文件ID
     * @param targetFormat 目标格式（pdf, docx, ofd）
     * @param outputFileName 输出文件名（可选）
     * @param uploadUserId 上传用户ID（可选）
     * @param uploadUsername 上传用户名（可选）
     * @return 转换后的文件信息
     */
    @ApiOperation("通过文件ID进行转换")
    @PostMapping("/convert-by-id")
    public Result<FileInfo> convertById(
            @ApiParam("源文件ID") @RequestParam("fileId") Long fileId,
            @ApiParam("目标格式") @RequestParam("targetFormat") String targetFormat,
            @ApiParam("输出文件名") @RequestParam(value = "outputFileName", required = false) String outputFileName,
            @ApiParam("上传用户ID") @RequestParam(value = "uploadUserId", required = false) Long uploadUserId,
            @ApiParam("上传用户名") @RequestParam(value = "uploadUsername", required = false) String uploadUsername) {
        try {
            // 获取源文件信息
            FileInfo sourceFile = fileService.getFileInfo(fileId);
            if (sourceFile == null) {
                return Result.error("源文件不存在");
            }

            // 验证目标格式
            if (!isValidTargetFormat(targetFormat)) {
                return Result.error("不支持的目标格式：" + targetFormat);
            }

            FileInfo convertedFile = fileConversionService.convertFile(
                sourceFile, targetFormat, outputFileName, uploadUserId, uploadUsername);
            return Result.success("文件转换成功", convertedFile);
        } catch (Exception e) {
            return Result.error("文件转换失败：" + e.getMessage());
        }
    }

    /**
     * 批量文件转换
     * 
     * @param files 要转换的文件列表
     * @param targetFormat 目标格式
     * @param uploadUserId 上传用户ID（可选）
     * @param uploadUsername 上传用户名（可选）
     * @return 转换结果列表
     */
    @ApiOperation("批量文件转换")
    @PostMapping("/batch-convert")
    public Result<java.util.List<FileInfo>> batchConvert(
            @ApiParam("文件列表") @RequestParam("files") MultipartFile[] files,
            @ApiParam("目标格式") @RequestParam("targetFormat") String targetFormat,
            @ApiParam("上传用户ID") @RequestParam(value = "uploadUserId", required = false) Long uploadUserId,
            @ApiParam("上传用户名") @RequestParam(value = "uploadUsername", required = false) String uploadUsername) {
        try {
            if (files == null || files.length == 0) {
                return Result.error("请选择要转换的文件");
            }

            if (!isValidTargetFormat(targetFormat)) {
                return Result.error("不支持的目标格式：" + targetFormat);
            }

            java.util.List<FileInfo> convertedFiles = fileConversionService.batchConvert(
                files, targetFormat, uploadUserId, uploadUsername);
            return Result.success("批量转换成功", convertedFiles);
        } catch (Exception e) {
            return Result.error("批量转换失败：" + e.getMessage());
        }
    }

    /**
     * 获取支持的转换格式
     * 
     * @return 支持的转换格式列表
     */
    @ApiOperation("获取支持的转换格式")
    @GetMapping("/supported-formats")
    public Result<java.util.Map<String, java.util.List<String>>> getSupportedFormats() {
        try {
            java.util.Map<String, java.util.List<String>> formats = fileConversionService.getSupportedFormats();
            return Result.success("获取成功", formats);
        } catch (Exception e) {
            return Result.error("获取支持格式失败：" + e.getMessage());
        }
    }

    /**
     * 下载转换后的文件
     * 
     * @param fileId 文件ID
     * @param response HTTP响应对象
     */
    @ApiOperation("下载转换后的文件")
    @GetMapping("/download/{fileId}")
    public void downloadConvertedFile(
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
     * 获取转换进度
     * 
     * @param taskId 转换任务ID
     * @return 转换进度信息
     */
    @ApiOperation("获取转换进度")
    @GetMapping("/progress/{taskId}")
    public Result<java.util.Map<String, Object>> getConversionProgress(
            @ApiParam("任务ID") @PathVariable String taskId) {
        try {
            java.util.Map<String, Object> progress = fileConversionService.getConversionProgress(taskId);
            return Result.success("获取成功", progress);
        } catch (Exception e) {
            return Result.error("获取转换进度失败：" + e.getMessage());
        }
    }

    /**
     * 验证目标格式是否有效
     */
    private boolean isValidTargetFormat(String format) {
        return java.util.Arrays.asList("pdf", "docx", "doc", "ofd").contains(format.toLowerCase());
    }
}
