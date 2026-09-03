package com.gitee.sqlrest.persistence.handler;

import com.gitee.sqlrest.common.dto.ItemParam;
import com.gitee.sqlrest.persistence.util.JsonUtils;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ListParamHandler extends BaseTypeHandler<List<ItemParam>> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<ItemParam> list, JdbcType jdbcType)
      throws SQLException {
    ps.setString(i, list2string(list));
  }

  @Override
  public List<ItemParam> getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    String r = rs.getString(columnName);
    if (rs.wasNull()) {
      return null;
    }
    return string2list(r);
  }

  @Override
  public List<ItemParam> getNullableResult(ResultSet rs, int columnIndex)
      throws SQLException {
    String r = rs.getString(columnIndex);
    if (rs.wasNull()) {
      return null;
    }
    return string2list(r);
  }

  @Override
  public List<ItemParam> getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    String r = cs.getString(columnIndex);
    if (cs.wasNull()) {
      return null;
    }
    return string2list(r);
  }

  private String list2string(List<ItemParam> list) {
    if (list == null || list.isEmpty()) {
      return null;
    }
    return JsonUtils.toJsonString(list);
  }

  private List<ItemParam> string2list(String str) {
    if (str == null || str.isEmpty()) {
      return new ArrayList<>();
    }
    return JsonUtils.toBeanList(str, ItemParam.class);
  }
}
