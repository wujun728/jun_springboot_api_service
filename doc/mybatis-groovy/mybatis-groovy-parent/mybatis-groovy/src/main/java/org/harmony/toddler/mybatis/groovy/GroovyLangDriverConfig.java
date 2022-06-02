package org.harmony.toddler.mybatis.groovy;

import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.harmony.toddler.mybatis.util.PathUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.harmony.toddler.mybatis.util.PathUtil.newUrl;

/**
 * Groovy Driver Config
 */
@Slf4j
public class GroovyLangDriverConfig {

    private static String[] roots = new String[]{"groovy/"};
    /**
     * Groovy Script Base Class
     */
    private static String baseClass = "org.harmony.toddler.mybatis.groovy.GroovySqlMethodExt";

    /**
     * Get Roots Of Groovy Script
     *
     * @return
     */
    public URL[] getRoots() {
        final String[] rs = roots;
        final List<URL> list = new ArrayList<>(rs.length);
        for (String root : rs) {
            if (StringUtils.isEmpty(root)) {
                log.warn("root is empty");
                continue;
            }
            addUrl(list, root);
        }
        return list.toArray(new URL[0]);
    }

    private static void addUrl(List<URL> list, String root) {
        // url
        if (root.contains("://")) {
            list.add(appendSlash(newUrl(root)));
            return;
        }
        // absolute path root
        Path path = Paths.get(root);
        if (PathUtil.isAbsolute(path)) {
            log.debug("path[{}]", path);
            addUrl(list, PathUtil.path2Url(path));
            return;
        }
        // relative path root
        addUrl(list, toURL(root));
    }

    private static void addUrl(List<URL> list, URL url) {
        if (Objects.nonNull(url)) {
            list.add(appendSlash(url));
        }
    }

    private static URL appendSlash(URL url) {
        if (url.toString().endsWith("/")) {
            return url;
        }
        return newUrl(url + "/");
    }

    // search path first, then search in the classpath if not found the path,
    // because it has to restart to reload the changes in classpath scripts
    private static URL toURL(String root) {
        URL url = findPath(root);
        if (Objects.isNull(url)) {
            url = getResource(root);
        }
        try {
            if (url != null) {
                url.openConnection();
                return url;
            }
        } catch (IOException ignored) {
        }
        return null;
    }

    private static URL getResource(String name) {
        return GroovyLangDriverConfig.class.getClassLoader().getResource(name);
    }


    private static URL findPath(String root) {
        Path base = PathUtil.basePath();
        String[] targets = StringUtils.split(root, "/\\");
        Optional<Path> rootPath = PathUtil.findPath(base, targets);
        return rootPath.map(PathUtil::path2Url)
                .orElse(null);
    }

    public String getGroovyScriptBaseClassName() {
        try {
            Class<?> aClass = GroovyLangDriverConfig.class.getClassLoader().loadClass(baseClass);
            if (Script.class.isAssignableFrom(aClass)) {
                return baseClass;
            }
        } catch (ClassNotFoundException e) {
            log.warn(e.getMessage(), e);
        }
        return GroovySqlMethodExt.class.getName();
    }

    public static void setBaseClass(String baseClass) {
        GroovyLangDriverConfig.baseClass = baseClass;
    }

    public static void setRoots(String[] roots) {
        if (ArrayUtils.isEmpty(roots)) {
            throw new RuntimeException("roots required not null");
        }
        GroovyLangDriverConfig.roots = roots;
    }

    public static GroovyLangDriverConfig newInstance() {
        return new GroovyLangDriverConfig();
    }
}
