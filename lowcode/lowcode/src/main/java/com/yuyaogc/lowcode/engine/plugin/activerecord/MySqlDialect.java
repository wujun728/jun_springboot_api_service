package com.yuyaogc.lowcode.engine.plugin.activerecord;

import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Query;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 */
public class MySqlDialect extends SqlDialect {

    @Override
    public String getNowUtc() {
        return "UTC_TIMESTAMP()";
    }

    @Override
    public void createColumn(Config cr, String table, String name, String columnType, String comment, boolean notNull) {
        String sql = String.format("ALTER TABLE `%s` ADD COLUMN `%s` %s %s", table, name, columnType,
                notNull ? "NOT NULL" : "NULL");
        if (StringUtils.isNotEmpty(comment)) {
            sql += String.format(" COMMENT '%s'", comment.replace("'", "''"));
        }


        Connection conn;

        try {
            conn = cr.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cr.close(conn);
        }

        schema.debug("Table {} added column {} of type {} {}", table, name, columnType, notNull ? "NOT NULL" : "NULL");
    }


    @Override
    public void modifyColumn(Config cr, String table, String name, String columnType, String comment, boolean notNull,
                             Object defaultValue) {

        String sql = String.format("ALTER TABLE `%s` MODIFY COLUMN `%s` %s", table, name, columnType);
        if (StringUtils.isNotEmpty(comment)) {
            sql += String.format(" COMMENT '%s'", comment.replace("'", "''"));
        }


        Connection conn;

        try {
            conn = cr.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cr.close(conn);
        }

        schema.info("Table {} modify column {} of type {} {}", table, name, columnType, notNull ? "NOT NULL" : "NULL");
    }

    @Override
    public String getColumnType(ColumnType type) {
        switch (type) {
            case Boolean:
                return "bit";
            case VarChar:
                return "varchar";
            case Text:
                return "mediumtext";
            case Binary:
                return "mediumblob ";
            case Integer:
                return "int";
            case Long:
                return "bigint";
            case Float:
                return "double";
            case Date:
                return "date";
            case DateTime:
                return "timestamp";
            case Decimal:
                return "decimal";
            default:
                return null;
        }
    }

    @Override
    public String getColumnType(ColumnType type, Integer length, Integer precision) {
        String result = getColumnType(type);
        if (length != null && length > 0) {
            result += "(" + length + ")";
        }
        return result;
    }

    @Override
    public boolean tableExists(Config cr, String table) {
        return existingTables(cr, Arrays.asList(table)).size() == 1;
    }

    @Override
    public List<String> existingTables(Config cr, List<String> tableNames) {
        String sql = "select TABLE_NAME, TABLE_TYPE from information_schema.tables"
                + " where TABLE_TYPE='BASE TABLE' AND TABLE_SCHEMA=(select database()) AND TABLE_NAME in (%s)";

        SqlPara format = cr.mogrify(sql, tableNames);

        Connection conn;

        try {
            conn = cr.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<String> result = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(format.getSql())) {
            cr.dialect.fillStatement(pst, format.getParmas());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cr.close(conn);
        }

        return result;
    }

    @Override
    public void createModelTable(Config cr, String table, String comment) {
        String sql = String.format("CREATE TABLE IF NOT EXISTS `%s` (`id` bigint(20) NOT NULL, PRIMARY KEY(`id`))", table);
        if (StringUtils.isNotEmpty(comment)) {
            sql += String.format("COMMENT = '%s'", comment.replace("'", "''"));
        }

        Connection conn;

        try {
            conn = cr.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cr.close(conn);
        }

        schema.debug("Table {}: created", table);
    }

