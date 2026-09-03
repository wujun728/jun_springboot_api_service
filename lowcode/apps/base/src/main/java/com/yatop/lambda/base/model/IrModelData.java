package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;


@Table(name = "base_mode_data",displayName = "种子数据")
public class IrModelData extends Model<IrModelData>{
    @Id
    private Long id;
    @Column(label = "名称")
    private String name;

    @Column(label = "模组")
    private String module;

    @Column(label = "关联id")
    private String refId;

    @Service
    public IrModelData findRef(String xmlid) {
        String[] parts = xmlid.split("\\.", 2);
       return this.selectOne(Criteria.equal("refId", parts[1]));
    }



    @Service(event = true)
    public void ttttt(){
        getContext().get("base.base_user").search(new Criteria(),0,0,null);
    }


}
