package com.gitee.sqlrest.persistence.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "SQLREST_ACCESS_RECORD", autoResultMap = true)
public class AccessRecordEntity {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("path")
  private String path;

  @TableField("status")
  private Integer status;

  @TableField("duration")
  private Long duration;

  @TableField("ip_addr")
  private String ipAddr;

  @TableField("user_agent")
  private String userAgent;

  @TableField("client_key")
  private String clientKey;

  @TableField("api_id")
  private Long apiId;

  @TableField("exception")
  private String exception;

  @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
  private Timestamp createTime;
}
