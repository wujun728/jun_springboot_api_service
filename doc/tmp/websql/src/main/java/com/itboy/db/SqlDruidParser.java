package com.itboy.db;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SqlDruidParser
 * @Description sql解析器
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/24 0024 10:16
 **/
@Slf4j
public class SqlDruidParser {

    public static Map<String, Object> sqlParser(String dbType, String sql) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        dbType = jdbcConstants(dbType);
        String result = com.itboy.db.SQLUtils.format(sql, dbType);
        String[] sqlPaream = result.split(";");
        List<String> sqlList = Arrays.asList(sqlPaream);
        resultMap.put("executeSql", sqlList);
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        SQLStatement stmt = stmtList.get(0);
        SchemaStatVisitor visitor = null;
        if (dbType == "oracle") {
            visitor = new OracleSchemaStatVisitor();
        } else if (dbType == "mysql") {
            visitor = new MySqlSchemaStatVisitor();
        }
        stmt.accept(visitor);
        Object method = getFirstOrNull(visitor.getTables());
        resultMap.put("tables", visitor.getCurrentTable());
        resultMap.put("fields", visitor.getColumns());
        resultMap.put("tableName", getTable(visitor.getTables()));
        resultMap.put("sqlContent", stmt);
        if (method != null) {
            resultMap.put("executeType", method.toString().toUpperCase());
        } else {
            resultMap.put("executeType", "SELECT");
        }
        return resultMap;
    }

    private static String jdbcConstants(String dbType) {

        String dsName = DataSourceFactory.getDbType(dbType);
        if (dsName == null) {
            throw new NullPointerException(dbType + "未获取到有效数据源");
        }
        switch (dsName) {
            case "mysql":
                dbType = JdbcConstants.MYSQL;
                break;
            case "oracle":
                dbType = JdbcConstants.ORACLE;
                break;
            default:
                return "";
        }
        return dbType;
    }

    private static Object getFirstOrNull(Map<TableStat.Name, TableStat> map) {
        Object obj = null;
        for (Map.Entry<TableStat.Name, TableStat> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }

    private static Object getTable(Map<TableStat.Name, TableStat> map) {
        String tableName = null;
        for (Map.Entry<TableStat.Name, TableStat> entry : map.entrySet()) {
            TableStat.Name keys = entry.getKey();
            tableName = keys.getName();
            if (tableName != null) {
                break;
            }
        }
        return tableName;
    }


}
