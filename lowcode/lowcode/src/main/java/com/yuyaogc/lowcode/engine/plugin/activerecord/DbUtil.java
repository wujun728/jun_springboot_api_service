package com.yuyaogc.lowcode.engine.plugin.activerecord;

import java.sql.*;
import java.util.List;

public class DbUtil {
    public static int update(Config config, Connection conn, String sql, List<Object> paras) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            config.dialect.fillStatement(pst, paras);
            int result = pst.executeUpdate();
            return result;
        }
    }

    public static int update(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            config.dialect.fillStatement(pst, paras);
            int result = pst.executeUpdate();
            return result;
        }
    }

    static final void close(ResultSet rs, Statement st) throws SQLException {
        if (rs != null) {rs.close();}
        if (st != null) {st.close();}
    }

    static final void close(ResultSet rs) throws SQLException {
        if (rs != null) {rs.close();}
    }

    static final void close(Statement st) throws SQLException {
        if (st != null) {st.close();}
    }
}
