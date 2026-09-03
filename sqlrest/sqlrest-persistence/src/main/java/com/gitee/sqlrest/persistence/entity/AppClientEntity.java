package com.gitee.sqlrest.persistence.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sqlrest.common.enums.DurationTimeEnum;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.EnumTypeHandler;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "SQLREST_APP_CLIENT", autoResultMap = true)
public class AppClientEntity {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("name")
  private String name;

  @TableField("description")
  private String description;

  @TableField("app_key")
  private String appKey;

  @TableField("app_secret")
  private String appSecret;

  @TableField(value = "expire_duration", typeHandler = EnumTypeHandler.class)
  private DurationTimeEnum expireDuration;

  @TableField("expire_at")
  private Long expireAt;

  @TableField(value = "access_token", insertStrategy = FieldStrategy.NEVER)
  private String accessToken;

  @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
  private Timestamp createTime;

  @TableField(value = "update_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
  private Timestamp updateTime;
}
