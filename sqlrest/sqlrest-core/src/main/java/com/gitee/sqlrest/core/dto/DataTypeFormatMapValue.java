package com.gitee.sqlrest.core.dto;

import com.gitee.sqlrest.common.enums.DataTypeFormatEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTypeFormatMapValue {

  private DataTypeFormatEnum key;
  private String value;
  private String remark;
}