    @Override
    public Map<String, Column> tableColumns(Config cr, String table) {
        String sql = "SELECT column_name, CASE WHEN  left(COLUMN_TYPE,LOCATE('(',COLUMN_TYPE)-1)='' THEN COLUMN_TYPE ELSE left(COLUMN_TYPE,LOCATE('(',COLUMN_TYPE)-1) END AS COLUMN_TYPE, CAST(SUBSTRING(COLUMN_TYPE,LOCATE('(',COLUMN_TYPE)+1,LOCATE(')',COLUMN_TYPE)-LOCATE('(',COLUMN_TYPE)-1) AS signed) AS LENGTH, IS_NULLABLE"
                + " FROM Information_schema.columns where TABLE_SCHEMA=(select database()) and TABLE_NAME = %s";



        SqlPara format = cr.mogrify(sql, Arrays.asList(table));

        Connection conn;

        try {
            conn = cr.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Map<String, Column> result = new HashMap<>();

        try (PreparedStatement pst = conn.prepareStatement(format.getSql())) {
            cr.dialect.fillStatement(pst, format.getParmas());

            ResultSet resultSet = pst.executeQuery();
            ResultSetMetaData meta = resultSet.getMetaData();

            List<Object[]> list = new ArrayList<>();
            while (resultSet.next()){
                Object[] values = new Object[meta.getColumnCount()];
                for (int i = 0; i < values.length; i++) {
                    values[i] = resultSet.getObject(i + 1);
                }
                list.add(values);
            }

            for (Object[] row : list) {
                result.put((String) row[0],
                        new Column((String) row[0], (String) row[1], Integer.valueOf(row[2].toString()),
                                "YES".equals((String) row[3])));
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cr.close(conn);
        }

        return result;
    }

    @Override
    public String quote(String identify) {
        return String.format("`%s`", identify);
    }

    @Override
    public String getPaging(String sql, Integer limit, Integer offset) {
        if (limit != null && limit > 0) {
            sql += " LIMIT " + limit;
        }
        if (offset != null && offset > 0) {
            sql += " OFFSET " + offset;
        }
        return sql;
    }

    @Override
    public String cast(String column, ColumnType type) {
        return column;
    }

    @Override
    public String addUniqueConstraint(Config cr, String table, String constraint, String[] fields) {
        try {
            String definition = String.format("unique(%s)", StringUtils.join(fields, ','));
            String oldDefinition = getConstraintDefinition(cr, constraint);
            if (oldDefinition != null) {
                if (!definition.equals(oldDefinition)) {
                    dropConstraint(cr, table, constraint);
                } else {
                    return null;
                }
            }
            String sql = String.format("ALTER TABLE %s ADD CONSTRAINT %s %s", quote(table), quote(constraint),
                    definition);


            Connection conn;

            try {
                conn = cr.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                cr.close(conn);
            }

            schema.debug("Table {} add unique constaint {} as {}", table, constraint, definition);
            return definition;
        } catch (Exception exc) {
            schema.warn("表{}添加唯一约束{}失败:{}", table, constraint, exc.getMessage());
            return null;
        }
    }

    @Override
    public void dropConstraint(Config cr, String table, String constraint) {
        try {
            // TODO savepoint

            String sql =String.format("ALTER TABLE %s DROP CONSTRAINT %s", quote(table), quote(constraint));

            Connection conn;

            try {
                conn = cr.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                cr.close(conn);
            }

            schema.debug("Table {}: dropped constraint {}", table, constraint);
        } catch (Exception e) {
            schema.warn("Table {}: unable to drop constraint {}", table, constraint);
        }
    }

    String getConstraintDefinition(Config cr, String constraint) {
        String sql = "SELECT definition FROM ir_model_constraint where name=%s";

        return null;
    }

    Pattern constraintPattern = Pattern.compile("CONSTRAINT `(?<name>\\S+)` FOREIGN KEY");

    @Override
    public RuntimeException getError(SQLException err, SqlPara sql) {
        System.out.println(sql.getSql());
        if (err.getErrorCode() == 1451) {
            String msg = err.getMessage();
            Matcher m = constraintPattern.matcher(msg);
            if (m.find()) {
                String constraint = m.group("name");
                throw new EngineException(constraint);
            }
            throw new EngineException("数据被引用,不能删除", err);
        }
        return new EngineException(String.format("执行SQL[%s]失败", sql.getSql()), err);
    }

    @Override
    public void forModelSave(EntityClass table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {
        sql.append("insert into `").append(table.getTableName()).append("`(");
        StringBuilder temp = new StringBuilder(") values(");
        Iterator var6 = attrs.entrySet().iterator();

        while (var6.hasNext()) {
            Map.Entry<String, Object> e = (Map.Entry) var6.next();
            String colName = e.getKey();
            EntityField entityField = table.getField(colName);

            if(entityField == null){
                continue;
            }

            if(Constants.MANY2MANY.equals(entityField.getDataType().getName())
                    || Constants.ONE2MANY.equals(entityField.getDataType().getName())){
                continue;
            }

            if (table.hasColumnLabel(colName)) {
                if (paras.size() > 0) {
                    sql.append(", ");
                    temp.append(", ");
                }
                sql.append('`').append(entityField.getColumnName()).append('`');
                temp.append('?');
                paras.add(entityField.getDataType().convertToRead(e.getValue(), Context.getInstance()));
            }
        }

        sql.append(temp).append(')');
    }

    @Override
    public String forModelFindById(EntityClass table, String columns) {
        StringBuilder sql = new StringBuilder("select ");
        columns = columns.trim();
        if ("*".equals(columns)) {
            //sql.append('*');

            List<EntityField> fields = new ArrayList<>(table.getFields());
            for (int i=0 ;i <fields.size();i++) {
                if (i > 0) {
                    sql.append(',');
                }
                sql.append('`').append(fields.get(i).getColumnName()).append('`').append(" as ").append('`').append(fields.get(i).getName()).append('`');
            }

        } else {
            String[] arr = columns.split(",");
            for (int i = 0; i < arr.length; i++) {
                if (i > 0) {
                    sql.append(',');
                }
                sql.append('`').append(arr[i].trim()).append('`');
            }
        }

        sql.append(" from `");
        sql.append(table.getTableName());
        sql.append("` where ");
        String[] pKeys = table.getPrimaryKey();
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            sql.append('`').append(pKeys[i]).append("` = ?");
        }
        return sql.toString();
    }

    @Override
    public String forModelDeleteById(EntityClass table) {
        String[] pKeys = table.getPrimaryKey();
        StringBuilder sql = new StringBuilder(45);
        sql.append("delete from `");
        sql.append(table.getTableName());
        sql.append("` where ");
        for (int i=0; i<pKeys.length; i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            sql.append('`').append(pKeys[i]).append("` = ?");
        }
        return sql.toString();
    }

    @Override
    public void setNotNull(Config cr, String table, String column, String columnType) {
        String sql = String.format("ALTER TABLE %s MODIFY %s %s NOT NULL", quote(table), quote(column), columnType);
        // TODO savepoint
        Connection conn;

        try {
            conn = cr.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cr.close(conn);
        }
        schema.debug("Table {}: column {}: added constraint NOT NULL", table, column);
    }

    @Override
    public void dropNotNull(Config cr, String table, String column, String columnType) {
        String sql = String.format("ALTER TABLE %s MODIFY %s %s NULL", quote(table), quote(column), columnType);
        // TODO savepoint
        Connection conn;

        try {
            conn = cr.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cr.close(conn);
        }
        schema.debug("Table {}: column {}: dropped constraint NOT NULL", table, column);
    }

    @Override
    public void createM2MTable(Config cr, String table, String column1, String column2, String comment) {
        table = quote(table);
        column1 = quote(column1);
        column2 = quote(column2);
        String sql = "CREATE TABLE " + table + " (" + column1 + " VARCHAR(13) NOT NULL, " + column2
                + " VARCHAR(13) NOT NULL, PRIMARY KEY(" + column1 + "," + column2 + "))"
                + " COMMENT = '" + comment + "'";
        Connection conn;

        try {
            conn = cr.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cr.close(conn);
        }
        schema.debug("Create table %s: %s", table, comment);
    }

    @Override
    public List<Object[]> getForeignKeys(Config cr, Collection<String> tables) {
        String sql = "SELECT k.constraint_name, k.table_name, k.column_name, k.referenced_table_name, k.referenced_column_name, s.definition"
                + " FROM information_schema.key_column_usage k"
                + " LEFT JOIN ir_model_constraint s on k.constraint_name=s.name"
                + " WHERE k.referenced_table_name IN %s AND k.table_schema=DATABASE()";

        return null;
    }

    @Override
    public String addForeignKey(Config cr, String table1, String column1, String table2, String column2,
                                String ondelete) {
        String fk = String.format("fk_%s_%s", table1, column1);
        String sql = String.format("ALTER TABLE %s"
                        + " ADD CONSTRAINT %s"
                        + " FOREIGN KEY (%s)"
                        + " REFERENCES %s (%s)"
                        + " ON DELETE %s",
                cr.quote(table1), cr.quote(fk), cr.quote(column1), cr.quote(table2), cr.quote(column2), ondelete);


        Connection conn;

        try {
            conn = cr.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cr.close(conn);
        }

        return fk;
    }

    @Override
    public void forModelUpdate(EntityClass table, Map<String, Object> attrs, Set<String> modifyFlag, StringBuilder sql, List<Object> paras) {
        sql.append("update `").append(table.getTableName()).append("` set ");
        String[] pKeys = table.getPrimaryKey();
        for (Map.Entry<String, Object> e : attrs.entrySet()) {
            String colName = e.getKey();

            EntityField field =  table.getField(colName);
            if(field == null){
                continue;
            }

            if(Constants.MANY2MANY.equals(field.getDataType().getName())
                    || Constants.ONE2MANY.equals(field.getDataType().getName())){
                continue;
            }


            String columnName = field.getColumnName();

            if (!isPrimaryKey(colName, pKeys) && table.hasColumnLabel(colName)) {
                if (paras.size() > 0) {
                    sql.append(", ");
                }
                sql.append('`').append(columnName).append("` = ? ");
                paras.add(field.getDataType().convertToRead(e.getValue(),Context.getInstance()));
            }
        }
        sql.append(" where ");
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            sql.append('`').append(pKeys[i]).append("` = ?");
            paras.add(attrs.get(pKeys[i]));
        }
    }

}
