package com.yuyaogc.lowcode.engine.loader;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by bzGhost on 20230322.
 */
public class AppClassLoader extends ClassLoader implements Closeable {
    public final static String JAR = ".jar";
    public final static String CLASS = ".class";
    public JarFile jarFile;
    public ClassLoader parent;
    public final static String TMP_DIR = "system.io.tmpdir";
    private Map<String, Class> classCache = new ConcurrentHashMap<>();
    private Map<String, File> fileCache = new HashMap<>();
    private Map<String, JarFile> jarFileCache = new HashMap<>();

    public AppClassLoader(JarFile jarFile) {
        super(Thread.currentThread().getContextClassLoader());
        this.parent = Thread.currentThread().getContextClassLoader();
        this.jarFile = jarFile;
    }


    public AppClassLoader(JarFile jarFile, ClassLoader parent) {
        super(parent);
        this.parent = parent;
        this.jarFile = jarFile;
        this.fileCache = new HashMap<>();
        this.jarFileCache = new HashMap<>();
        this.classCache = new ConcurrentHashMap<>(255);
    }

    public String classNameToJarEntry(String name) {
        String s = name.replaceAll("\\.", "\\/");
        StringBuilder stringBuilder = new StringBuilder(s);
        stringBuilder.append(CLASS);
        return stringBuilder.toString();

    }



    @Override
    public InputStream getResourceAsStream(String name) {
        InputStream is = null;
        try {
            if (null != jarFile) {
                JarEntry entry = jarFile.getJarEntry(name);
                if (entry != null) {
                    is = jarFile.getInputStream(entry);
                }
                if (is == null) {
                    is = super.getResourceAsStream(name);
                }
            }
        } catch (IOException e) {
            // logger.error(e.getMessage());
        }
        return is;
    }

    @Override
    protected Class<?> findClass(String name) {
        Class c = null;
        if (null != jarFile) {
            try {
                String jarEntryName = classNameToJarEntry(name);
                InputStream inputStream = null;
                JarEntry entry = null;

                Enumeration enumeration = jarFile.entries();
                while (enumeration.hasMoreElements()) {
                    JarEntry bootInfo = (JarEntry) enumeration.nextElement();

                    if (bootInfo.getName().endsWith(jarEntryName)) {
                        entry = bootInfo;
                        inputStream = jarFile.getInputStream(bootInfo);
                        break;
                    }

                    if (bootInfo.getName().endsWith(JAR)) {
                        File file = fileCache.get(bootInfo.getName());
                        if (Objects.isNull(file)) {
                            file = createFile(jarFile, bootInfo, bootInfo.getName());
                            fileCache.put(bootInfo.getName(), file);
                        }

                        JarFile jarFile = jarFileCache.get(bootInfo.getName());
                        if (Objects.isNull(jarFile)) {
                            jarFile = new JarFile(file);
                            jarFileCache.put(bootInfo.getName(), jarFile);
                        }

                        Enumeration enumerationJar = jarFile.entries();
                        while (enumerationJar.hasMoreElements()) {
                            entry = (JarEntry) enumerationJar.nextElement();
                            if (!entry.getName().endsWith(CLASS)) {
                                continue;
                            }
                            if (entry.getName().endsWith(jarEntryName)) {
                                // 加载流
                                inputStream = jarFile.getInputStream(entry);
                                break;
                            }
                        }
                    }
                }

                if (null != entry) {
                    if (null != inputStream) {
                        if (!classCache.containsKey(name)) {
                            c = byte2Class(name, inputStream);
                            this.classCache.put(name, c);
                        }
                    }
                } else {
                    if (parent != null) {
                        return parent.loadClass(name);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Class not found,class: " + name + " ,Please check maven dependencies.");
            }
        }

        return c;
    }

    private void copy(JarFile jarFile, JarEntry jarEntry, File file) throws IOException {
        try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
            try (OutputStream outputStream = new FileOutputStream(file)) {
                if (inputStream == null) {
                    throw new IllegalArgumentException("参数inputStream不能为空");
                }
                if (outputStream == null) {
                    throw new IllegalArgumentException("参数inputStream不能为空");
                }
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                byte[] arr = new byte[1024];
                int len;
                while ((len = bufferedInputStream.read(arr)) != -1) {
                    bufferedOutputStream.write(arr, 0, len);
                }
                bufferedOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void deleteIfExists(Path dir) throws IOException {
        try {
            Files.deleteIfExists(dir);
        } catch (DirectoryNotEmptyException e) {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return super.postVisitDirectory(dir, exc);
                }
            });
        }
    }

    private boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    private String substringAfterLast(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        } else if (isEmpty(separator)) {
            return "";
        } else {
            int pos = str.lastIndexOf(separator);
            return pos != -1 && pos != str.length() - separator.length() ? str.substring(pos + separator.length()) : "";
        }
    }

    private File createFile(JarFile jarFile, JarEntry jarEntry, String name) throws IOException {
        try {
            deleteIfExists(Paths.get(TMP_DIR));
        } catch (Exception e) {
        }

        File tempFolder = new File(TMP_DIR);
        File loaderFolder = new File(tempFolder, "app-loader-" + UUID.randomUUID());
        loaderFolder.mkdirs();
        loaderFolder.deleteOnExit();
        //logger.warn("==============删除Jar {}==============", loaderFolder.getAbsolutePath());
        File jar = new File(loaderFolder, substringAfterLast(name, "/"));
        jar.createNewFile();

        copy(jarFile, jarEntry, jar);
        return jar;
    }

    private Class byte2Class(String name, InputStream is) throws IOException {
        Class c = null;
        int availableLen = is.available();
        int len = 0;
        byte[] bt1 = new byte[availableLen];
        while (len < availableLen) {
            len += is.read(bt1, len, availableLen - len);
        }
        try {
            c = defineClass(name, bt1, 0, bt1.length);
        } catch (ClassFormatError e) {
            e.printStackTrace();
        }

        is.close();
        return c;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (classCache.containsKey(name)) {
            return classCache.get(name);
        }
        return super.loadClass(name);
    }

    @Override
    public String toString() {
        return "JarClassLoader{" +
                "jarFile=" + jarFile.getName() +
                ", parent=" + parent +
                '}';
    }

    @Override
    public void close() throws IOException {
        if (jarFile != null) {
            try {
                jarFile.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
