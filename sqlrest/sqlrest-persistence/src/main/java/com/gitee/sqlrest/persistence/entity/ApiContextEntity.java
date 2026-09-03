package com.gitee.sqlrest.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "SQLREST_API_CONTEXT", autoResultMap = true)
public class ApiContextEntity {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("api_id")
  private Long apiId;

  @TableField("sql_text")
  private String sqlText;

  public ApiContextEntity(String sqlText) {
    this.sqlText = sqlText;
  }
}
