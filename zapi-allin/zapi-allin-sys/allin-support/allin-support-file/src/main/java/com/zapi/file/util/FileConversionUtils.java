package com.zapi.file.util;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 文件格式转换工具类
 * 使用 Aspose.Words 进行高质量的文档转换
 * 完美支持中文、图片、表格、复杂排版等
 * 
 * @author zapi
 */
@Slf4j
public class FileConversionUtils {

    /**
     * Word转PDF（使用流）
     * 
     * @param wordInputStream Word文档输入流
     * @param pdfOutputStream PDF文档输出流
     * @return 是否转换成功
     */
    public static boolean wordToPdf(InputStream wordInputStream, OutputStream pdfOutputStream) {
        try {
            log.info("开始使用Aspose.Words转换Word到PDF");
            
            // 加载Word文档
            Document document = new Document(wordInputStream);
            
            // 保存为PDF格式
            document.save(pdfOutputStream, SaveFormat.PDF);
            
            log.info("Aspose.Words转换成功");
            return true;
            
        } catch (Exception e) {
            log.error("Aspose.Words转换失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Word转PDF（使用文件路径）
     * 
     * @param wordFilePath Word文件路径
     * @param pdfFilePath PDF文件路径
     * @return 是否转换成功
     */
    public static boolean wordToPdf(String wordFilePath, String pdfFilePath) {
        try {
            log.info("开始使用Aspose.Words转换Word到PDF: {} -> {}", wordFilePath, pdfFilePath);
            
            // 加载Word文档
            Document document = new Document(wordFilePath);
            
            // 保存为PDF
            File outputFile = new File(pdfFilePath);
            // 确保父目录存在
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            
            document.save(outputFile.getAbsolutePath(), SaveFormat.PDF);
            
            log.info("Aspose.Words转换成功，PDF文件: {}", pdfFilePath);
            return true;
            
        } catch (Exception e) {
            log.error("Aspose.Words转换失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Word转PDF（返回字节数组）
     * 
     * @param wordInputStream Word文档输入流
     * @return PDF文档字节数组，失败返回null
     */
    public static byte[] wordToPdfBytes(InputStream wordInputStream) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            if (wordToPdf(wordInputStream, outputStream)) {
                return outputStream.toByteArray();
            }
            
            return null;
            
        } catch (Exception e) {
            log.error("Aspose.Words转换失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 检查Aspose.Words是否可用
     * 
     * @return 是否可用
     */
    public static boolean isAsposeWordsAvailable() {
        try {
            // 尝试创建一个空文档来验证Aspose.Words是否可用
            Document doc = new Document();
            log.info("✅ Aspose.Words 可用");
            return true;
        } catch (Exception e) {
            log.warn("❌ Aspose.Words 不可用: {}", e.getMessage());
            return false;
        }
    }

    /**
     * PDF转Word（使用流）
     * 
     * @param pdfInputStream PDF文档输入流
     * @param wordOutputStream Word文档输出流
     * @return 是否转换成功
     */
    public static boolean pdfToWord(InputStream pdfInputStream, OutputStream wordOutputStream) {
        try {
            log.info("开始使用Aspose.Words转换PDF到Word");
            
            // 加载PDF文档
            Document document = new Document(pdfInputStream);
            
            // 保存为Word格式（DOCX）
            document.save(wordOutputStream, SaveFormat.DOCX);
            
            log.info("Aspose.Words PDF转Word成功");
            return true;
            
        } catch (Exception e) {
            log.error("Aspose.Words PDF转Word失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * PDF转Word（使用文件路径）
     * 
     * @param pdfFilePath PDF文件路径
     * @param wordFilePath Word文件路径
     * @return 是否转换成功
     */
    public static boolean pdfToWord(String pdfFilePath, String wordFilePath) {
        try {
            log.info("开始使用Aspose.Words转换PDF到Word: {} -> {}", pdfFilePath, wordFilePath);
            
            // 加载PDF文档
            Document document = new Document(pdfFilePath);
            
            // 保存为Word
            File outputFile = new File(wordFilePath);
            // 确保父目录存在
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            
            document.save(outputFile.getAbsolutePath(), SaveFormat.DOCX);
            
            log.info("Aspose.Words PDF转Word成功，Word文件: {}", wordFilePath);
            return true;
            
        } catch (Exception e) {
            log.error("Aspose.Words PDF转Word失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * PDF转Word（返回字节数组）
     * 
     * @param pdfInputStream PDF文档输入流
     * @return Word文档字节数组，失败返回null
     */
    public static byte[] pdfToWordBytes(InputStream pdfInputStream) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            if (pdfToWord(pdfInputStream, outputStream)) {
                return outputStream.toByteArray();
            }
            
            return null;
            
        } catch (Exception e) {
            log.error("Aspose.Words PDF转Word失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取Aspose.Words版本信息
     * 
     * @return 版本信息
     */
    public static String getAsposeWordsVersion() {
        try {
            // Aspose.Words 的版本信息
            return "Aspose.Words for Java";
        } catch (Exception e) {
            return "Unknown";
        }
    }
}

