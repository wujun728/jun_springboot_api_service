package org.harmony.toddler.mybatis.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Slf4j
public class SqlParserUtil {

    public static String toCamelAlias(String originalSql) {
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(originalSql);
        } catch (JSQLParserException e) {
            log.warn(e.getMessage(), e);
        }
        if (statement instanceof Select) {
            String sql = select2CamelAlias((Select) statement);
            if (StringUtils.isNotEmpty(sql)) {
                return sql;
            }
        }
        return originalSql;

    }

    private static String select2CamelAlias(Select select) {
        if (select.getSelectBody() instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            for (SelectItem selectItem : plainSelect.getSelectItems()) {
                camelAlias(selectItem);
            }
            return plainSelect.toString();
        }
        return null;
    }

    private static String camelAlias(SelectItem selectItem) {
        String columnName;
        if (selectItem instanceof SelectExpressionItem) {
            SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;

            Alias alias = selectExpressionItem.getAlias();

            Expression expression = selectExpressionItem.getExpression();
            if (alias != null) {
                if (expression instanceof CaseExpression) {
                    // case表达式
                    columnName = alias.getName();
                } else if (expression instanceof LongValue || expression instanceof StringValue || expression instanceof DateValue || expression instanceof DoubleValue) {
                    // 值表达式
                    columnName = Objects.nonNull(alias.getName()) ? alias.getName() : expression.getASTNode().jjtGetValue().toString();
                } else if (expression instanceof TimeKeyExpression) {
                    // 日期
                    columnName = alias.getName();
                } else {
                    columnName = alias.getName();
                }
            } else {
                SimpleNode node = expression.getASTNode();
                Object value = node.jjtGetValue();
                if (value instanceof Column) {
                    columnName = ((Column) value).getColumnName();
                    Alias a = new Alias(snake2Camel(columnName), true);
                    selectExpressionItem.setAlias(a);
                } else if (value instanceof Function) {
                    columnName = value.toString();
                } else {
                    // 增加对select 'aaa' from table; 的支持
                    columnName = String.valueOf(value);
                    columnName = columnName.replace("'", "");
                    columnName = columnName.replace("\"", "");
                    columnName = columnName.replace("`", "");
                }
            }

            columnName = columnName.replace("'", "");
            columnName = columnName.replace("\"", "");
            columnName = columnName.replace("`", "");

        } else if (selectItem instanceof AllTableColumns) {
            AllTableColumns allTableColumns = (AllTableColumns) selectItem;
            columnName = allTableColumns.toString();
        } else {
            columnName = selectItem.toString();
        }
        return columnName;
    }

    private static String snake2Camel(String fieldName) {
        if (fieldName == null || "".equals(fieldName.trim())) {
            return "";
        }
        int len = fieldName.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = fieldName.charAt(i);
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(fieldName.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
