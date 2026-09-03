package com.zapi.form.support.request;

import com.zapi.enums.FormOperationEnum;
import com.zapi.form.model.dto.FormTableColumnDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormOperationRequest {

    /***
     * 操作类型 FormOperationEnum
     */
    private FormOperationEnum operationType;

    /***
     * 相关转换实体
     */
    private FormTableColumnDTO tableColumnDTO;
}
