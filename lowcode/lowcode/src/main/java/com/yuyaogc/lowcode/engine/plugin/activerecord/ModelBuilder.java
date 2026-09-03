package com.yuyaogc.lowcode.engine.plugin.activerecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * ModelBuilder.
 */
public class ModelBuilder {

    public static final ModelBuilder me = new ModelBuilder();

    @SuppressWarnings({"rawtypes"})
    public <T> List<T> build(ResultSet rs, Class<? extends Model> modelClass) throws SQLException, ReflectiveOperationException {
        return build(rs, modelClass, null);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> List<T> build(ResultSet rs, Class<? extends Model> modelClass, Function<T, Boolean> func) throws SQLException, ReflectiveOperationException {
        List<T> result = new ArrayList<T>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        buildLabelNamesAndTypes(rsmd, labelNames, types);
        while (rs.next()) {
            Model<?> ar = modelClass.newInstance();
            Map<String, Object> attrs = ar.getData();
            for (int i = 1; i <= columnCount; i++) {
                Object value;
                if (types[i] < Types.BLOB) {
                    value = rs.getObject(i);
                } else {
                    if (types[i] == Types.CLOB) {
                        value = handleClob(rs.getClob(i));
                    } else if (types[i] == Types.NCLOB) {
                        value = handleClob(rs.getNClob(i));
                    } else if (types[i] == Types.BLOB) {
                        value = handleBlob(rs.getBlob(i));
                    } else {
                        value = rs.getObject(i);
                    }
                }

                attrs.put(labelNames[i], value);
            }

            if (func == null) {
                result.add((T) ar);
            } else {
                if (!func.apply((T) ar)) {
                    break;
                }
            }
        }
        return result;
    }

    public void buildLabelNamesAndTypes(ResultSetMetaData rsmd, String[] labelNames, int[] types) throws SQLException {
        for (int i = 1; i < labelNames.length; i++) {
            // 备忘：getColumnLabel 获取 sql as 子句指定的名称而非字段真实名称
            labelNames[i] = rsmd.getColumnLabel(i);
            types[i] = rsmd.getColumnType(i);
        }
    }

    public byte[] handleBlob(Blob blob) throws SQLException {
        if (blob == null)
            return null;

        InputStream is = null;
        try {
            is = blob.getBinaryStream();
            if (is == null)
                return null;
            byte[] data = new byte[(int) blob.length()];        // byte[] data = new byte[is.available()];
            if (data.length == 0)
                return null;
            is.read(data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    public String handleClob(Clob clob) throws SQLException {
        if (clob == null)
            return null;

        Reader reader = null;
        try {
            reader = clob.getCharacterStream();
            if (reader == null)
                return null;
            char[] buffer = new char[(int) clob.length()];
            if (buffer.length == 0)
                return null;
            reader.read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }


}
