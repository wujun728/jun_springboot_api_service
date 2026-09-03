package com.zapi.util.file;

import com.zapi.util.Base64Utils;
import com.zapi.util.UuidUtils;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtils {

	/**
	 * 获取文件名的后缀
	 * @param fileName
	 * @return
	 */
	public static String getFileNameSuffix(String fileName) {
		int lastIndexOf = fileName.lastIndexOf(".");
		if (lastIndexOf > -1) {
			String subString = fileName.substring(lastIndexOf + 1);
			return subString;
		} else {
			return "";
		}
	}

	/**
	 * 重新定义文件名
	 * @param fileName
	 * @return
	 */
	public static String renameFileName(String fileName) {
		String fileSuffix = getFileNameSuffix(fileName);
		if (ObjectUtils.isEmpty(fileSuffix)) {
			return UuidUtils.uuid();
		} else {
			return UuidUtils.uuid() + "." + fileSuffix;
		}
	}
	
	
	/***
	 * 传入文件转换成base
	 * 返回字符串
	 * @param in
	 * @return
	 */
	public static String base64InputStreamToString(InputStream in) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = in.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Base64Utils.encode(data);
    }
	
	/* 
	 *将InputStream写入本地文件
     * @param destination 写入本地目录
     * @param input    输入流
     * @throws IOException
     */
    public static void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        input.close();
    }


    /**
     * 将文件转换成byte数组
     * @param tradeFile
     * @return
     */
    public static byte[] file2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }
    
    /***
	 * 传入文件转换成子玠数组
	 * 返回字节数组
	 * @param in
	 * @return
	 */
	public static byte[] inputStreamToByte(InputStream in) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = in.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
    
    
    /**
     * 功能描述: byte数组转 InputStream
     *
     * @param bytes byte数组
     * @return java.io.InputStream
     */
    public static InputStream byte2InputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }
 
 
    /**
     * @param inputStream 输入流
     * @return byte[] 数组
     */
    public static byte[] inputStream2byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, 100)) > 0) {
            byteArrayOutputStream.write(buff, 0, rc);
        }
        return byteArrayOutputStream.toByteArray();
    }
 
 
    public static void main(String[] args) {
        String str = "你好呀";
        byte[] bytes = str.getBytes();
        InputStream inputStream = byte2InputStream(bytes);
        try {
            byte[] bytes1 = inputStream2byte(inputStream);
            String string = new String(bytes1, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
