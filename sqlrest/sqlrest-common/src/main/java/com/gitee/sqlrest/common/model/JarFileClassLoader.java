package com.gitee.sqlrest.common.model;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class JarFileClassLoader extends URLClassLoader {

  public JarFileClassLoader(String path, ClassLoader parent) {
    this(new String[]{path}, parent);
  }

  public JarFileClassLoader(String[] paths, ClassLoader parent) {
    super(getURLs(paths), parent);
  }

  private static URL[] getURLs(String[] paths) {
    if (null == paths || 0 == paths.length) {
      throw new IllegalArgumentException("jar file path is empty.");
    }
    List<String> dirs = new ArrayList<>();
    for (String path : paths) {
      dirs.add(path);
      collectDirs(path, dirs);
    }
    List<URL> urls = new ArrayList<>();
    for (String path : dirs) {
      urls.addAll(doGetURLs(path));
    }
    if (urls.isEmpty()) {
      throw new RuntimeException("No jar file found from path :"
          + Arrays.stream(paths).collect(Collectors.joining(",")) + "!");
    }
    return urls.toArray(new URL[0]);
  }

  private static void collectDirs(String path, List<String> collector) {
    if (StringUtils.isEmpty(path)) {
      return;
    }
    File current = new File(path);
    if (!current.exists() || !current.isDirectory()) {
      return;
    }
    for (File child : current.listFiles()) {
      if (!child.isDirectory()) {
        continue;
      }
      collector.add(child.getAbsolutePath());
      collectDirs(child.getAbsolutePath(), collector);
    }
  }

  private static List<URL> doGetURLs(final String path) {
    File jarPath = new File(path);
    if (!jarPath.exists() || !jarPath.isDirectory()) {
      return Collections.emptyList();
    }
    FileFilter jarFilter = name -> name.getName().endsWith(".jar");
    File[] allJars = new File(path).listFiles(jarFilter);
    List<URL> jarURLs = new ArrayList<>(allJars.length);
    for (int i = 0; i < allJars.length; i++) {
      try {
        jarURLs.add(allJars[i].toURI().toURL());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return jarURLs;
  }

}
