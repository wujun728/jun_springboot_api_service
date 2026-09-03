package com.zapi.dataset.model.vo;

import com.zapi.dataset.model.entity.DataSet;
import lombok.Data;

import java.util.List;

@Data
public class DataSetVO extends DataSet {

    /***
     * 字段列表
     */
    private List<FieldInfoVO> fields;

}
