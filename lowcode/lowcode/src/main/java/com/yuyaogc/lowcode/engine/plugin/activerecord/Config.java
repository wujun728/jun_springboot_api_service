package com.yuyaogc.lowcode.engine.plugin.activerecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Config  {
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    private static Logger logger = LoggerFactory.getLogger(Config.class);
    /**
     * decent limit on size of IN queries - guideline = Oracle limit
     */
    static int IN_MAX = 1000;
    public SqlDialect dialect;
    DataSource dataSource;
    int transactionLevel;
    String name;
    boolean showSql;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    protected Config(SqlDialect dialect) {
        this.dialect = dialect;
    }

    public Config(String name, DataSource ds, SqlDialect dialect) {
        this.name = name;
        dataSource = ds;
        this.dialect = dialect;

    }

    void setTransactionLevel(int transactionLevel) {
        int t = transactionLevel;
        if (t != 0 && t != 1  && t != 2  && t != 4  && t != 8) {
            throw new IllegalArgumentException("The transactionLevel only be 0, 1, 2, 4, 8");
        }
        this.transactionLevel = transactionLevel;
    }

    public SqlDialect getSqlDialect() {
        return dialect;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn != null)
            return conn;
        return showSql ? new SqlReporter(dataSource.getConnection()).getConnection() : dataSource.getConnection();
    }

    public SqlPara mogrify(String sql, Collection<?> params) {
        List<Object> args = new ArrayList<>();
        for (Object p : params) {
            if (p instanceof Collection<?>) {
                Collection<?> col = (Collection<?>) p;
                String v = col.stream().map(c -> "?").collect(Collectors.joining(","));
                sql = sql.replaceFirst("%s", "(" + v + ")");
                args.addAll(col);
            } else {
                sql = sql.replaceFirst("%s", "?");
                args.add(p);
            }
        }
        return new SqlPara(sql, args);
    }







    static Boolean printSql;

    boolean isPrintSql() {
        if (printSql == null) {
            printSql = false;
        }
        return printSql;
    }

    /**
     * 设置是否打印sql
     *
     * @param isPrint
     */
    public static void setPrintSql(boolean isPrint) {
        printSql = isPrint;
    }

    public String quote(String identify) {
        return dialect.quote(identify);
    }


    public void setThreadLocalConnection(Connection connection) {
        threadLocal.set(connection);
    }


    public void removeThreadLocalConnection() {
        threadLocal.remove();
    }

    public Connection getThreadLocalConnection() {
        return threadLocal.get();
    }

    public boolean isInTransaction() {
        return threadLocal.get() != null;
    }

    public List<Object[]> splitForInConditions(Object[] ids) {
        List<Object[]> result = new ArrayList<>();
        if (ids.length < IN_MAX) {
            result.add(ids);
        } else {
            Integer from = 0;
            while (from < ids.length) {
                Integer to = from + IN_MAX;
                if (to > ids.length) {
                    to = ids.length;
                }
                result.add(Arrays.copyOfRange(ids, from, to));
                from += IN_MAX;
            }
        }
        return result;
    }

    public void close(Statement st, Connection conn) {
        if (st != null) {try {st.close();} catch (SQLException e) {System.err.println(e.getMessage());}}

        if (threadLocal.get() == null) {	// in transaction if conn in threadlocal
            if (conn != null) {try {conn.close();}
            catch (SQLException e) {throw new ActiveRecordException(e);}}
        }
    }

    public void close(ResultSet rs, Statement st, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (threadLocal.get() == null) {    // in transaction if conn in threadlocal
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new ActiveRecordException(e);
                }
            }
        }
    }



    public void close(Connection conn) {
        if (threadLocal.get() == null)        // in transaction if conn in threadlocal
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new ActiveRecordException(e);
                }
    }
}
