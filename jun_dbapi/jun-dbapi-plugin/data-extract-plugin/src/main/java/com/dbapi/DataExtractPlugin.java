package com.dbapi;

import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.plugin.GlobalTransformPlugin;

public class DataExtractPlugin extends GlobalTransformPlugin {

    @Override
    public void init() {
    }

    @Override
    public Object transform(ResponseDto data, String params) {
        return data.getData();
    }

    @Override
    public String getName() {
        return "Data取值插件";
    }

    @Override
    public String getDescription() {
        return "只返回data字段的数据，不返回msg和success字段";
    }

    @Override
    public String getParamDescription() {
        return "不需要填参数";
    }
}
