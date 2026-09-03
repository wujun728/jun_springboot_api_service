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
@TableName(value = "SQLREST_CLIENT_GROUP", autoResultMap = true)
public class ClientGroupEntity {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("client_id")
  private Long clientId;

  @TableField("group_id")
  private Long groupId;
}
