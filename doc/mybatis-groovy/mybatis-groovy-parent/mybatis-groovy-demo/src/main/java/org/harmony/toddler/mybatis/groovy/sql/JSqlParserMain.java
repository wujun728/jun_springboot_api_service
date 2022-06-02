package org.harmony.toddler.mybatis.groovy.sql;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;

import java.util.List;
import java.util.Objects;

public class JSqlParserMain {
    public static void main(String[] args) {
        String originalSql = "SELECT t.id,  /* 注释 */  t.name FROM user u WHERE t.id IN (?, ?, ?, ?) order by id desc LIMIT ?";
        try {
            Select selectStatement = (Select) CCJSqlParserUtil.parse(originalSql);
            List orderByElements;
            List orderByElementsReturn;
            if (selectStatement.getSelectBody() instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
                for (SelectItem selectItem : plainSelect.getSelectItems()) {
                    camelAlias(selectItem);
                    System.out.println(selectItem.getClass());
                }
                plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), CCJSqlParserUtil.parseCondExpression(" a = 1 and b = 2")));
                System.out.println(plainSelect);
                System.out.println(plainSelect.toString());
            }

            if (selectStatement.getSelectBody() instanceof SetOperationList) {
                SetOperationList setOperationList = (SetOperationList) selectStatement.getSelectBody();
                orderByElements = setOperationList.getOrderByElements();
//                orderByElementsReturn = addOrderByElements(orderList, orderByElements);
//                setOperationList.setOrderByElements(orderByElementsReturn);
//                return setOperationList.toString();
            }

            if (selectStatement.getSelectBody() instanceof WithItem) {
//                return originalSql;
            }

//            return originalSql;
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    private static void camelAlias(SelectItem selectItem) {
        String columnName = "";
        if (selectItem instanceof SelectExpressionItem) {
            SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;

            Alias alias = selectExpressionItem.getAlias();

            Expression expression = selectExpressionItem.getExpression();
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
                if (alias != null) {
                    columnName = alias.getName();
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
        System.out.printf("%s => %s\n", columnName, snake2Camel(columnName));
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
