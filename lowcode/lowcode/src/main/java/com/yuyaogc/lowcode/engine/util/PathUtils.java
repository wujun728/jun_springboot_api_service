package com.yuyaogc.lowcode.engine.util;

public class PathUtils {
    /**
     * 路径处理
     * 
     * @param paths
     * @return
     */
    public static String combine(String... paths) {
        String path = "";
        for (String p : paths) {
            String f = p.replaceAll("\\\\", "/");
            if (path.length() == 0) {
                path = f;
            } else {
                boolean end = path.endsWith("/");
                boolean start = f.startsWith("/");
                if (end) {
                    if (!start) {
                        path += f;
                    } else {
                        path += f.substring(1);
                    }
                } else {
                    if (start) {
                        path += f;
                    } else {
                        path += "/" + f;
                    }
                }
            }
        }
        return path;
    }
}