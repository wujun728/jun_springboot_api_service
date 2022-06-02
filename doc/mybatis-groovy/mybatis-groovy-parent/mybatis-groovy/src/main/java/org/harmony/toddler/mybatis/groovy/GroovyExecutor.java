package org.harmony.toddler.mybatis.groovy;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Groovy Script Executor
 */
@Slf4j
public class GroovyExecutor {

    private final GroovyScriptEngine engine;

    /**
     * Create an instance of {@link GroovyExecutor}
     *
     * @param roots
     * @return
     */
    public static GroovyExecutor create(URL... roots) {
        if (roots.length > 0) {
            return new GroovyExecutor(roots);
        }
        return new GroovyExecutor(ClassLoader.getSystemResource("groovy/"));
    }

    /**
     * obsolete
     *
     * @return
     */
    @Deprecated
    public static GroovyExecutor ofDev() {
        String projectPath = System.getProperty("user.dir");
        try {
            URL root = Paths.get(projectPath, "src", "main", "resources", "groovy").toUri().toURL();
            return new GroovyExecutor(root);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private GroovyExecutor(URL... roots) {
        this.engine = new GroovyScriptEngine(roots, this.getClass().getClassLoader());
        for (URL root : roots) {
            log.info("root[{}]", root);
        }
    }

    /**
     * Adds a classpath of scriptss.
     *
     * @param path is a jar file or a directory.
     */
    public void addClasspath(final String path) {
        engine.getGroovyClassLoader().addClasspath(path);
    }

    /**
     * Adds a URL root of scripts.
     *
     * @param url
     */
    public void addUrl(URL url) {
        engine.getGroovyClassLoader().addURL(url);
    }

    public void setBase(String scriptBaseClass) {
        this.engine.getConfig().setScriptBaseClass(scriptBaseClass);
    }

    public void addImports(String... classNames) {
        ImportCustomizer importCustomizer = new ImportCustomizer();
        importCustomizer.addImports(classNames);
        this.addCompilationCustomizers(importCustomizer);
    }

    public void addCompilationCustomizers(final CompilationCustomizer... customizers) {
        engine.getConfig().addCompilationCustomizers(customizers);
    }

    /**
     * Run a script with binding
     *
     * @param script
     * @param binding
     * @return
     */
    public Object run(String script, Binding binding) {
        try {
            return engine.run(script, binding);
        } catch (ResourceException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (ScriptException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Object call(String script, Binding binding, String method, Object args) {
        try {
            return engine.createScript(script, binding).invokeMethod(method, args);
        } catch (ResourceException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (ScriptException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
