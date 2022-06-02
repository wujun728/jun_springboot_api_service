package org.harmony.toddler.mybatis.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class PathUtil {
    public static Path basePath() {
        return Paths.get(SystemUtils.USER_DIR);
    }

    public static Optional<Path> findPath(Path base, String... targets) {
        return findPath(base, targets, 100, true).findFirst();
    }

    private static Stream<Path> findPath(Path base, String[] targets, final int maxDepth, final boolean onlyDir, FileVisitOption... options) {
        Stream<Path> baseStream = Stream.of(base);
        if (targets.length == 0) return baseStream;
        final String target = targets[0];
        try {
            Stream<Path> paths = Files.find(base, maxDepth, createPredicate(onlyDir, target), options);
            if (targets.length == 1) return paths;
            String[] ts = Arrays.copyOfRange(targets, 1, targets.length);
            return paths.flatMap(path -> findPath(path, ts, maxDepth, onlyDir, options));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static BiPredicate<Path, BasicFileAttributes> createPredicate(final boolean onlyDir, final String target) {
        return (path, basicFileAttributes) -> (!onlyDir || Files.isDirectory(path)) && StringUtils.equalsIgnoreCase(path.getFileName().toString(), target);
    }

    public static URL newUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Judge whether the path is absolute or not
     *
     * @param path path
     * @return return true if path is absolute
     */
    public static boolean isAbsolute(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        return PathUtil.isAbsolute(Paths.get(path));
    }

    /**
     * Judge whether the path is absolute or not
     *
     * @param path path
     * @return return true if path is absolute
     */
    public static boolean isAbsolute(Path path) {
        return path.isAbsolute();
    }

    public static URL path2Url(Path path) {
        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Normalize path:
     * <strong>resolve relative path and double separator, do not trim ending separator</strong>
     *
     * @param path
     * @return
     */
    public static String normalizePath(String path) {
        try {
            return new URI(path).normalize().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}