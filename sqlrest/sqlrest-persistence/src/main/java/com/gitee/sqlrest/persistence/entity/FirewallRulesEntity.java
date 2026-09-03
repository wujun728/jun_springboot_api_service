package com.gitee.sqlrest.persistence.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sqlrest.common.enums.OnOffEnum;
import com.gitee.sqlrest.common.enums.WhiteBlackEnum;
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
@TableName(value = "SQLREST_FIREWALL_RULES", autoResultMap = true)
public class FirewallRulesEntity {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField(value = "status", typeHandler = EnumTypeHandler.class)
  private OnOffEnum status;

  @TableField(value = "mode", typeHandler = EnumTypeHandler.class)
  private WhiteBlackEnum mode;

  @TableField(value = "addresses")
  private String addresses;

  @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
  private Timestamp createTime;

  @TableField(value = "update_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
  private Timestamp updateTime;
}
