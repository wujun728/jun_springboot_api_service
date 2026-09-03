package com.gitee.sqlrest.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.URLUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取项目的版本号工具类
 */
@Slf4j
@UtilityClass
public final class PomVersionUtils {

  private static final String PREFIX = "version=";

  public static String getProjectVersion() {
    Class<?> clazz = PomVersionUtils.class;
    String resourcePath = clazz.getResource("").toString();
    if (resourcePath.startsWith("file:")) {
      return getProjectVersionFromFile(resourcePath);
    } else if (resourcePath.startsWith("jar:")) {
      return getProjectVersionFromJar(clazz);
    } else {
      return null;
    }
  }

  private static String getProjectVersionFromFile(String classPath) {
    String basePath = classPath.substring(0, classPath.indexOf("/classes/"));
    basePath = URLUtil.decode(FileUtil.normalize(basePath));
    File propertiesFile = Paths.get(basePath, "maven-archiver", "pom.properties").toFile();
    if (propertiesFile.exists()) {
      return extractPomVersion(FileUtil.getInputStream(propertiesFile));
    }
    return null;
  }

  private static String getProjectVersionFromJar(Class<?> clazz) {
    ProtectionDomain protectionDomain = clazz.getProtectionDomain();
    CodeSource codeSource = protectionDomain.getCodeSource();
    try (JarFile jarFile = new JarFile(codeSource.getLocation().getPath())) {
      Enumeration<JarEntry> entries = jarFile.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        if (entry.getName().startsWith("META-INF/maven/") && entry.getName().endsWith("/pom.properties")) {
          return extractPomVersion(jarFile.getInputStream(entry));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String extractPomVersion(InputStream inputStream) {
    String line;
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
      while ((line = bufferedReader.readLine()) != null) {
        if (line.startsWith(PREFIX)) {
          return line.substring(PREFIX.length());
        }
      }
    } catch (IOException e) {
    }
    return null;
  }

  public static void main(String[] args) {
    System.out.println(getProjectVersion());
  }
}
