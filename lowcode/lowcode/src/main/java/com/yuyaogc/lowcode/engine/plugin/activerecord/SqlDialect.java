package com.yuyaogc.lowcode.engine.plugin.activerecord;

import com.yuyaogc.lowcode.engine.entity.EntityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public abstract class SqlDialect {
    protected ModelBuilder modelBuilder = ModelBuilder.me;
    /**
     * 数据库标识符最大长度
     */
    public static final int IDENTITY_MAX_LENGTH = 63;
    public static Logger schema = LoggerFactory.getLogger("sql.schema");

    /**
     * 获取数据库时间
     *
     * @return
     */
    abstract String getNowUtc();

    public abstract void forModelUpdate(EntityClass table, Map<String, Object> attrs, Set<String> modifyFlag, StringBuilder sql, List<Object> paras);

    public <T> List<T> buildModelList(ResultSet rs, Class<? extends Model> modelClass) throws SQLException, ReflectiveOperationException {
        return modelBuilder.build(rs, modelClass);
    }

    public String forFindAll(String tableName) {
        return "select * from " + tableName;
    }

    /**
     * 生成别名
     *
     * @param srcTableAlias
     * @param link
     * @return
     */
    public String generateTableAlias(String srcTableAlias, String link) {
        String alias = srcTableAlias + "__" + link;
        if (alias.length() > IDENTITY_MAX_LENGTH) {
            CRC32 crc = new CRC32();
            crc.update(alias.getBytes(Charset.forName("UTF-8")));
            alias = alias.substring(0, IDENTITY_MAX_LENGTH - 10) + "_" + Long.toString(crc.getValue(), 32);
        }
        return alias;
    }

    public boolean isOracle() {
        return false;
    }

    /**
     * 创建字段
     *
     * @param cr
     * @param table
     * @param name
     * @param columnType
     * @param comment
     * @param notNull
     */
    public abstract void createColumn(Config cr, String table, String name, String columnType, String comment, boolean notNull);

    /**
     * 根据{@link ColumnType}转换数据库字段类型
     *
     * @param type
     * @return
     */
    public abstract String getColumnType(ColumnType type);


    public abstract void modifyColumn(Config da, String table, String name, String columnType, String comment, boolean notNull,
                                      Object defaultValue);

    /**
     * 根据{@link ColumnType}转换数据库字段类型
     *
     * @param type
     * @param length
     * @param precision
     * @return
     */
    public abstract String getColumnType(ColumnType type, Integer length, Integer precision);

    /**
     * 判断指定的表名是否存在
     *
     * @param cr
     * @param table
     * @return
     */
    public abstract boolean tableExists(Config cr, String table);

    /**
     * 判断指定的表名是否存在
     *
     * @param cr
     * @param tableNames
     * @return 存在的表名
     */
    public abstract List<String> existingTables(Config cr, List<String> tableNames);

    /**
     * 创建模型的表
     *
     * @param cr
     * @param table
     * @param comment
     */
    public abstract void createModelTable(Config cr, String table, String comment);

    /**
     * 创建多对多中间表
     *
     * @param cr
     * @param table
     * @param column1
     * @param column2
     * @param comment
     */
    public abstract void createM2MTable(Config cr, String table, String column1, String column2, String comment);

    /**
     * 获取指定表的字段
     *
     * @param cr
     * @param table
     * @return
     */
    public abstract Map<String, Column> tableColumns(Config cr, String table);

    /**
     * 为数据库标识符添加分隔符：如oracle:"identify", sqlserver:[identify], mysql: `identify`
     *
     * @param identify
     * @return
     */
    public abstract String quote(String identify);

    /**
     * 生成分页sql
     *
     * @param sql
     * @param limit
     * @param offset
     * @return
     */
    public abstract String getPaging(String sql, Integer limit, Integer offset);

    /**
     * 数据类型转换
     *
     * @param column
     * @param type
     * @return
     */
    public abstract String cast(String column, ColumnType type);

    /**
     * 添加唯一约束
     *
     * @param cr
     * @param table
     * @param constraint
     * @param fields
     */
    public abstract String addUniqueConstraint(Config cr, String table, String constraint, String[] fields);

    /**
     * 设置非空
     *
     * @param cr
     * @param table
     * @param column
     * @param columnType
     */
    public abstract void setNotNull(Config cr, String table, String column, String columnType);

    /**
     * 设置可空
     *
     * @param cr
     * @param table
     * @param column
     * @param columnType
     */
    public abstract void dropNotNull(Config cr, String table, String column, String columnType);

    /**
     * 获取指定表的外键信息
     *
     * @param cr
     * @param tables
     * @return
     */
    public abstract List<Object[]> getForeignKeys(Config cr, Collection<String> tables);

    /**
     * 添加外键
     *
     * @param cr
     * @param table1
     * @param column1
     * @param table2
     * @param column2
     * @param ondelete
     * @return
     */
    public abstract String addForeignKey(Config cr, String table1, String column1, String table2, String column2, String ondelete);

    /**
     * 删除约束
     *
     * @param cr
     * @param table
     * @param name
     */
    public abstract void dropConstraint(Config cr, String table, String name);

    /**
     * 处理异常
     *
     * @param err
     * @param sql
     * @return
     */
    public abstract RuntimeException getError(SQLException err, SqlPara sql);


    public abstract void forModelSave(EntityClass table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras);


    public abstract String forModelFindById(EntityClass table, String columns);

    public void getModelGeneratedKey(Model<?> model, PreparedStatement pst, EntityClass table) throws SQLException {
        String[] pKeys = table.getPrimaryKey();
        ResultSet rs = pst.getGeneratedKeys();
        String[] var6 = pKeys;
        int var7 = pKeys.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            String pKey = var6[var8];
            if ((model.get(pKey) == null || this.isOracle()) && rs.next()) {
                Class<?> colType = table.getColumnType(pKey);
                if (colType != null) {
                    if (colType != Integer.class && colType != Integer.TYPE) {
                        if (colType != Long.class && colType != Long.TYPE) {
                            if (colType == BigInteger.class) {
                                this.processGeneratedBigIntegerKey(model, pKey, rs.getObject(1));
                            } else {
                                model.set(pKey, rs.getObject(1));
                            }
                        } else {
                            model.set(pKey, rs.getLong(1));
                        }
                    } else {
                        model.set(pKey, rs.getInt(1));
                    }
                }
            }
        }

        rs.close();
    }

    protected void processGeneratedBigIntegerKey(Model<?> model, String pKey, Object v) {
        if (v instanceof BigInteger) {
            model.set(pKey, (BigInteger) v);
        } else if (v instanceof Number) {
            Number n = (Number) v;
            model.set(pKey, BigInteger.valueOf(n.longValue()));
        } else {
            model.set(pKey, v);
        }

    }


    public void fillStatement(PreparedStatement pst, List<Object> paras) throws SQLException {
        int i = 0;

        for (int size = paras.size(); i < size; ++i) {
            pst.setObject(i + 1, paras.get(i));
        }

    }

    public void fillStatement(PreparedStatement pst, Object... paras) throws SQLException {
        for (int i = 0; i < paras.length; ++i) {
            pst.setObject(i + 1, paras[i]);
        }

    }

    public String getDefaultPrimaryKey() {
        return "id";
    }

    public boolean isPrimaryKey(String colName, String[] pKeys) {
        String[] var3 = pKeys;
        int var4 = pKeys.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String pKey = var3[var5];
            if (colName.equalsIgnoreCase(pKey)) {
                return true;
            }
        }

        return false;
    }

    public abstract String forModelDeleteById(EntityClass var1);

    public void trimPrimaryKeys(String[] pKeys) {
        for (int i = 0; i < pKeys.length; ++i) {
            pKeys[i] = pKeys[i].trim();
        }

    }

    public String replaceOrderBy(String sql) {
        return SqlDialect.Holder.ORDER_BY_PATTERN.matcher(sql).replaceAll("");
    }

    protected void fillStatementHandleDateType(PreparedStatement pst, List<Object> paras) throws SQLException {
        int i = 0;

        for (int size = paras.size(); i < size; ++i) {
            Object value = paras.get(i);
            if (value instanceof Date) {
                if (value instanceof java.sql.Date) {
                    pst.setDate(i + 1, (java.sql.Date) value);
                } else if (value instanceof Timestamp) {
                    pst.setTimestamp(i + 1, (Timestamp) value);
                } else {
                    Date d = (Date) value;
                    pst.setTimestamp(i + 1, new Timestamp(d.getTime()));
                }
            } else {
                pst.setObject(i + 1, value);
            }
        }

    }

    protected void fillStatementHandleDateType(PreparedStatement pst, Object... paras) throws SQLException {
        for (int i = 0; i < paras.length; ++i) {
            Object value = paras[i];
            if (value instanceof Date) {
                if (value instanceof java.sql.Date) {
                    pst.setDate(i + 1, (java.sql.Date) value);
                } else if (value instanceof Timestamp) {
                    pst.setTimestamp(i + 1, (Timestamp) value);
                } else {
                    Date d = (Date) value;
                    pst.setTimestamp(i + 1, new Timestamp(d.getTime()));
                }
            } else {
                pst.setObject(i + 1, value);
            }
        }

    }

    public String forPaginateTotalRow(String select, String sqlExceptSelect, Object ext) {
        return "select count(*) " + this.replaceOrderBy(sqlExceptSelect);
    }

    protected static class Holder {
        private static final Pattern ORDER_BY_PATTERN = Pattern.compile("order\\s+by\\s+[^,\\s]+(\\s+asc|\\s+desc)?(\\s*,\\s*[^,\\s]+(\\s+asc|\\s+desc)?)*", 10);

        protected Holder() {
        }
    }
}
