package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "base_dict_data")
public class DictData extends Model<DictData> {

    @Id
    private Long id;

    /**
     * 字典编码
     */
    @Column(label = "字段编号")
    private Long dictCode;

    /**
     * 字典排序
     */
    @Column(label = "字段排序")
    private Integer dictSort;

    /**
     * 字典标签
     */
    @Column(label = "字典标签")
    private String dictLabel;

    /**
     * 字典键值
     */
    @Column(label = "字典健值")
    private String dictValue;

    /**
     * 字典类型
     */
    @Column(label = "字典类型")
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;

    /**
     * 表格字典样式
     */
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    private String isDefault;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    @Column(label = "备注")
    private String remark;

    public boolean getDefault() {
        return true;
    }

}