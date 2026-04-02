package com.gitee.freakchicken.dbapi.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PluginConf {

    private static Properties p = new Properties();

    static {

        try (InputStream in = PluginConf.class.getClassLoader().getResourceAsStream("plugin.properties")) {
            if (in != null) {
                p.load(in);
            }
        } catch (IOException e) {
            // plugin.properties loading failed, properties will remain empty
        }

    }

    public static String getKey(String key) {
        return p.getProperty(key);
    }

}
