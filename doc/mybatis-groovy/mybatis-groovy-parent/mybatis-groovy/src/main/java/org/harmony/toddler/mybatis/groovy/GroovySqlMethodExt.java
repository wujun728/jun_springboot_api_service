package org.harmony.toddler.mybatis.groovy;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public abstract class GroovySqlMethodExt extends GroovySqlScriptBase {
    static Pattern REGEX_AND_OR = Pattern.compile("(?i)^and|^or");
    static String SELECT = " SELECT ";
    static String FROM = " FROM ";
    static String WHERE = " WHERE ";
    static String GROUP_BY = " GROUP BY ";
    static String ORDER_BY = " ORDER BY ";
    static String LIMIT = " LIMIT ";
    static String OFFSET = " OFFSET ";
    static String COMMA = ",";

    public static String select(String fields) {
        return trim(fields, SELECT, EMPTY, EMPTY, COMMA);
    }

    public static String select(Closure<String> fields) {
        return select(fields.call(EMPTY));
    }

    public static String from(String tables) {
        return trim(tables, FROM, EMPTY, EMPTY, EMPTY);
    }

    public static String from(Closure<String> tables) {
        return from(tables.call(EMPTY));
    }

    public static String where(String cond) {
        String c = trimOrEmpty(cond);
        if (StringUtils.isNotEmpty(c)) {
            return WHERE + RegExUtils.replaceFirst(c, REGEX_AND_OR, EMPTY);
        }
        return EMPTY;
    }

    public static String where(Closure<String> cond) {
        return where(cond.call(EMPTY));
    }

    public static String cond(Closure<String> cond) {
        return defaultOrEmpty(cond.call(EMPTY));
    }

    public static String propOrEmpty(GroovyObject param,
                                     String name,
                                     Closure<String> closure) {
        return propOrDef(param, name, EMPTY, closure);
    }

    public static String propOrDef(GroovyObject param,
                                   String name,
                                   String defVal,
                                   Closure<String> closure) {
        if (hasProperty(param, name)) {
            String v = prop(param, name);
            return StringUtils.isNotEmpty(v) ? closure.call(v) : defVal;
        } else {
            return defVal;
        }
    }

    public static String propOrEmpty(Object prop, Closure<String> closure) {
        return propOrDef(prop, EMPTY, closure);
    }

    public static String propOrDef(Object propVal, String defVal, Closure<String> closure) {
        return Objects.nonNull(propVal) ? closure.call(propVal) : defVal;
    }

    public static String prop(GroovyObject vo, String name) {
        return hasProperty(vo, name) ? Objects.toString(vo.getProperty(name)) : null;
    }

    public static boolean hasProperty(GroovyObject vo, String name) {
        return Objects.nonNull(vo.getMetaClass().hasProperty(vo, name));
    }

    public static String groupBy(String fields) {
        return trim(fields, GROUP_BY, EMPTY, EMPTY, COMMA);
    }

    public static String groupBy(String fields,
                                 String prefixOverrides,
                                 String suffixOverrides) {
        return trim(fields, GROUP_BY, prefixOverrides, EMPTY, suffixOverrides);
    }

    public static String groupBy(Closure<String> fields) {
        return groupBy(fields.call(EMPTY), EMPTY, COMMA);
    }

    public static String groupBy(String prefixOverrides,
                                 String suffixOverrides,
                                 Closure<String> fields) {
        return groupBy(fields.call(EMPTY), prefixOverrides, suffixOverrides);
    }

    public static String orderBy(String fields) {
        return trim(fields, ORDER_BY, EMPTY, EMPTY, COMMA);
    }

    public static String orderBy(String fields, String prefixOverrides, String suffixOverrides) {
        return trim(fields, ORDER_BY, prefixOverrides, EMPTY, suffixOverrides);
    }

    public static String orderBy(Closure<String> fields) {
        return orderBy(fields.call(EMPTY), EMPTY, COMMA);
    }

    public static String orderBy(String prefixOverrides, String suffixOverrides, Closure<String> fields) {
        return orderBy(fields.call(EMPTY), prefixOverrides, suffixOverrides);
    }

    public static String limit(Integer size, Integer offset) {
        if (Objects.isNull(size) || size <= 0) {
            return EMPTY;
        }
        return LIMIT + size + OFFSET + offset;
    }

    public static String limit(Integer size, Closure<String> closure) {
        return limit(size, 0, closure);
    }

    public static String limit(Integer size, Integer offset, Closure<String> closure) {
        if (Objects.isNull(size) || size <= 0) {
            return EMPTY;
        }
        if (Objects.isNull(offset) || offset <= 0) {
            offset = 0;
        }
        return defaultOrEmpty(closure.call(size, offset));
    }

    public static String trimOrEmpty(String s) {
        return StringUtils.trimToEmpty(s);
//        return StringUtils.stripToEmpty(s);
    }

    public static String defaultOrEmpty(String s) {
        return StringUtils.defaultString(s);
    }

    /**
     * trim string with prefix and suffix
     *
     * @param string
     * @param prefix
     * @param prefixOverrides
     * @param suffix
     * @param suffixOverrides
     * @return
     */
    public static String trim(String string,
                              String prefix, String prefixOverrides,
                              String suffix, String suffixOverrides) {
        String s = trimOrEmpty(string);
        if (StringUtils.isEmpty(s)) {
            return EMPTY;
        }
        String pf = defaultOrEmpty(prefix);
        String sf = defaultOrEmpty(suffix);
        String po = trimOrEmpty(prefixOverrides);
        String so = trimOrEmpty(suffixOverrides);
        s = StringUtils.stripStart(s, po);
        s = StringUtils.stripEnd(s, so);
        return pf + s + sf;
    }

    public static String currentDate() {
        return now("yyyy-MM-dd");
    }

    public static String now(String pattern) {
        return DateFormatUtils.format(System.currentTimeMillis(), pattern);
    }

    public <V> GroovySqlMethodExt join(Object entity, Closure<V> on, Map<Object, V> params) {
        return this;
    }


    public GroovySqlMethodExt eq(boolean condition, Object column, Object val) {
        return null;
    }


    public GroovySqlMethodExt ne(boolean condition, Object column, Object val) {
        return null;
    }


    public GroovySqlMethodExt gt(boolean condition, Object column, Object val) {
        return null;
    }


    public GroovySqlMethodExt ge(boolean condition, Object column, Object val) {
        return null;
    }


    public GroovySqlMethodExt lt(boolean condition, Object column, Object val) {
        return null;
    }


    public GroovySqlMethodExt le(boolean condition, Object column, Object val) {
        return null;
    }


    public GroovySqlMethodExt like(boolean condition, Object column, Object val) {
        return this;
    }


    public GroovySqlMethodExt notLike(boolean condition, Object column, Object val) {
        return not(condition).like(condition, column, val);
    }


    public GroovySqlMethodExt likeLeft(boolean condition, Object column, Object val) {
        return this;
    }


    public GroovySqlMethodExt likeRight(boolean condition, Object column, Object val) {
        return this;
    }


    public GroovySqlMethodExt between(boolean condition, Object column, Object val1, Object val2) {
        return this;
    }


    public GroovySqlMethodExt notBetween(boolean condition, Object column, Object val1, Object val2) {
        return not(condition).between(condition, column, val1, val2);
    }


    public GroovySqlMethodExt and(boolean condition, Consumer<GroovySqlMethodExt> consumer) {
        return and(condition).addNestedCondition(condition, consumer);
    }


    public GroovySqlMethodExt or(boolean condition, Consumer<GroovySqlMethodExt> consumer) {
        return or(condition).addNestedCondition(condition, consumer);
    }


    public GroovySqlMethodExt nested(boolean condition, Consumer<GroovySqlMethodExt> consumer) {
        return addNestedCondition(condition, consumer);
    }


    public GroovySqlMethodExt or(boolean condition) {
        return this;
    }


    public GroovySqlMethodExt apply(boolean condition, String applySql, Object... value) {
        return this;
    }


    public GroovySqlMethodExt last(boolean condition, String lastSql) {
        return this;
    }


    public GroovySqlMethodExt comment(boolean condition, String comment) {
        return this;
    }


    public GroovySqlMethodExt first(boolean condition, String firstSql) {
        return this;
    }


    public GroovySqlMethodExt exists(boolean condition, String existsSql) {
        return this;
    }


    public GroovySqlMethodExt notExists(boolean condition, String notExistsSql) {
        return this;
    }


    public GroovySqlMethodExt isNull(boolean condition, Object column) {
        return this;
    }


    public GroovySqlMethodExt isNotNull(boolean condition, Object column) {
        return this;
    }


    public GroovySqlMethodExt in(boolean condition, Object column, Collection<?> coll) {
        return this;
    }


    public GroovySqlMethodExt notIn(boolean condition, Object column, Collection<?> coll) {
        return not(condition).in(condition, column, coll);
    }


    public GroovySqlMethodExt inSql(boolean condition, Object column, String inValue) {
        return this;
    }


    public GroovySqlMethodExt notInSql(boolean condition, Object column, String inValue) {
        return not(condition).inSql(condition, column, inValue);
    }


    public GroovySqlMethodExt groupBy(boolean condition, Object... columns) {
        return this;
    }


    public GroovySqlMethodExt orderBy(boolean condition, boolean isAsc, Object... columns) {
        return this;
    }


    public GroovySqlMethodExt having(boolean condition, String sqlHaving, Object... params) {
        return this;
    }


    public GroovySqlMethodExt func(boolean condition, Consumer<GroovySqlMethodExt> consumer) {
        if (condition) {
            consumer.accept(this);
        }
        return this;
    }

    /**
     * 内部自用
     * <p>NOT 关键词</p>
     */
    protected GroovySqlMethodExt not(boolean condition) {
        return this;
    }

    /**
     * 内部自用
     * <p>拼接 AND</p>
     */
    protected GroovySqlMethodExt and(boolean condition) {
        return this;
    }

    /**
     * 多重嵌套查询条件
     *
     * @param condition 查询条件值
     */
    protected GroovySqlMethodExt addNestedCondition(boolean condition, Consumer<GroovySqlMethodExt> consumer) {
        return this;
    }

    /**
     * 对sql片段进行组装
     *
     * @param condition   是否执行
     * @param sqlSegments sql片段数组
     * @return GroovySqlMethodExt
     */
    protected GroovySqlMethodExt doIt(boolean condition, Object... sqlSegments) {
        return this;
    }


    /**
     * 获取 columnName
     */
    protected String columnToString(Object column) {
        return (String) column;
    }

    /**
     * 多字段转换为逗号 "," 分割字符串
     *
     * @param columns 多字段
     */
    protected String columnsToString(Object... columns) {
        return Arrays.stream(columns).map(this::columnToString).collect(joining(","));
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }
}