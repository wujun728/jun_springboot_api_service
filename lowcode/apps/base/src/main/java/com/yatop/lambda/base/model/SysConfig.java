package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.enums.DataTypeEnum;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.Map;

@Table(name = "base_sys_config", displayName = "系统配置")
public class SysConfig extends Model<SysConfig> {
    @Id
    private Long id;

    @File(name = "sys_icon", label = "系统图标")
    private String sysIcon;

    @Column(name = "sys_name" ,label = "系统名称")
    private String sysName;


    @Selection(options = {
            @Option(label = "正常", value = "0"),
            @Option(label = "失效",value = "1")
    })
    @Column(label = "系统状态")
    private String status;


    @Service
    public void create(SysConfig value){
        super.create(value);
    }

}
