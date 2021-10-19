package com.jun.plugin.service;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;

public class EasyImageUtilsTest {

    public static void main(String[] args) throws IOException {
        size("d://1.jpg", "d://laker_200x300.jpg");
    }

    public static void size(String source, String out) throws IOException {
        Thumbnails
                .of(source)
                .size(100, 100)
                .toFile(out);
    }
}
