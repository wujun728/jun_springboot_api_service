package org.harmony.toddler.mybatis.groovy;

import groovy.lang.Binding;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static org.harmony.toddler.mybatis.groovy.GroovySqlSource.PARAMETER_OBJECT_KEY;


@Slf4j
public class GroovyFacade {

    public static final String EXT = ".groovy";

    private static GroovyExecutor engine;
    private static final Map<String, Object> additionalCtxAttributes = new HashMap<>();
    private static final Map<String, GroovySqlFunction> funcMap = new HashMap<>();

    private GroovyFacade() {
        // Prevent instantiation
    }

    /**
     * Initialize a template engine.
     *
     * @param driverConfig a language driver configuration
     * @since 2.1.0
     */
    public static void initialize(GroovyLangDriverConfig driverConfig) {
        engine = GroovyExecutor.create(driverConfig.getRoots());
        engine.setBase(driverConfig.getGroovyScriptBaseClassName());
    }

    /**
     * Append Script Extension(.groovy)
     *
     * @param script
     * @return
     */
    public static String format(String script) {
        String spt = StringUtils.trimToEmpty(script);
        spt = spt.endsWith(EXT) ? spt : spt + EXT;
        log.trace("format:spt:{}:{}", script, spt);
        return spt;
    }

    /**
     * Destroy a template engine.
     *
     * @since 2.1.0
     */
    public static void destroy() {
        additionalCtxAttributes.clear();
    }

    /**
     * Just Trim the Sql Script
     *
     * @param script
     * @return
     */
    public static Object compile(String script) {
        log.info("compile:[{}]", script);
        return format(script);
    }

    /**
     * Just Trim the Sql Script, split method after #
     *
     * @param script
     * @param name
     * @return
     */
    public static Object compile(String script, String name) {
        log.info("compile:[{},{}]", script, name);
        String spt = StringUtils.trimToEmpty(script);
        String[] ss = StringUtils.split(spt, '#');
        final String spt0 = format(ss[0]);
        if (ss.length > 1) {
            String method = StringUtils.trimToEmpty(ss[1]);
            funcMap.put(script, context -> Objects.toString(run(spt0, method, context), StringUtils.EMPTY));
        } else {
            funcMap.put(script, context -> Objects.toString(run(spt0, context), StringUtils.EMPTY));
        }
        return script;
    }

    public static String applyFunc(Object template, Map<String, Object> context) {
        log.trace("apply:tpl:{}", template);
        context.putAll(additionalCtxAttributes);
        return funcMap.get(template.toString()).apply(context);
    }

    public static String apply(Object template, Map<String, Object> context) {
        log.trace("apply:tpl:{}", template);
        context.putAll(additionalCtxAttributes);
        return (engine.run(template.toString(), new Binding(context))).toString();
    }

    public static Object run(String script, Map<String, Object> context) {
        log.trace("run:script:{}", script);
        context.putAll(additionalCtxAttributes);
        return engine.run(format(script), new Binding(context));
    }

    public static Object run(String script, String method, Map<String, Object> context) {
        return run(script, method, context, context.get(PARAMETER_OBJECT_KEY));
    }

    public static Object run(String script, String method, Map<String, Object> context, Object args) {
        log.trace("run:script:{}:method:{}", script, method);
        context.putAll(additionalCtxAttributes);
        return engine.call(format(script), new Binding(context), method, args);
    }
}

interface GroovySqlFunction extends Function<Map<String, Object>, String> {
}