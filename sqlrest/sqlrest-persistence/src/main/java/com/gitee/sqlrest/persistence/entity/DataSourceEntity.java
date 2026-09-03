
package com.gitee.sqlrest.persistence.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sqlrest.common.enums.ProductTypeEnum;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.EnumTypeHandler;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "SQLREST_DATASOURCE", autoResultMap = true)
public class DataSourceEntity {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("name")
  private String name;

  @TableField(value = "type", typeHandler = EnumTypeHandler.class)
  private ProductTypeEnum type;

  @TableField("version")
  private String version;

  @TableField("driver")
  private String driver;

  @TableField("url")
  private String url;

  @TableField("username")
  private String username;

  @TableField("password")
  private String password;

  @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
  private Timestamp createTime;

  @TableField(value = "update_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
  private Timestamp updateTime;

  public static boolean isSame(DataSourceEntity lhs, DataSourceEntity rhs) {
    if (null == lhs || null == rhs) {
      return false;
    }
    return StringUtils.equals(lhs.version, rhs.version)
        && StringUtils.equals(lhs.driver, rhs.driver)
        && StringUtils.equals(lhs.url, rhs.url)
        && StringUtils.equals(lhs.username, rhs.username)
        && StringUtils.equals(lhs.password, rhs.password);
  }
}
