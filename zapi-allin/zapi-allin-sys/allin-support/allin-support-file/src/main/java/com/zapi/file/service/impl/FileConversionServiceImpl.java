package com.zapi.file.service.impl;

import com.zapi.file.entity.FileInfo;
import com.zapi.file.service.FileConversionService;
import com.zapi.file.service.FileService;
import com.zapi.file.util.FileConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
// import org.ofdrw.converter.ConvertHelper; // OFD功能已禁用
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 文件转换服务实现类
 * 使用Apache POI和其他开源库实现文件格式转换
 */
@Slf4j
@Service
public class FileConversionServiceImpl implements FileConversionService {

    @Autowired
    private FileService fileService;

    // 转换进度跟踪
    private final Map<String, Map<String, Object>> conversionProgress = new ConcurrentHashMap<>();

    @Override
    public FileInfo convertWordToPdf(MultipartFile file, String outputFileName, 
                                    Long uploadUserId, String uploadUsername) throws Exception {
        log.info("开始Word转PDF转换，原文件：{}", file.getOriginalFilename());
        
        String taskId = generateTaskId();
        updateProgress(taskId, 0, "开始转换Word文档");

        try {
            // 生成输出文件名
            String finalOutputName = generateOutputFileName(file.getOriginalFilename(), outputFileName, "pdf");
            
            updateProgress(taskId, 20, "正在读取Word文档");

            // 根据文件扩展名选择转换方式
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            
            if (file.getOriginalFilename().toLowerCase().endsWith(".docx")) {
                // DOCX转PDF
                convertDocxToPdf(file.getInputStream(), pdfOutputStream);
            } else {
                // DOC转PDF (先转HTML再转PDF)
                convertDocToPdf(file.getInputStream(), pdfOutputStream);
            }

            updateProgress(taskId, 80, "正在保存PDF文件");

            // 将转换后的PDF保存到文件服务器
            ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());
            FileInfo convertedFile = fileService.uploadFile(
                pdfInputStream, finalOutputName, "conversion/pdf",
                "Word转PDF转换结果", uploadUserId, uploadUsername);
            
            log.info("PDF文件已保存，ID: {}, 路径: {}, URL: {}", 
                convertedFile.getId(), convertedFile.getFilePath(), convertedFile.getFileUrl());

            updateProgress(taskId, 100, "转换完成");
            log.info("Word转PDF转换完成，输出文件：{}", finalOutputName);

            return convertedFile;
        } catch (Exception e) {
            updateProgress(taskId, -1, "转换失败：" + e.getMessage());
            log.error("Word转PDF转换失败", e);
            throw e;
        }
    }

    // OFD转PDF功能已移除
    // 如需使用，请使用专门的OFD处理服务
    /*
    @Override
    public FileInfo convertOfdToPdf(MultipartFile file, String outputFileName, 
                                   Long uploadUserId, String uploadUsername) throws Exception {
        throw new UnsupportedOperationException("OFD转PDF功能已禁用");
    }
    */

    @Override
    public FileInfo convertPdfToWord(MultipartFile file, String outputFileName, 
                                    Long uploadUserId, String uploadUsername) throws Exception {
        log.info("开始PDF转Word转换，原文件：{}", file.getOriginalFilename());
        
        String taskId = generateTaskId();
        updateProgress(taskId, 0, "开始转换PDF文档");

        try {
            String finalOutputName = generateOutputFileName(file.getOriginalFilename(), outputFileName, "docx");
            
            updateProgress(taskId, 30, "正在处理PDF文档");

            // PDF转Word实现（需要使用PDF处理库，如PDFBox + Apache POI）
            ByteArrayOutputStream wordOutputStream = convertPdfToWordInternal(file.getInputStream());

            updateProgress(taskId, 80, "正在保存Word文件");

            ByteArrayInputStream wordInputStream = new ByteArrayInputStream(wordOutputStream.toByteArray());
            FileInfo convertedFile = fileService.uploadFile(
                wordInputStream, finalOutputName, "conversion/docx", 
                "PDF转Word转换结果", uploadUserId, uploadUsername);
            
            log.info("Word文件已保存，ID: {}, 路径: {}, URL: {}", 
                convertedFile.getId(), convertedFile.getFilePath(), convertedFile.getFileUrl());

            updateProgress(taskId, 100, "转换完成");
            log.info("PDF转Word转换完成，输出文件：{}", finalOutputName);

            return convertedFile;
        } catch (Exception e) {
            updateProgress(taskId, -1, "转换失败：" + e.getMessage());
            log.error("PDF转Word转换失败", e);
            throw e;
        }
    }

    @Override
    public FileInfo convertFile(FileInfo sourceFile, String targetFormat, String outputFileName, 
                               Long uploadUserId, String uploadUsername) throws Exception {
        log.info("开始文件转换，源文件：{}，目标格式：{}", sourceFile.getFileName(), targetFormat);

        // 根据源文件格式和目标格式选择转换方法
        String sourceFormat = getFileExtension(sourceFile.getFileName()).toLowerCase();
        String targetFormatLower = targetFormat.toLowerCase();

        // 模拟MultipartFile
        // 这里需要根据实际文件服务实现来获取文件流
        // 暂时抛出异常，提示需要实现
        throw new UnsupportedOperationException("通过文件ID转换功能需要配合具体的文件存储实现");
    }

    @Override
    public List<FileInfo> batchConvert(MultipartFile[] files, String targetFormat, 
                                      Long uploadUserId, String uploadUsername) throws Exception {
        log.info("开始批量文件转换，文件数量：{}，目标格式：{}", files.length, targetFormat);
        
        List<FileInfo> convertedFiles = new ArrayList<>();
        String batchTaskId = generateTaskId();
        
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            updateProgress(batchTaskId, (i * 100) / files.length, 
                String.format("正在转换第%d个文件：%s", i + 1, file.getOriginalFilename()));
            
            try {
                FileInfo convertedFile = null;
                String sourceFormat = getFileExtension(file.getOriginalFilename()).toLowerCase();
                
                if (targetFormat.equalsIgnoreCase("pdf")) {
                    if (sourceFormat.equals("docx") || sourceFormat.equals("doc")) {
                        convertedFile = convertWordToPdf(file, null, uploadUserId, uploadUsername);
                    }
                    // OFD转PDF功能已禁用
                    // else if (sourceFormat.equals("ofd")) {
                    //     convertedFile = convertOfdToPdf(file, null, uploadUserId, uploadUsername);
                    // }
                } else if (targetFormat.equalsIgnoreCase("docx") && sourceFormat.equals("pdf")) {
                    convertedFile = convertPdfToWord(file, null, uploadUserId, uploadUsername);
                }
                
                if (convertedFile != null) {
                    convertedFiles.add(convertedFile);
                }
            } catch (Exception e) {
                log.error("批量转换中文件{}转换失败", file.getOriginalFilename(), e);
                // 继续处理下一个文件
            }
        }
        
        updateProgress(batchTaskId, 100, "批量转换完成");
        return convertedFiles;
    }

    @Override
    public Map<String, List<String>> getSupportedFormats() {
        Map<String, List<String>> formats = new HashMap<>();
        
        // 支持转换为PDF的格式
        formats.put("to_pdf", Arrays.asList("doc", "docx", "ofd"));
        
        // 支持转换为Word的格式
        formats.put("to_word", Arrays.asList("pdf"));
        
        // 支持转换为OFD的格式（如果实现了的话）
        formats.put("to_ofd", Arrays.asList("pdf", "doc", "docx"));
        
        return formats;
    }

    @Override
    public Map<String, Object> getConversionProgress(String taskId) {
        return conversionProgress.getOrDefault(taskId, createDefaultProgress());
    }

    @Override
    public boolean isSupportedConversion(String fileName, String targetFormat) {
        String sourceFormat = getFileExtension(fileName).toLowerCase();
        String targetFormatLower = targetFormat.toLowerCase();
        
        Map<String, List<String>> supportedFormats = getSupportedFormats();
        
        if (targetFormatLower.equals("pdf")) {
            return supportedFormats.get("to_pdf").contains(sourceFormat);
        } else if (targetFormatLower.equals("docx") || targetFormatLower.equals("doc")) {
            return supportedFormats.get("to_word").contains(sourceFormat);
        } else if (targetFormatLower.equals("ofd")) {
            return supportedFormats.get("to_ofd").contains(sourceFormat);
        }
        
        return false;
    }

    /**
     * DOCX转PDF的具体实现
     * 优先级：Aspose.Words > LibreOffice > POI库
     */
    private void convertDocxToPdf(InputStream inputStream, OutputStream outputStream) throws Exception {
        log.info("==================== Word转PDF 引擎选择 ====================");
        
        // 方案1：优先使用 Aspose.Words（商业版，效果最好）
        boolean asposeAvailable = FileConversionUtils.isAsposeWordsAvailable();
        log.info("Aspose.Words 可用状态: {}", asposeAvailable);
        
        if (asposeAvailable) {
            log.info("✅ 使用 Aspose.Words 引擎（商业版，完美支持中文）");
            boolean success = FileConversionUtils.wordToPdf(inputStream, outputStream);
            if (success) {
                log.info("====================================================");
                return;
            } else {
                log.warn("Aspose.Words 转换失败，尝试降级方案");
            }
        }
        
        // 方案2：使用 LibreOffice（开源免费，效果好）
        boolean libreOfficeAvailable = isLibreOfficeAvailable();
        log.info("LibreOffice 可用状态: {}", libreOfficeAvailable);
        
        if (libreOfficeAvailable) {
            log.info("✅ 使用 LibreOffice 引擎（开源免费，支持中文）");
            convertDocxToPdfWithLibreOffice(inputStream, outputStream);
            log.info("====================================================");
            return;
        }
        
        // 方案3：降级使用POI库（开源免费，效果一般）
        log.warn("⚠️ Aspose.Words 和 LibreOffice 均不可用");
        log.warn("⚠️ 降级使用 POI 库转换（中文支持有限）");
        log.warn("⚠️ 建议:");
        log.warn("   1. 安装 Aspose.Words jar包（商业版，效果最好）");
        log.warn("   2. 安装 LibreOffice: brew install --cask libreoffice");
        convertDocxToPdfWithPOI(inputStream, outputStream);
        log.info("====================================================");
    }
    
    /**
     * 使用LibreOffice命令行工具进行转换
     */
    private void convertDocxToPdfWithLibreOffice(InputStream inputStream, OutputStream outputStream) throws Exception {
        File tempInputFile = null;
        File tempOutputDir = null;
        
        try {
            // 创建临时输入文件
            tempInputFile = createTempFile(inputStream, ".docx");
            
            // 创建临时输出目录
            tempOutputDir = Files.createTempDirectory("libreoffice_convert_").toFile();
            
            // 构建LibreOffice命令
            // macOS: /Applications/LibreOffice.app/Contents/MacOS/soffice
            // Linux: /usr/bin/soffice 或 /usr/bin/libreoffice
            // Windows: C:\Program Files\LibreOffice\program\soffice.exe
            String libreOfficePath = detectLibreOfficePath();
            
            ProcessBuilder processBuilder = new ProcessBuilder(
                libreOfficePath,
                "--headless",
                "--convert-to", "pdf",
                "--outdir", tempOutputDir.getAbsolutePath(),
                tempInputFile.getAbsolutePath()
            );
            
            log.info("执行LibreOffice转换命令: {}", String.join(" ", processBuilder.command()));
            
            Process process = processBuilder.start();
            
            // 等待转换完成（最多30秒）
            boolean finished = process.waitFor(30, TimeUnit.SECONDS);
            
            if (!finished) {
                process.destroyForcibly();
                throw new Exception("LibreOffice转换超时");
            }
            
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                // 读取错误输出 (Java 8 兼容方式)
                BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
                StringBuilder errorBuilder = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorBuilder.append(line).append("\n");
                }
                String error = errorBuilder.toString();
                throw new Exception("LibreOffice转换失败，退出码: " + exitCode + ", 错误: " + error);
            }
            
            // 找到生成的PDF文件
            String baseName = tempInputFile.getName().replaceFirst("[.][^.]+$", "");
            File pdfFile = new File(tempOutputDir, baseName + ".pdf");
            
            if (!pdfFile.exists()) {
                throw new Exception("LibreOffice转换后未找到PDF文件");
            }
            
            // 读取PDF文件到输出流
            try (FileInputStream fis = new FileInputStream(pdfFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            
            log.info("LibreOffice转换成功，PDF大小: {} bytes", pdfFile.length());
            
        } finally {
            // 清理临时文件
            if (tempInputFile != null && tempInputFile.exists()) {
                tempInputFile.delete();
            }
            if (tempOutputDir != null && tempOutputDir.exists()) {
                // 删除目录中的所有文件
                File[] files = tempOutputDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        file.delete();
                    }
                }
                tempOutputDir.delete();
            }
        }
    }
    
    /**
     * 使用POI库进行转换（备用方案）
     */
    private void convertDocxToPdfWithPOI(InputStream inputStream, OutputStream outputStream) throws Exception {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            PdfOptions options = PdfOptions.create();
            // 尝试设置字体映射以支持中文
            options.fontEncoding("UTF-8");
            PdfConverter.getInstance().convert(document, outputStream, options);
        }
    }
    
    /**
     * 检测LibreOffice是否可用
     */
    private boolean isLibreOfficeAvailable() {
        try {
            String path = detectLibreOfficePath();
            boolean exists = path != null && new File(path).exists();
            if (exists) {
                log.info("✅ LibreOffice检测成功: {}", path);
            } else {
                log.warn("❌ LibreOffice路径不存在: {}", path);
            }
            return exists;
        } catch (Exception e) {
            log.warn("❌ LibreOffice检测失败: {}", e.getMessage());
            log.warn("请确保已安装LibreOffice，安装方法:");
            log.warn("  macOS:   brew install --cask libreoffice");
            log.warn("  Ubuntu:  sudo apt install libreoffice");
            log.warn("  Windows: 下载 https://www.libreoffice.org/download/");
            return false;
        }
    }
    
    /**
     * 检测LibreOffice安装路径
     */
    private String detectLibreOfficePath() throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        
        List<String> possiblePaths = new ArrayList<>();
        
        if (os.contains("mac")) {
            possiblePaths.add("/Applications/LibreOffice.app/Contents/MacOS/soffice");
            possiblePaths.add("/usr/local/bin/soffice");
        } else if (os.contains("linux")) {
            possiblePaths.add("/usr/bin/soffice");
            possiblePaths.add("/usr/bin/libreoffice");
            possiblePaths.add("/snap/bin/libreoffice");
        } else if (os.contains("windows")) {
            possiblePaths.add("C:\\Program Files\\LibreOffice\\program\\soffice.exe");
            possiblePaths.add("C:\\Program Files (x86)\\LibreOffice\\program\\soffice.exe");
        }
        
        // 检查各个可能的路径
        for (String path : possiblePaths) {
            if (new File(path).exists()) {
                log.info("找到LibreOffice: {}", path);
                return path;
            }
        }
        
        // 尝试从PATH环境变量中查找
        try {
            ProcessBuilder pb = new ProcessBuilder("which", "soffice");
            Process process = pb.start();
            if (process.waitFor(2, TimeUnit.SECONDS) && process.exitValue() == 0) {
                // 读取输出 (Java 8 兼容方式)
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
                String path = reader.readLine();
                if (path != null && !path.trim().isEmpty()) {
                    log.info("从PATH找到LibreOffice: {}", path);
                    return path.trim();
                }
            }
        } catch (Exception e) {
            log.debug("无法从PATH查找LibreOffice", e);
        }
        
        throw new Exception("未找到LibreOffice安装，请安装LibreOffice以获得最佳转换效果");
    }

    /**
     * DOC转PDF的具体实现
     * 由于DOC格式较老，先转为HTML再转PDF
     */
    private void convertDocToPdf(InputStream inputStream, OutputStream outputStream) throws Exception {
        // 这是一个简化实现，实际生产环境建议使用专业的转换库
        // 如：LibreOffice、OpenOffice、或商业转换服务
        
        try (HWPFDocument document = new HWPFDocument(inputStream)) {
            WordToHtmlConverter converter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            
            // 设置图片处理器
            converter.setPicturesManager(new PicturesManager() {
                @Override
                public String savePicture(byte[] content, PictureType pictureType,
                                        String suggestedName, float widthInches, float heightInches) {
                    // 简单返回base64编码的图片
                    String extension = pictureType.getExtension();
                    return "data:image/" + extension + ";base64," + 
                           Base64.getEncoder().encodeToString(content);
                }
            });
            
            converter.processDocument(document);
            Document htmlDocument = converter.getDocument();
            
            // 将HTML转换为字符串
            ByteArrayOutputStream htmlOutput = new ByteArrayOutputStream();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(htmlDocument), new StreamResult(htmlOutput));
            
            // 这里需要进一步实现HTML到PDF的转换
            // 可以使用iText、wkhtmltopdf等工具
            throw new UnsupportedOperationException("DOC转PDF功能需要额外的PDF生成库支持");
        }
    }

    // OFD转PDF功能已移除
    /*
    private ByteArrayOutputStream convertOfdToPdfInternal(InputStream inputStream) throws Exception {
        throw new UnsupportedOperationException("OFD转PDF功能已禁用");
    }
    */

    /**
     * PDF转Word的具体实现
     * 优先使用 Aspose.Words，效果最好
     */
    private ByteArrayOutputStream convertPdfToWordInternal(InputStream inputStream) throws Exception {
        log.info("==================== PDF转Word 引擎选择 ====================");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // 方案1：优先使用 Aspose.Words（完美支持中文和排版）
        boolean asposeAvailable = FileConversionUtils.isAsposeWordsAvailable();
        log.info("Aspose.Words 可用状态: {}", asposeAvailable);
        
        if (asposeAvailable) {
            log.info("✅ 使用 Aspose.Words 引擎转换PDF到Word");
            boolean success = FileConversionUtils.pdfToWord(inputStream, outputStream);
            if (success) {
                log.info("====================================================");
                return outputStream;
            } else {
                log.warn("Aspose.Words 转换失败，尝试降级方案");
            }
        }
        
        // 方案2：降级使用 PDFBox + POI（仅提取文本，效果较差）
        log.warn("⚠️ Aspose.Words 不可用，降级使用 PDFBox 提取文本");
        log.warn("⚠️ 此方案仅能提取纯文本，会丢失格式、图片、表格等");
        log.warn("⚠️ 建议安装 Aspose.Words 以获得最佳转换效果");
        
        try (PDDocument document = PDDocument.load(inputStream)) {
            // 创建新的Word文档
            XWPFDocument wordDocument = new XWPFDocument();
            
            // 提取PDF文本内容
            PDFTextStripper textStripper = new PDFTextStripper();
            String pdfText = textStripper.getText(document);
            
            // 将文本写入Word文档
            String[] paragraphs = pdfText.split("\n");
            for (String paragraphText : paragraphs) {
                if (paragraphText.trim().length() > 0) {
                    XWPFParagraph paragraph = wordDocument.createParagraph();
                    XWPFRun run = paragraph.createRun();
                    run.setText(paragraphText.trim());
                }
            }
            
            // 写入输出流
            wordDocument.write(outputStream);
            wordDocument.close();
            
            log.info("PDFBox 文本提取完成");
        } catch (Exception e) {
            log.error("PDF转Word转换失败", e);
            throw new Exception("PDF转Word转换失败：" + e.getMessage());
        }
        
        log.info("====================================================");
        return outputStream;
    }

    /**
     * 创建临时文件
     */
    private File createTempFile(InputStream inputStream, String suffix) throws IOException {
        File tempFile = File.createTempFile("conversion_", suffix);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }

    /**
     * 生成任务ID
     */
    private String generateTaskId() {
        return "CONV_" + System.currentTimeMillis() + "_" + 
               UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 更新转换进度
     */
    private void updateProgress(String taskId, int progress, String message) {
        Map<String, Object> progressInfo = new HashMap<>();
        progressInfo.put("taskId", taskId);
        progressInfo.put("progress", progress);
        progressInfo.put("message", message);
        progressInfo.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        progressInfo.put("status", progress < 0 ? "error" : (progress >= 100 ? "completed" : "processing"));
        
        conversionProgress.put(taskId, progressInfo);
        log.info("转换进度更新 - 任务ID：{}，进度：{}%，状态：{}", taskId, progress, message);
    }

    /**
     * 创建默认进度信息
     */
    private Map<String, Object> createDefaultProgress() {
        Map<String, Object> progress = new HashMap<>();
        progress.put("taskId", "");
        progress.put("progress", 0);
        progress.put("message", "任务不存在");
        progress.put("status", "not_found");
        return progress;
    }

    /**
     * 生成输出文件名
     */
    private String generateOutputFileName(String originalFileName, String customName, String targetExtension) {
        if (customName != null && !customName.trim().isEmpty()) {
            return customName.endsWith("." + targetExtension) ? 
                   customName : customName + "." + targetExtension;
        }
        
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        return baseName + "_converted." + targetExtension;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
