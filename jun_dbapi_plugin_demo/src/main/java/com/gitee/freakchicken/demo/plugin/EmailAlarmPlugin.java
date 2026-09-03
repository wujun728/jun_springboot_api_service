package com.gitee.freakchicken.demo.plugin;

import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.AlarmPlugin;

import javax.servlet.http.HttpServletRequest;

public class EmailAlarmPlugin extends AlarmPlugin {

    @Override
    public void init() {

    }

    @Override
    public void alarm(Exception e, ApiConfig config, HttpServletRequest request, String localPluginParam) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getParamDescription() {
        return null;
    }
}
