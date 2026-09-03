package com.yuyaogc.lowcode.engine.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class HttpUtils {
    public static void WriteHtml(HttpServletResponse response, String html) {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter pw = response.getWriter()) {
            pw.write(html);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("响应失败", e);
        }
    }
}