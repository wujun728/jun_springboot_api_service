package com.yuyaogc.lowcode.engine.context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.ColumnType;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Config;
import com.yuyaogc.lowcode.engine.plugin.activerecord.SqlPara;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * 这个类的主要职责是将过滤条件表达式编译为SQL查询。
 * 过滤条件是由二元条件和运算符构成。可用的运算符是'!','&'和'|'。
 * '!'是一元'非'运算符，'&'是二进制的'and'，'|'是二进制的'or'。
 * 例如, 这是一个有效的过滤条件：
 * <p>
 * ["&", "!", {term1}, "|", {term2}, {term3}]
 * </p>
 * <p>
 * 二元条件是由字段，操作符，值构成，以下两种都是有效的二元条件：
 * <p>
 * {"field":"field1", "op":"=", "value":"value1"}
 * </p>
 * <p>
 * ["field1", "=", "value1"]
 * </p>
 */
public class Expression {
    protected static Logger logger = LoggerFactory.getLogger(Expression.class);

    static final String ID = "id";
    static final String OP_IN = "in";
    static final String OP_NOT_IN = "not in";
    static final String OP_IN_SELECT = "inselect";
    static final String OP_NOT_IN_SELECT = "not inselect";
    static final String OP_EQ = "=";
    static final String OP_NOT_EQ = "!=";
    static final String OP_LT_GT = "<>";
    static final String OP_CHILD_OF = "child_of";
    static final String OP_PARENT_OF = "parent_of";
    static final String OP_EQ_Q = "=?";
    static final String OP_LIKE = "like";
    static final String OP_NOT_LIKE = "not like";
    static final String OP_ILIKE = "ilike";
    static final String OP_NOT_ILIKE = "not ilike";
    static final String OP_LT = "<";
    static final String OP_GT = ">";
    static final String OP_LTE = "<=";
    static final String OP_GTE = ">=";

    static final String OP_NOT = "!";
    static final String OP_OR = "|";
    static final String OP_AND = "&";
    static final List<String> FILTER_OPS = Arrays.asList(OP_NOT, OP_OR, OP_AND);
    static final List<String> TERM_OPS = Arrays.asList(OP_EQ, OP_NOT_EQ, OP_LTE, OP_LT, OP_GT, OP_GTE, OP_EQ_Q, OP_LIKE,
            OP_NOT_LIKE, OP_ILIKE, OP_NOT_ILIKE, OP_IN, OP_NOT_IN, OP_CHILD_OF, OP_PARENT_OF);
    static final List<String> NEGATIVE_TEAM_OPS = Arrays.asList(OP_NOT_EQ, OP_NOT_LIKE, OP_NOT_ILIKE, OP_NOT_IN);
    static final Map<String, String> FILTER_OPS_NEGATION = new HashMap<String, String>() {
        {
            put(OP_AND, OP_OR);
            put(OP_OR, OP_AND);
        }
    };
    static final Map<String, String> TERM_OPS_NEGATION = new HashMap<String, String>() {
        {
            put(OP_LT, OP_GTE);
            put(OP_GT, OP_LTE);
            put(OP_LTE, OP_GT);
            put(OP_GTE, OP_LT);
            put(OP_EQ, OP_NOT_EQ);
            put(OP_NOT_EQ, OP_EQ);
            put(OP_IN, OP_NOT_IN);
            put(OP_LIKE, OP_NOT_LIKE);
            put(OP_ILIKE, OP_NOT_ILIKE);
            put(OP_NOT_IN, OP_IN);
            put(OP_NOT_LIKE, OP_LIKE);
            put(OP_NOT_ILIKE, OP_ILIKE);
        }
    };
    static final BinaryOp TRUE_LEAF = new BinaryOp(1, OP_EQ, 1);
    static final BinaryOp FALSE_LEAF = new BinaryOp(0, OP_EQ, 1);

    static final List<Object> TRUE_FILTER = Arrays.asList(TRUE_LEAF);
    static final List<Object> FALSE_FILTER = Arrays.asList(FALSE_LEAF);

