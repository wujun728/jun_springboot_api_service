package com.dbapi;

import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.AlarmPlugin;
import com.gitee.freakchicken.dbapi.plugin.PluginConf;

import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeishuAlarmPlugin extends AlarmPlugin {

    @Override
    public void init() {
    }

    @Override
    public void alarm(Exception e, ApiConfig apiConfig, HttpServletRequest request, String s) {
        String webhookUrl = PluginConf.getKey("FeishuAlarmPlugin.WEBHOOK_URL");
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            this.logger.error("FeishuAlarmPlugin.WEBHOOK_URL is not set");
            return;
        }
        sendTextMessage(webhookUrl,
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date()),
                apiConfig.getName(), apiConfig.getPath(), e.toString(), request.getRemoteAddr());
    }

    @Override
    public String getName() {
        return "飞书告警插件";
    }

    @Override
    public String getDescription() {
        return "发送异常消息给飞书群";
    }

    @Override
    public String getParamDescription() {
        return "无参数";
    }

    public void sendTextMessage(String webhookUrl, String time, String apiName, String uri, String error, String address) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String msg = "{\n  \"msg_type\": \"interactive\",\n  \"card\": {\n    \"config\": { \"wide_screen_mode\": false },\n    \"header\": {\n      \"title\": { \"content\": \"DBAPI ERROR: %s\", \"tag\": \"plain_text\" },\n      \"template\": \"carmine\"\n    },\n    \"elements\": [\n      {\n        \"tag\": \"div\",\n        \"text\": {\n          \"content\": \"%s\",\n          \"tag\": \"lark_md\"\n        }\n      }\n    ]\n  }\n}\n";

            String content = "TIME: %s\\nAPI: %s\\nURI: %s\\nERROR: %s\\nREMOTE ADDRESS: %s";
            content = String.format(content, time, apiName, uri, error, address);
            String jsonPayload = String.format(msg, apiName, content);

            try (OutputStream output = connection.getOutputStream()) {
                output.write(jsonPayload.getBytes("UTF-8"));
            }
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                this.logger.error("Failed to send message to Feishu, response code: " + responseCode);
            }
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
        }
    }
}
