package org.harmony.toddler.mybatis.groovy.sql;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.util.JdbcConstants;
import org.apache.ibatis.scripting.xmltags.OgnlCache;

import java.util.HashMap;

public class DruidSqlParserMain {
    public static void main(String[] args) {
        String sql = "SELECT t.id, t.name FROM user u";
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcConstants.MYSQL);
        SQLStatement statement = parser.parseSelect();
        System.out.println(statement.getChildren());
        testOgnl();
    }

    private static void testOgnl() {
        String expr = "id";
        Object context = new HashMap<>();
//        context = null; // OgnlException: source is null
//        context = Integer.valueOf(100); // NoSuchPropertyException
        Object value = OgnlCache.getValue(expr, context);
        System.out.println(value);
    }
}
