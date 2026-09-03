package com.gitee.freakchicken.dbapi.util;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JdbcUtilTest {

    private Connection mockConn;
    private PreparedStatement mockPst;
    private ResultSet mockRs;
    private ResultSetMetaData mockMeta;

    @Before
    public void setUp() throws Exception {
        mockConn = mock(Connection.class);
        mockPst = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);
        mockMeta = mock(ResultSetMetaData.class);
    }

    // ===== validateTableName (via getRDBMSColumnProperties, caught by catch block -> returns null) =====

    @Test
    public void testValidateTableName_null_returnsNull() {
        assertNull(JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", null));
    }

    @Test
    public void testValidateTableName_empty_returnsNull() {
        assertNull(JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", ""));
    }

    @Test
    public void testValidateTableName_sqlInjection_returnsNull() {
        assertNull(JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", "users; DROP TABLE users--"));
    }

    @Test
    public void testValidateTableName_specialChars_returnsNull() {
        assertNull(JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", "table'name"));
    }

    @Test
    public void testValidateTableName_tooLong_returnsNull() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 130; i++) sb.append('a');
        assertNull(JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", sb.toString()));
    }

    // ===== query() =====

    @Test
    public void testQuery_returnsResults() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenReturn(mockRs);
        when(mockRs.getMetaData()).thenReturn(mockMeta);
        when(mockMeta.getColumnCount()).thenReturn(2);
        when(mockMeta.getColumnLabel(1)).thenReturn("id");
        when(mockMeta.getColumnLabel(2)).thenReturn("name");
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getObject("id")).thenReturn(1);
        when(mockRs.getObject("name")).thenReturn("test");

        List<JSONObject> result = JdbcUtil.query("SELECT * FROM t", mockConn);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).get("id"));
        assertEquals("test", result.get(0).get("name"));
    }

    @Test
    public void testQuery_emptyResult() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenReturn(mockRs);
        when(mockRs.getMetaData()).thenReturn(mockMeta);
        when(mockMeta.getColumnCount()).thenReturn(1);
        when(mockMeta.getColumnLabel(1)).thenReturn("id");
        when(mockRs.next()).thenReturn(false);

        List<JSONObject> result = JdbcUtil.query("SELECT * FROM t", mockConn);

        assertTrue(result.isEmpty());
    }

    @Test(expected = SQLException.class)
    public void testQuery_sqlException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("bad sql"));

        JdbcUtil.query("INVALID SQL", mockConn);
    }

    @Test
    public void testQuery_closesResources() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenReturn(mockRs);
        when(mockRs.getMetaData()).thenReturn(mockMeta);
        when(mockMeta.getColumnCount()).thenReturn(0);
        when(mockRs.next()).thenReturn(false);

        JdbcUtil.query("SELECT 1", mockConn);

        verify(mockRs).close();
        verify(mockPst).close();
    }

    @Test
    public void testQuery_closesResourcesOnException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenReturn(mockRs);
        when(mockRs.getMetaData()).thenThrow(new SQLException("meta error"));

        try {
            JdbcUtil.query("SELECT 1", mockConn);
            fail("Should have thrown");
        } catch (SQLException e) {
            // expected
        }

        verify(mockRs).close();
        verify(mockPst).close();
    }

    // ===== getAllTables() =====

    @Test
    public void testGetAllTables_mysql() throws Exception {
        when(mockConn.prepareStatement("show tables")).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getString(1)).thenReturn("users", "orders");

        List<String> tables = JdbcUtil.getAllTables(mockConn, "MYSQL");

        assertEquals(2, tables.size());
        assertEquals("users", tables.get(0));
        assertEquals("orders", tables.get(1));
    }

    @Test
    public void testGetAllTables_postgresql() throws Exception {
        String expectedSql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name";
        when(mockConn.prepareStatement(expectedSql)).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getString(1)).thenReturn("accounts");

        List<String> tables = JdbcUtil.getAllTables(mockConn, "POSTGRESQL");

        assertEquals(1, tables.size());
        assertEquals("accounts", tables.get(0));
    }

    @Test
    public void testGetAllTables_exception_returnsNull() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("conn error"));

        List<String> result = JdbcUtil.getAllTables(mockConn, "MYSQL");

        assertNull(result);
    }

    @Test
    public void testGetAllTables_closesResources() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        JdbcUtil.getAllTables(mockConn, "MYSQL");

        verify(mockRs).close();
        verify(mockPst).close();
        verify(mockConn).close();
    }

    @Test
    public void testGetAllTables_closesResourcesOnException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenThrow(new SQLException("exec error"));

        JdbcUtil.getAllTables(mockConn, "MYSQL");

        // pst was created, rs was not
        verify(mockPst).close();
        verify(mockConn).close();
    }

    // ===== getRDBMSColumnProperties() =====

    @Test
    public void testGetRDBMSColumnProperties_mysql() throws Exception {
        when(mockConn.prepareStatement(contains("`my_table`"))).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenReturn(mockRs);
        when(mockRs.getMetaData()).thenReturn(mockMeta);
        when(mockMeta.getColumnCount()).thenReturn(2);
        when(mockMeta.getColumnTypeName(1)).thenReturn("INT");
        when(mockMeta.getColumnClassName(1)).thenReturn("java.lang.Integer");
        when(mockMeta.getColumnName(1)).thenReturn("id");
        when(mockMeta.getColumnTypeName(2)).thenReturn("VARCHAR");
        when(mockMeta.getColumnClassName(2)).thenReturn("java.lang.String");
        when(mockMeta.getColumnName(2)).thenReturn("name");

        List<JSONObject> result = JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", "my_table");

        assertEquals(2, result.size());
        assertEquals("INT", result.get(0).get("fieldTypeName"));
        assertEquals("id", result.get(0).get("label"));
        assertEquals("VARCHAR", result.get(1).get("fieldTypeName"));
    }

    @Test
    public void testGetRDBMSColumnProperties_postgresql_usesDoubleQuotes() throws Exception {
        when(mockConn.prepareStatement(contains("\"my_table\""))).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenReturn(mockRs);
        when(mockRs.getMetaData()).thenReturn(mockMeta);
        when(mockMeta.getColumnCount()).thenReturn(0);

        List<JSONObject> result = JdbcUtil.getRDBMSColumnProperties(mockConn, "POSTGRESQL", "my_table");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRDBMSColumnProperties_dotInColumnName() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenReturn(mockRs);
        when(mockRs.getMetaData()).thenReturn(mockMeta);
        when(mockMeta.getColumnCount()).thenReturn(1);
        when(mockMeta.getColumnTypeName(1)).thenReturn("INT");
        when(mockMeta.getColumnClassName(1)).thenReturn("java.lang.Integer");
        when(mockMeta.getColumnName(1)).thenReturn("schema.column_name");

        List<JSONObject> result = JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", "valid_table");

        assertEquals("column_name", result.get(0).get("label"));
    }

    @Test
    public void testGetRDBMSColumnProperties_exception_returnsNull() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("error"));

        List<JSONObject> result = JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", "valid_table");

        assertNull(result);
    }

    @Test
    public void testGetRDBMSColumnProperties_closesResources() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenReturn(mockRs);
        when(mockRs.getMetaData()).thenReturn(mockMeta);
        when(mockMeta.getColumnCount()).thenReturn(0);

        JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", "valid_table");

        verify(mockRs).close();
        verify(mockPst).close();
        verify(mockConn).close();
    }

    // ===== SQL injection patterns (caught by catch block -> returns null) =====

    @Test
    public void testSqlInjection_unionSelect_returnsNull() {
        assertNull(JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", "t UNION SELECT * FROM users"));
    }

    @Test
    public void testSqlInjection_semicolon_returnsNull() {
        assertNull(JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", "t;DROP TABLE x"));
    }

    @Test
    public void testSqlInjection_backtickEscape_returnsNull() {
        assertNull(JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", "t` WHERE 1=1--"));
    }

    @Test
    public void testValidTableName_withDots() throws Exception {
        // schema.table format should be allowed
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPst);
        when(mockPst.executeQuery()).thenReturn(mockRs);
        when(mockRs.getMetaData()).thenReturn(mockMeta);
        when(mockMeta.getColumnCount()).thenReturn(0);

        List<JSONObject> result = JdbcUtil.getRDBMSColumnProperties(mockConn, "MYSQL", "schema.table_name");

        assertNotNull(result);
    }
}
