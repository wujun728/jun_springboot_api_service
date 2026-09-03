package com.yuyaogc.lowcode.engine.context;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.func.Compare;
import com.yuyaogc.lowcode.engine.func.Func;
import com.yuyaogc.lowcode.engine.func.Join;
import com.yuyaogc.lowcode.engine.func.Nested;

import java.io.IOException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * EQ 就是 EQUAL等于
 * NE就是 NOT EQUAL不等于
 * GT 就是 GREATER THAN大于　
 * LT 就是 LESS THAN小于
 * GE 就是 GREATER THAN OR EQUAL 大于等于
 * LE 就是 LESS THAN OR EQUAL 小于等于
 */
@JsonDeserialize(using = CriteriaJsonDeserializer.class)
public class Criteria<T, R, Children extends Criteria<T, R, Children>> extends ArrayList<Object>
        implements Compare<Children, R>, Nested<Children, Children>, Join<Children>, Func<Children, R> {
    /**
     * 占位符
     */
    protected final Children typedThis = (Children) this;

    public Criteria() {

    }

    public Criteria(BinaryOp bo) {
        add(bo);
    }

    public static Criteria binary(String field, String op, Object value) {
        return new Criteria(new BinaryOp(field, op, value));
    }

    public static Criteria equal(String field, Object value) {
        return new Criteria(new BinaryOp(field, "=", value));
    }

    public static Criteria notEqual(String field, Object value) {
        return new Criteria(new BinaryOp(field, "!=", value));
    }

    public static Criteria greater(String field, Object value) {
        return new Criteria(new BinaryOp(field, ">", value));
    }

    public static Criteria greaterOrEqual(String field, Object value) {
        return new Criteria(new BinaryOp(field, ">=", value));
    }

    public static Criteria less(String field, Object value) {
        return new Criteria(new BinaryOp(field, "<", value));
    }

    public static Criteria lessOrEqual(String field, Object value) {
        return new Criteria(new BinaryOp(field, "<=", value));
    }

    public static Criteria like(String field, Object value) {
        return new Criteria(new BinaryOp(field, "like", value));
    }

    public static Criteria notLike(String field, Object value) {
        return new Criteria(new BinaryOp(field, "!like", value));
    }

    public static Criteria ilike(String field, Object value) {
        return new Criteria(new BinaryOp(field, "ilike", value));
    }

    public static Criteria in(String field, Object value) {
        return new Criteria(new BinaryOp(field, "in", value));
    }

    public static Criteria notIn(String field, Object value) {
        return new Criteria(new BinaryOp(field, "not in", value));
    }

    public static Criteria childOf(String field, Object value) {
        return new Criteria(new BinaryOp(field, "child_of", value));
    }

    public static Criteria parentOf(String field, Object value) {
        return new Criteria(new BinaryOp(field, "parent_of", value));
    }

    public Criteria and(String field, String op, Object value) {
        if (size() > 0) {
            add(0, "&");
        }
        add(new BinaryOp(field, op, value));
        return this;
    }

    public Criteria and(Criteria... criterias) {
        for (Criteria criteria : criterias) {
            if (size() > 0) {
                add(0, "&");
            }
            addAll(criteria);
        }
        return this;
    }

    public Criteria or(String field, String op, Object value) {
        if (size() > 0) {
            add(0, "|");
        }
        add(new BinaryOp(field, op, value));
        return this;
    }

    public Criteria or(Criteria... criterias) {
        for (Criteria criteria : criterias) {
            if (size() > 0) {
                add(0, "|");
            }
            addAll(criteria);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public static Criteria parse(List<Object> list) {
        Criteria criteria = new Criteria();
        for (Object obj : list) {
            if (obj instanceof String) {
                criteria.add(obj);
            } else if (obj instanceof Map<?, ?>) {
                Map<String, Object> map = (Map<String, Object>) obj;
                BinaryOp binaryOp = new BinaryOp((String) map.get("field"), (String) map.get("op"), map.get("value"));
                criteria.add(binaryOp);
            } else if (obj instanceof List<?>) {
                List<Object> args = (List<Object>) obj;
                if (args.size() == 3) {
                    BinaryOp binaryOp = new BinaryOp((String) args.get(0), (String) args.get(1),
                            args.get(2));
                    criteria.add(binaryOp);
                }
            }
        }
        return criteria;
    }

    public static Criteria parse(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Criteria.class);
        } catch (Exception e) {
            throw new EngineException(String.format("无法把[%s]解析为Criteria", json));
        }
    }

    protected void append(Criteria criteria) {
        this.and(criteria);
    }

    protected String columnToString(R column) {
        return (String) column;
    }


    /**
     * 做事函数
     */
    @FunctionalInterface
    public interface DoSomething {

        void doIt();
    }

    protected final Children maybeDo(boolean condition, DoSomething something) {
        if (condition) {
            something.doIt();
        }
        return typedThis;
    }

    @Override
    public <V> Children allEq(boolean condition, Map<R, V> params, boolean null2IsNull) {
        return null;
    }

    @Override
    public <V> Children allEq(boolean condition, BiPredicate<R, V> filter, Map<R, V> params, boolean null2IsNull) {
        return null;
    }

    @Override
    public Children eq(boolean condition, R column, Object val) {
        return maybeDo(condition, () -> append(Criteria.equal(columnToString(column), val)));
    }

    @Override
    public Children ne(boolean condition, R column, Object val) {
        return maybeDo(condition, () -> append(Criteria.notEqual(columnToString(column), val)));

    }

    @Override
    public Children gt(boolean condition, R column, Object val) {
        return maybeDo(condition, () -> append(Criteria.greater(columnToString(column), val)));
    }

    @Override
    public Children ge(boolean condition, R column, Object val) {
        return maybeDo(condition, () -> append(Criteria.greaterOrEqual(columnToString(column), val)));
    }

    @Override
    public Children lt(boolean condition, R column, Object val) {
        return maybeDo(condition, () -> append(Criteria.less(columnToString(column), val)));
    }

    @Override
    public Children le(boolean condition, R column, Object val) {
        return maybeDo(condition, () -> append(Criteria.lessOrEqual(columnToString(column), val)));
    }

    @Override
    public Children between(boolean condition, R column, Object val1, Object val2) {
        return null;
    }

    @Override
    public Children notBetween(boolean condition, R column, Object val1, Object val2) {
        return null;
    }

    @Override
    public Children like(boolean condition, R column, Object val) {
        return maybeDo(condition, () -> append(Criteria.like(columnToString(column), val)));
    }

    @Override
    public Children notLike(boolean condition, R column, Object val) {
        return maybeDo(condition, () -> append(Criteria.notLike(columnToString(column), val)));
    }

    @Override
    public Children notLikeLeft(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public Children notLikeRight(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public Children likeLeft(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public Children likeRight(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public Children isNull(boolean condition, R column) {
        return null;
    }

    @Override
    public Children isNotNull(boolean condition, R column) {
        return null;
    }

    @Override
    public Children in(boolean condition, R column, Collection<?> coll) {
        return maybeDo(condition, () -> append(Criteria.in(columnToString(column), (Object) coll)));
    }

    @Override
    public Children in(boolean condition, R column, Object... values) {
        return null;
    }

    @Override
    public Children notIn(boolean condition, R column, Collection<?> coll) {
        return maybeDo(condition, () -> append(Criteria.notIn(columnToString(column), (Object) coll)));
    }

    @Override
    public Children notIn(boolean condition, R column, Object... values) {
        return null;
    }

    @Override
    public Children inSql(boolean condition, R column, String sql) {
        return null;
    }

    @Override
    public Children gtSql(boolean condition, R column, String sql) {
        return null;
    }

    @Override
    public Children geSql(boolean condition, R column, String sql) {
        return null;
    }

    @Override
    public Children ltSql(boolean condition, R column, String sql) {
        return null;
    }

    @Override
    public Children leSql(boolean condition, R column, String sql) {
        return null;
    }

    @Override
    public Children notInSql(boolean condition, R column, String inValue) {
        return null;
    }

    @Override
    public Children groupBy(boolean condition, R column) {
        return null;
    }

    @Override
    public Children groupBy(boolean condition, List<R> columns) {
        return null;
    }

    @Override
    public Children groupBy(boolean condition, R column, R... columns) {
        return null;
    }

    @Override
    public Children groupBy(boolean condition, R column, List<R> columns) {
        return null;
    }

    @Override
    public Children orderBy(boolean condition, boolean isAsc, R column) {
        return null;
    }

    @Override
    public Children orderBy(boolean condition, boolean isAsc, List<R> columns) {
        return null;
    }

    @Override
    public Children orderBy(boolean condition, boolean isAsc, R column, R... columns) {
        return null;
    }

    @Override
    public Children orderBy(boolean condition, boolean isAsc, R column, List<R> columns) {
        return null;
    }

    @Override
    public Children having(boolean condition, String sqlHaving, Object... params) {
        return null;
    }

    @Override
    public Children func(boolean condition, Consumer<Children> consumer) {
        return null;
    }

    @Override
    public Children or(boolean condition) {
        return null;
    }

    @Override
    public Children apply(boolean condition, String applySql, Object... values) {
        return null;
    }

    @Override
    public Children last(boolean condition, String lastSql) {
        return null;
    }

    @Override
    public Children comment(boolean condition, String comment) {
        return null;
    }

    @Override
    public Children first(boolean condition, String firstSql) {
        return null;
    }

    @Override
    public Children exists(boolean condition, String existsSql, Object... values) {
        return null;
    }

    @Override
    public Children notExists(boolean condition, String existsSql, Object... values) {
        return null;
    }

    @Override
    public Children and(boolean condition, Consumer<Children> consumer) {
        return null;
    }

    @Override
    public Children or(boolean condition, Consumer<Children> consumer) {
        return null;
    }

    @Override
    public Children nested(boolean condition, Consumer<Children> consumer) {
        return null;
    }

    @Override
    public Children not(boolean condition, Consumer<Children> consumer) {
        return null;
    }
}

class CriteriaJsonDeserializer extends JsonDeserializer<Criteria> {

    @Override
    public Criteria deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonToken token = parser.currentToken();
        ArrayList<Object> list = null;
        if (token == JsonToken.START_ARRAY) {
            list = parser.readValueAs(new TypeReference<ArrayList<?>>() {
            });
        } else if (token == JsonToken.START_OBJECT) {
            HashMap<String, ?> map = parser.readValueAs(new TypeReference<HashMap<String, ?>>() {
            });
            list = new ArrayList<>();
            list.add(map);
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        return Criteria.parse(list);
    }
}
