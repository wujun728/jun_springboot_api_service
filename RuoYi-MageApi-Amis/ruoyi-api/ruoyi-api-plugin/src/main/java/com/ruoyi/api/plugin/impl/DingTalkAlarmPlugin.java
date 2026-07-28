package com.ruoyi.api.plugin.impl;

import com.ruoyi.api.plugin.AlarmPlugin;

public class DingTalkAlarmPlugin extends AlarmPlugin {

    private String webhookUrl;

    @Override
    public void init() {
        this.webhookUrl = com.ruoyi.api.plugin.PluginConf.getKey("dingtalk.webhook");
        this.logger.info("DingTalkAlarmPlugin initialized");
    }

    @Override
    public void alarm(Exception e, String apiPath, String requestUri, String localPluginParam) {
        this.logger.error("DingTalk alarm triggered for api: {}, error: {}", apiPath, e.getMessage());
    }

    @Override
    public String getName() {
        return "DingTalk Alarm";
    }

    @Override
    public String getDescription() {
        return "Send alarm messages to DingTalk group chat";
    }

    @Override
    public String getParamDescription() {
        return "No parameters required. Configure webhook in plugin.properties: dingtalk.webhook";
    }
}