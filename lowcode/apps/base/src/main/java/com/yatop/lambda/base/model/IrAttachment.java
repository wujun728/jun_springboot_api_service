package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.annotation.NotBlank;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;


@Table(name = "base_attachment")
public class IrAttachment extends Model<IrAttachment> {
    @Id
    private Long id;

    @Column(name = "file_name",label = "文件名")
    @NotBlank
    private String fileName;

    @Column(name = "size",label = "大小")
    private String size;

    @Column
    @NotBlank
    private Long age;

}