    public static boolean isFalse(EntityClass model, List<Object> criteria) {
        Stack<Integer> stack = new Stack<>();
        criteria = normalizeCriteria(criteria);
        for (int i = criteria.size() - 1; i >= 0; i--) {
            Object token = criteria.get(i);
            if (OP_AND.equals(token)) {
                stack.push(Math.min(stack.pop(), stack.pop()));
            } else if (OP_OR.equals(token)) {
                stack.push(Math.max(stack.pop(), stack.pop()));
            } else if (OP_NOT.equals(token)) {
                stack.push(-stack.pop());
            } else if (TRUE_LEAF.equals(token)) {
                stack.push(1);
            } else if (FALSE_LEAF.equals(token)) {
                stack.push(-1);
            } else if (token instanceof BinaryOp) {
                BinaryOp bo = (BinaryOp) token;
                if (OP_IN.equals(bo.getOp()) && isEmptyCollection(bo)) {
                    stack.push(-1);
                } else if (OP_NOT_IN.equals(bo.getOp()) && isEmptyCollection(bo)) {
                    stack.push(1);
                } else {
                    stack.push(0);
                }
            } else {
                stack.push(0);
            }
        }
        return stack.pop() == -1;
    }

    static boolean isEmptyCollection(BinaryOp bo) {
        return !(bo.getValue() instanceof Query) && bo.getValue() instanceof Collection<?>
                && ((Collection<?>) bo.getValue()).isEmpty();
    }

    static List<Object> normalizeCriteria(List<Object> criteria) {
        if (criteria.isEmpty()) {
            return Arrays.asList(TRUE_LEAF);
        }
        List<Object> result = new ArrayList<Object>();
        int expected = 1;
        Map<String, Integer> opArity = new HashMap<String, Integer>(3) {
            {
                put(OP_NOT, 1);
                put(OP_AND, 2);
                put(OP_OR, 2);
            }
        };
        for (Object d : criteria) {
            Object token = d;
            if (expected == 0) {
                result.add(0, OP_AND);
                expected = 1;
            }
            if (token instanceof BinaryOp) {
                expected -= 1;
            } else {
                expected += opArity.getOrDefault(token.toString(), 0) - 1;
            }
            result.add(token);
        }
        assert (expected == 0) : String.format("参数criteria语法不正确:%s", getCriteriaString(criteria));
        return result;
    }

