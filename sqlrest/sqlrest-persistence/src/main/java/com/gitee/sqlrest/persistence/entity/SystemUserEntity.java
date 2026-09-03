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
@TableName(value = "SQLREST_SYSTEM_USER", autoResultMap = true)
public class SystemUserEntity {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("username")
  private String username;

  @TableField("password")
  private String password;

  @TableField("salt")
  private String salt;

  @TableField("real_name")
  private String realName;

  @TableField("email")
  private String email;

  @TableField("address")
  private String address;

  @TableField("locked")
  private Boolean locked;

  @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
  private Timestamp createTime;

  @TableField(value = "update_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
  private Timestamp updateTime;

}
