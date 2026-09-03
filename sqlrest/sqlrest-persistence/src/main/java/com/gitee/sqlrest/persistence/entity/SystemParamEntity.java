package com.gitee.sqlrest.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sqlrest.common.enums.ParamTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "SQLREST_SYSTEM_PARAM", autoResultMap = true)
public class SystemParamEntity {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("param_key")
  private String paramKey;

  @TableField("param_type")
  private ParamTypeEnum paramType;

  @TableField("param_value")
  private String paramValue;
}