    static String getCriteriaString(List<Object> criteria) {
        try {
            return new ObjectMapper().writeValueAsString(criteria);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    static List<Object> combine(String op, List<Object> unit, List<Object> zero, List<List<Object>> criterias) {
        if (criterias.size() == 1 && criterias.get(0).equals(unit)) {
            return unit;
        }

        List<Object> result = new ArrayList<>();
        int count = 0;
        for (List<Object> criteria : criterias) {
            if (criteria.equals(unit)) {
                continue;
            }
            if (criteria.equals(zero)) {
                return zero;
            }
            if (!criteria.isEmpty()) {
                result.addAll(normalizeCriteria(criteria));
                count++;
            }
        }
        for (int i = 0; i < count - 1; i++) {
            result.set(0, op);
        }
        return result.isEmpty() ? unit : result;
    }

    static List<Object> and(List<List<Object>> criterias) {
        return combine(OP_AND, Arrays.asList(TRUE_LEAF), Arrays.asList(FALSE_LEAF), criterias);
    }

    static List<Object> or(List<List<Object>> criterias) {
        return combine(OP_OR, Arrays.asList(FALSE_LEAF), Arrays.asList(TRUE_LEAF), criterias);
    }

    static List<Object> distributeNot(List<Object> criteria) {
        List<Object> result = new ArrayList<>();
        Stack<Boolean> stack = new Stack<>();
        stack.push(false);
        for (Object token : criteria) {
            boolean negate = stack.pop();
            if (isLeaf(token, false)) {
                if (negate) {
                    BinaryOp bo = (BinaryOp) token;
                    if (TERM_OPS_NEGATION.containsKey(bo.getOp())) {
                        if (token.equals(TRUE_LEAF)) {
                            result.add(FALSE_LEAF);
                        } else if (token.equals(FALSE_LEAF)) {
                            result.add(TRUE_LEAF);
                        } else {
                            result.add(new BinaryOp(bo.getField(), TERM_OPS_NEGATION.get(bo.getOp()), bo.getValue()));
                        }
                    } else {
                        result.add(OP_NOT);
                        result.add(token);
                    }
                } else {
                    result.add(token);
                }
            } else if (OP_NOT.equals(token)) {
                stack.push(!negate);
            } else if (token instanceof String && FILTER_OPS_NEGATION.containsKey((String) token)) {
                result.add(negate ? FILTER_OPS_NEGATION.get((String) token) : token);
                stack.push(negate);
                stack.push(negate);
            } else {
                result.add(token);
            }
        }
        return result;
    }

    static Object normalizeLeaf(Object element) {
        if (!isLeaf(element, false)) {
            return element;
        }
        BinaryOp bo = (BinaryOp) element;
        String original = bo.getOp();
        String op = bo.getOp().toLowerCase();
        if (OP_LT_GT.equals(op)) {
            op = OP_NOT_EQ;
        }
        Object left = bo.getField();
        Object right = bo.getValue();
        boolean inBool = right instanceof Boolean && (OP_IN.equals(op) || OP_NOT_IN.equals(op));
        if (inBool) {
            logger.warn("过滤条件({},{},{}) 中应使用 '=' 或者 '!=' 对比符", left, original, right);
            op = OP_IN.equals(op) ? OP_EQ : OP_NOT_EQ;
        }
        boolean equalsCollection = right instanceof Collection && (OP_EQ.equals(op) || OP_NOT_EQ.equals(op));
        if (equalsCollection) {
            logger.warn("过滤条件({},{},{})中应使用 'in' 或者 'not in' 对比符", left, original,
                    right);
            op = OP_EQ.equals(op) ? OP_IN : OP_NOT_IN;
        }
        return new BinaryOp(left, op, right);
    }

    static boolean isOp(Object element) {
        return element instanceof String && FILTER_OPS.contains((String) element);
    }

    static boolean isLeaf(Object element, boolean internal) {
        if (element instanceof BinaryOp) {
            BinaryOp bo = (BinaryOp) element;
            boolean isOps = TERM_OPS.contains(bo.getOp()) || OP_LT_GT.equals(bo.getOp())
                    || (internal && (OP_IN_SELECT.equals(bo.getOp()) || OP_NOT_IN_SELECT.equals(bo.getOp())));
            if (!isOps) {
                logger.warn("过滤条件{}中，对比操作符{}无效", bo, bo.getOp());
            }
            return true;
        }
        return false;
    }

    static boolean isBool(Object element) {
        return TRUE_LEAF.equals(element) || FALSE_LEAF.equals(element);
    }

    static void checkLeaf(Object element, boolean internal) {
        if (!isOp(element) && !isLeaf(element, internal)) {
            throw new EngineException(String.format("Invalid leaf %s", element));
        }
    }

    EntityClass rootModel;
    String rootAlias;
    List<Object> expression;
    Query query;
    Config cr;

    public Query getQuery() {
        return query;
    }

    public Expression(Config config, List<Object> criteria, EntityClass model, String alias, Query query) {
        cr = config;
        rootModel = model;
        rootAlias = alias == null ? model.getTableName() : alias;
        expression = distributeNot(normalizeCriteria(criteria));
        this.query = query == null
                ? new Query(config, model.getTableName(),
                model.getTableName())
                : query;
        parse();
    }

    class LeafModel {
        Object leaf;
        EntityClass model;
        String alias;

        public LeafModel(Object leaf, EntityClass model, String alias) {
            this.leaf = leaf;
            this.model = model;
            this.alias = alias;
        }
    }

    void parseOp(Object leaf, Stack<SqlPara> resultStack) {
        String op = (String) leaf;
        if (OP_NOT.equals(op)) {
            SqlPara sf = resultStack.pop();
            resultStack.push(new SqlPara("(NOT (" + sf.getSql() + "))", sf.getParmas()));
        } else {
            SqlPara lhs = resultStack.pop();
            SqlPara rhs = resultStack.pop();
            String expr = OP_AND.equals(op) ? "(" + lhs.getSql() + " AND " + rhs.getSql() + ")"
                    : "(" + lhs.getSql() + " OR " + rhs.getSql() + ")";
            List<Object> params = new ArrayList<>();
            params.addAll(lhs.getParmas());
            params.addAll(rhs.getParmas());
            resultStack.push(new SqlPara(expr, params));
        }

    }

    void parseLeaf(DataType field, LeafModel lm, String left, String op, Object right,
                   Stack<LeafModel> stack, Stack<SqlPara> resultStack) {
        int datelength = 10;
        if (Constants.DATETIME.equals(field.getType()) && right != null) {
            if (right instanceof String && ((String) right).length() == datelength) {
                String str = (String) right;
                if (OP_GT.equals(op) || OP_LTE.equals(op)) {
                    str += " 23:59:59";
                } else {
                    str += " 00:00:00";
                }
                stack.push(new LeafModel(new BinaryOp(left, op, str), lm.model, lm.alias));
            } else if (right instanceof Date && !ClassUtils.isAssignable(right.getClass(), Timestamp.class)) {
                Date d = (Date) right;
                if (OP_GT.equals(op) || OP_LTE.equals(op)) {
                    right = new Timestamp(d.getYear(), d.getMonth(), d.getDay(), 23, 59, 59, 0);
                } else {
                    right = new Timestamp(d.getYear(), d.getMonth(), d.getDay(), 0, 0, 0, 0);
                }
                stack.push(new LeafModel(new BinaryOp(left, op, right), lm.model, lm.alias));
            } else {
                resultStack.add(leafToSql((BinaryOp) lm.leaf, lm.model, lm.alias));
            }
        }
        // else if (field.Translate && right.IsTrue())
        // {
        // //todo
        // throw new NotImplementedException();
        // }
        else {
            resultStack.add(leafToSql((BinaryOp) lm.leaf, lm.model, lm.alias));
        }
    }

    boolean parseMany2one(EntityField field, LeafModel lm, String left, String op, Object right, Stack<LeafModel> stack,
                          Stack<SqlPara> resultStack) {
        if (field.getDataType() instanceof DataType.Many2oneField) {

            DataType.Many2oneField m2o = (DataType.Many2oneField) field.getDataType();
            if ((OP_CHILD_OF.equals(op) || OP_PARENT_OF.equals(op))) {
            } else {

                resultStack.push(leafToSql((BinaryOp) lm.leaf, lm.model, lm.alias));
            }
            return true;
        }
        return false;
    }

    boolean parseMany2many(EntityField field, LeafModel lm, String left, String op, Object right, Stack<LeafModel> stack,
                           Stack<SqlPara> resultStack) {
        if (field.getDataType() instanceof DataType.Many2manyField) {
            DataType.Many2manyField m2m = (DataType.Many2manyField) field.getDataType();
            EntityClass relClass = Container.me().getEntityClass(field.getRelModel());
            resultStack.push(new SqlPara(String.format("%s.id IN (SELECT %s.%s FROM %s WHERE %s.%s %s %%s)",
                    cr.quote(rootAlias),
                    cr.quote(relClass.getTableName()),
                    cr.quote(field.getJoinColumnName()),
                    cr.quote(relClass.getTableName()),
                    cr.quote(relClass.getTableName()),
                    cr.quote(field.getInverseName()),
                    op), Arrays.asList(right)));


            return true;
        }
        return false;
    }

    boolean parseOne2many(EntityField field, LeafModel lm, String left, String op, Object right, Stack<LeafModel> stack,
                          Stack<SqlPara> resultStack) {
        if (field.getDataType() instanceof DataType.One2manyField) {
            DataType.One2manyField o2m = (DataType.One2manyField) field.getDataType();
            // TODO
            return true;
        }
        return false;
    }

    boolean parseOfId(LeafModel lm, String left, String op, Object right, Stack<LeafModel> stack,
                      Stack<SqlPara> resultStack) {
        boolean isOfId = ID.equals(left) && (OP_CHILD_OF.equals(op) || OP_PARENT_OF.equals(op));
        if (isOfId) {
            List<Object> ids2 = toIds(right, lm.model, lm.leaf);
            List<Object> dom = hierarchyFunc(op, left, ids2, lm.model, null, "");
            for (Object domLeaf : dom) {
                stack.push(new LeafModel(domLeaf, lm.model, lm.alias));
            }
            return true;
        }
        return false;
    }

    boolean parseMany2onePath(String[] path, EntityField field, LeafModel lm, String op, Object right,
                              Stack<LeafModel> stack, Stack<SqlPara> resultStack) {
        if (path.length > 1 && field.getDataType() instanceof DataType.Many2oneField) {
            DataType.Many2oneField m2o = (DataType.Many2oneField) field.getDataType();
            String relModel = field.getRelModel();
            EntityClass relClass = Container.me().getEntityClass(relModel);
            String aliasRel = query.leftJoin(lm.alias, field.getColumnName(), relClass.getTableName(), ID, field.getColumnName());
            stack.push(new LeafModel(new BinaryOp(path[1], op, right), relClass, aliasRel));
            return true;
        }
        return false;
    }

    boolean parseOne2manyPathJoin(String[] path, EntityField field, LeafModel lm, String op, Object right,
                                  Stack<LeafModel> stack, Stack<SqlPara> resultStack) {
        if (path.length > 1 && field.isStore() && field.getDataType() instanceof DataType.One2manyField) {
            // TODO
            throw new UnsupportedOperationException();
        }
        return false;
    }

    boolean parse2manyPath(String[] path, EntityField field, LeafModel lm, String op, Object right,
                           Stack<LeafModel> stack, Stack<SqlPara> resultStack) {
        boolean result = path.length > 1 && field.isStore()
                && (field.getDataType() instanceof DataType.Many2manyField || field.getDataType() instanceof DataType.One2manyField);
        if (result) {
            // TODO
            throw new UnsupportedOperationException();
        }
        return result;
    }

    boolean parseOfOne2many(EntityField field, LeafModel lm, String left, String op, Object right, Stack<LeafModel> stack,
                            Stack<SqlPara> resultStack) {
        boolean result = field.getDataType() instanceof DataType.One2manyField && (OP_CHILD_OF.equals(op) || OP_PARENT_OF.equals(op));
        if (result) {
            DataType.One2manyField o2m = (DataType.One2manyField) field.getDataType();
            // TODO
        }
        return result;
    }


    void parse() {
        Stack<LeafModel> stack = new Stack<>();
        Stack<SqlPara> resultStack = new Stack<>();

        for (Object leaf : expression) {
            leaf = normalizeLeaf(leaf);
            checkLeaf(leaf, false);
            stack.push(new LeafModel(leaf, rootModel, rootAlias));
        }

        while (stack.size() > 0) {
            LeafModel lm = stack.pop();
            if (isOp(lm.leaf)) {
                parseOp(lm.leaf, resultStack);
                continue;
            }

            if (isBool(lm.leaf)) {
                SqlPara sf = leafToSql((BinaryOp) lm.leaf, lm.model, lm.alias);
                resultStack.push(sf);
                continue;
            }

            BinaryOp bo = (BinaryOp) lm.leaf;
            String left = (String) bo.getField();
            String op = bo.getOp();
            Object right = bo.getValue();

            String[] path = left.split("\\.", 2);
            EntityField field = lm.model.getField(path[0]);
            if (field == null) {
                throw new EngineException(
                        String.format("无效的字段[%s.%s]在条件节点[%s]", lm.model.getName(), path[0], lm.leaf));
            }
            if (field.isInherited()) {
                continue;
            } else if (parseOfId(lm, left, op, right, stack, resultStack)) {
                continue;
            } else if (parseMany2onePath(path, field, lm, op, right, stack, resultStack)) {
                continue;
            } else if (parseOne2manyPathJoin(path, field, lm, op, right, stack, resultStack)) {
                continue;
            } else if (parse2manyPath(path, field, lm, op, right, stack, resultStack)) {
                continue;
            } else if (parseOfOne2many(field, lm, left, op, right, stack, resultStack)) {
                continue;
            } else if (parseOne2many(field, lm, left, op, right, stack, resultStack)) {
                continue;
            } else if (parseMany2many(field, lm, left, op, right, stack, resultStack)) {
                continue;
            } else if (parseMany2one(field, lm, left, op, right, stack, resultStack)) {
                continue;
            } else if (!field.isStore()) {
                continue;
            } else {
                parseLeaf(field.getDataType(), lm, left, op, right, stack, resultStack);
            }
        }

        SqlPara result = resultStack.pop();
        query.addWhere(result.getSql(), result.getParmas());
    }

    SqlPara leafToInSql(BinaryOp leaf, EntityClass model, String left, String op, Object right, String tableAlias,
                        Config cr) {
        if (right instanceof Query) {
            Query.SelectClause sub = ((Query) right).select();
            return new SqlPara(String.format("(%s.%s %s (%s))", tableAlias, cr.quote(left), op, sub.getQuery()),
                    sub.getParams());
        }
        if (right instanceof Collection<?>) {
            List<Object> params = new ArrayList<>((Collection<?>) right);
            String query;
            if (params.isEmpty()) {
                query = OP_IN.equals(op) ? "FALSE" : "TRUE";
            } else {
                String[] formats = new String[params.size()];
                Arrays.fill(formats, "%s");
                String instr = StringUtils.join(formats, ",");
                EntityField field = null;
                //if (left != ID) {
                     field = model.getField(left);
                    for (int i = 0; i < params.size(); i++) {
                        params.set(i, field.getDataType().convertToColumn(params.get(i), model, false));
                    }
                //}
                query = String.format("(%s.%s %s (%s))", tableAlias, cr.quote(field.getColumnName()), op, instr);
            }
            return new SqlPara(query, params);
        }
        throw new EngineException("无效的条件值:" + leaf);
    }

    SqlPara leafToSqlInner(BinaryOp leaf, EntityClass model, EntityField field, String left, String op, Object right,
                           String tableAlias, Config cr) {
        boolean needWildcard = OP_LIKE.equals(op) || OP_ILIKE.equals(op) || OP_NOT_LIKE.equals(op)
                || OP_NOT_ILIKE.equals(op);
        boolean needCast = op.endsWith(OP_LIKE);
        String column = String.format("%s.%s", tableAlias, cr.quote(field.getColumnName()));
        if (needCast) {
            column = cr.getSqlDialect().cast(column, ColumnType.Text);
        }

        List<Object> params = null;
        String query = String.format("(%s %s %%s)", column, op);

        boolean orIsNull = (needWildcard && right == null) || (right != null && NEGATIVE_TEAM_OPS.contains(op));
        if (orIsNull) {
            query = String.format("(%s OR %s.%s IS NULL)", query, tableAlias, cr.quote(field.getColumnName()));
        }

        if (needWildcard) {
            params = Arrays.asList("%" + right + "%");
        } else {
            //TODO
            params = Arrays.asList(field.getDataType().convertToColumn(right, model, false));
        }

        return new SqlPara(query, params);
    }

    @SuppressWarnings("unchecked")
    public SqlPara leafToSql(BinaryOp leaf, EntityClass model, String alias) {
        if (leaf.equals(TRUE_LEAF)) {
            return new SqlPara("TRUE", Collections.emptyList());
        }
        if (leaf.equals(FALSE_LEAF)) {
            return new SqlPara("FALSE", Collections.emptyList());
        }
        String left = (String) leaf.getField();
        EntityField field = model.getField(left);
        String op = leaf.getOp();
        Object right = leaf.getValue();
        String tableAlias = cr.quote(alias);
        if (OP_IN_SELECT.equals(op) || OP_NOT_IN_SELECT.equals(op)) {
            List<Object> r = (List<Object>) right;
            return new SqlPara(String.format("(%s.%s %s (%s))", tableAlias, cr.quote(field.getColumnName()),
                    OP_IN_SELECT.equals(op) ? "in" : "not in", r.get(0)), (List<Object>) r.get(1));
        }
        if (OP_IN.equals(op) || OP_NOT_IN.equals(op)) {
            return leafToInSql(leaf, model, left, op, right, tableAlias, cr);
        }

        boolean boolNullOrFalse = field != null && field.getDataType() instanceof DataType.BooleanField
                && ((OP_EQ.equals(op) && Objects.equals(false, right))
                || (OP_NOT_EQ.equals(op) && Objects.equals(true, right)));
        if (boolNullOrFalse) {
            String col = cr.quote(field.getColumnName());
            return new SqlPara(
                    String.format("(%s.%s IS NULL or %s.%s = false)", tableAlias, col, tableAlias, col),
                    Collections.emptyList());
        }

        boolean isNull = (Objects.equals(right, false) || right == null) && OP_EQ.equals(op);
        if (isNull) {
            return new SqlPara(
                    String.format("%s.%s IS NULL", tableAlias, cr.quote(field.getColumnName())),
                    Collections.emptyList());
        }

        boolean boolNotNullAndNotFalse = field != null && field.getDataType() instanceof DataType.BooleanField
                && ((OP_NOT_EQ.equals(op) && Objects.equals(false, right))
                || (OP_EQ.equals(op) && Objects.equals(true, right)));
        if (boolNotNullAndNotFalse) {
            String col = cr.quote(field.getColumnName());
            return new SqlPara(
                    String.format("(%s.%s IS NOT NULL and %s.%s != false)", tableAlias, col, tableAlias, col),
                    Collections.emptyList());
        }

        boolean isNotNull = (Objects.equals(right, false) || right == null) && OP_NOT_EQ.equals(op);
        if (isNotNull) {
            return new SqlPara(
                    String.format("%s.%s IS NOT NULL", tableAlias, cr.quote(field.getColumnName())),
                    Collections.emptyList());
        }

        if (OP_EQ_Q.equals(op)) {
            if (Objects.equals(right, false) || right == null) {
                return new SqlPara("TRUE", Collections.emptyList());
            } else {
                return leafToSql(new BinaryOp(left, OP_EQ, right), model, alias);
            }
        }

        if (field == null) {
            throw new EngineException(
                    String.format("条件[%s]中存在模型[%s]无效的字段[%s]", leaf, model.getName(), left));
        }

        return leafToSqlInner(leaf, model, field, left, op, right, tableAlias, cr);
    }

    List<Object> toIds(Object value, EntityClass comodel, Object left) {
        List<String> names = new ArrayList<>();
        if (value instanceof String) {
            names.add((String) value);
        } else if (value instanceof Collection<?>) {
            for (Object o : (Collection<?>) value) {
                if (o instanceof String) {
                    names.add((String) o);
                }
            }
        }
        // TODO
        if (names.size() > 0) {
            List<Object> ids = new ArrayList<>();
            return ids;
        }
        return Arrays.asList(value);
    }

    List<Object> childOfCriteria(Object left, List<Object> ids, EntityClass leftModel, String parent, String prefix) {
        List<Object> criteria = new ArrayList<>();
        // TODO
        return criteria;
    }

    List<Object> parentOfCriteria(Object left, List<Object> ids, EntityClass leftModel, String parent, String prefix) {
        List<Object> criteria = new ArrayList<>();
        // TODO
        return criteria;
    }

    List<Object> hierarchyFunc(String op, Object left, List<Object> ids, EntityClass leftModel, String parent,
                               String prefix) {
        return OP_CHILD_OF.equals(op) ? childOfCriteria(left, ids, leftModel, parent, prefix)
                : parentOfCriteria(left, ids, leftModel, parent, prefix);
    }
}
