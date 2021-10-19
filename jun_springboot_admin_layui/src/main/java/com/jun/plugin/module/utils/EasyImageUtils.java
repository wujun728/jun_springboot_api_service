package com.jun.plugin.module.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author laker
 * 图片处理类
 */
public class EasyImageUtils {


    /**
     * 按照宽高尺寸压缩图片，
     *
     * @param sourceUrl 图片url
     * @param out       输出的流 预览流
     * @param width
     * @param height
     * @throws IOException
     */
    public static void compressBysize(String sourceUrl, OutputStream out, int width, int height) throws IOException {
        Thumbnails
                .of(new URL(sourceUrl))
                .width(width)
                .height(height)
                .toOutputStream(out);
    }

    public static void scale(String source, String out, double scale) throws IOException {
        Thumbnails
                .of(source)
                .scale(scale)
                .size(100, 100)
                .toFile(out);
    }

}
