package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

/**
 * 字典类型表
 *
 * @author Lion Li
 */
@Table(name = "base_dict_type")
public class DictType extends Model<DictType> {

    /**
     * 字典主键
     */
    @Id
    private Long id;

    /**
     * 字典名称
     */
    @Column(label = "字典名称")
    private String dictName;

    /**
     * 字典类型
     */
    @Column(label = "字典类型")
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    @Column(label = "备注")
    private String remark;

}
