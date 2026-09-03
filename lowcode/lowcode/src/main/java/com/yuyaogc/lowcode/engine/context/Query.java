package com.yuyaogc.lowcode.engine.context;

import com.yuyaogc.lowcode.engine.plugin.activerecord.Config;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询
 */
public class Query {
    Map<String, String> tables = new HashMap<>();
    List<String> whereClause = new ArrayList<>();
    List<Object> whereParams = new ArrayList<>();
    Map<String, JoinClause> joins = new HashMap<>();
    String order;
    Integer limit;
    Integer offset;
    Config cr;

    public Map<String, String> getTables() {
        return tables;
    }

    public List<String> getWhereClause() {
        return whereClause;
    }

    public List<Object> getWhereParams() {
        return whereParams;
    }

    public Map<String, JoinClause> getJoins() {
        return joins;
    }

    public String getOrder() {
        return order;
    }

    public Query setOrder(String order) {
        this.order = order;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public Query setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getOffset() {
        return offset;
    }

    public Query setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Query(Config cr, String alias) {
        this(cr, alias, alias);
    }

    public Query(Config cr, String alias, String table) {
        this.cr = cr;
        if (StringUtils.isEmpty(table)) {
            table = alias;
        }
        tables.put(alias, table);
    }

    public void addTable(String alias) {
        addTable(alias, alias);
    }

    public void addTable(String alias, String table) {
        assert !tables.containsKey(alias) && !joins.containsKey(alias)
                : String.format("别名[%s]已经存在于[%s]", alias, this);
        tables.put(alias, table);
    }

    public void addWhere(String whereClause) {
        addWhere(whereClause, null);
    }

    public void addWhere(String whereClause, List<Object> whereParams) {
        this.whereClause.add(whereClause);
        if (whereParams != null) {
            this.whereParams.addAll(whereParams);
        }
    }

    public String join(String lhsAlias, String lhsColumn, String rhsTable, String rhsColumn, String link) {
        return join("JOIN", lhsAlias, lhsColumn, rhsTable, rhsColumn, link, null, null);
    }

    public String join(String lhsAlias, String lhsColumn, String rhsTable, String rhsColumn, String link,
                       String extra, List<Object> extraParams) {
        return join("JOIN", lhsAlias, lhsColumn, rhsTable, rhsColumn, link, extra, extraParams);
    }

    public String leftJoin(String lhsAlias, String lhsColumn, String rhsTable, String rhsColumn, String link) {

        return join("LEFT JOIN", lhsAlias, lhsColumn, rhsTable, rhsColumn, link, null, null);
    }

    public String leftJoin(String lhsAlias, String lhsColumn, String rhsTable, String rhsColumn, String link,
                           String extra, List<Object> extraParams) {

        return join("LEFT JOIN", lhsAlias, lhsColumn, rhsTable, rhsColumn, link, extra, extraParams);
    }

    public SqlClause getSql() {
        List<String> fromList = new ArrayList<>();
        for (String table : tables.keySet()) {
            fromList.add(formTable(table, tables.get(table)));
        }
        String from = StringUtils.join(fromList, ",");
        List<String> joinList = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        for (String alias : joins.keySet()) {
            JoinClause join = joins.get(alias);
            joinList.add(String.format("%s %s ON (%s)", join.kind, formTable(join.getRhs(), alias), join.condition));
            params.addAll(join.getConditionParams());
        }
        if (joinList.size() > 0) {
            from += " " + StringUtils.join(joinList, " ");
        }
        String where = StringUtils.join(whereClause, " AND ");
        params.addAll(whereParams);
        return new SqlClause(from, where, params);
    }

    public SelectClause select(String... selects) {
        return doSelect(selects.length > 0 ? StringUtils.join(selects, ",")
                : cr.quote(tables.keySet().stream().findFirst().get()) + ".id");
    }

    public SelectClause select(Collection<String> selects) {
        return doSelect(selects != null && selects.size() > 0 ? StringUtils.join(selects, ",")
                : cr.quote(tables.keySet().stream().findFirst().get()) + ".id");
    }

    SelectClause doSelect(String selects) {
        SqlClause sql = getSql();
        String queryStr = String.format("SELECT %s FROM %s", selects, sql.getFrom());
        if (StringUtils.isNotEmpty(sql.getWhere())) {
            queryStr += " WHERE " + sql.getWhere();
        }
        if (StringUtils.isNotEmpty(order)) {
            queryStr += " ORDER BY " + order;
        }
        queryStr = cr.getSqlDialect().getPaging(queryStr, limit, offset);
        return new SelectClause(queryStr, sql.getParams());
    }

    static Pattern table_name_patten = Pattern.compile("^[a-z_][a-z0-9_$]*$", Pattern.CASE_INSENSITIVE);

    String formTable(String table, String alias) {
        if (table != null && table.equals(alias)) {
            return cr.quote(alias);
        }
        Matcher matcher = table_name_patten.matcher(table);
        if (matcher.matches()) {
            return String.format("%s AS %s", cr.quote(table), cr.quote(alias));
        }
        return String.format("(%s) AS %s", cr.quote(table), cr.quote(alias));

    }

    String join(String kind, String lhsAlias, String lhsColumn, String rhsTable, String rhsColumn, String link,
                String extra, List<Object> extraParams) {
        String rhsAlias = cr.dialect.generateTableAlias(lhsAlias, link);
        if (!joins.containsKey(rhsAlias)) {
            String condition = String.format("%s.%s = %s.%s", cr.quote(lhsAlias), cr.quote(lhsColumn),
                    cr.quote(rhsAlias), cr.quote(rhsColumn));
            List<Object> conditionParams = new ArrayList<>();
            if (StringUtils.isNotEmpty(extra)) {
                condition += " AND "
                        + extra.replace("{lhs}", cr.quote(lhsAlias)).replace("{rhs}", cr.quote(rhsAlias));
                if (extraParams != null) {
                    conditionParams.addAll(extraParams);
                }
            }
            if (StringUtils.isNotEmpty(kind)) {
                joins.put(rhsAlias, new JoinClause(kind, rhsTable, condition, conditionParams));
            } else {
                tables.put(rhsAlias, rhsTable);
                addWhere(condition, conditionParams);
            }
        }
        return rhsAlias;
    }

    public class SelectClause {
        String query;
        List<Object> params;

        public SelectClause(String query, List<Object> params) {
            this.query = query;
            this.params = params;
        }

        public String getQuery() {
            return query;
        }

        public List<Object> getParams() {
            return params;
        }
    }

    public class SqlClause {
        String from;
        String where;
        List<Object> params;

        public SqlClause(String from, String where, List<Object> params) {
            this.from = from;
            this.where = where;
            this.params = params;
        }

        public String getFrom() {
            return from;
        }

        public String getWhere() {
            return where;
        }

        public List<Object> getParams() {
            return params;
        }
    }

    public class JoinClause {
        String kind;
        String rhs;
        String condition;
        List<Object> conditionParams;

        public JoinClause(String kind, String rhsTable, String condtion, List<Object> conditionParams) {
            this.kind = kind;
            this.rhs = rhsTable;
            this.condition = condtion;
            this.conditionParams = conditionParams;
        }

        public String getKind() {
            return kind;
        }

        public String getRhs() {
            return rhs;
        }

        public String getCondition() {
            return condition;
        }

        public List<Object> getConditionParams() {
            return conditionParams;
        }
    }
}
