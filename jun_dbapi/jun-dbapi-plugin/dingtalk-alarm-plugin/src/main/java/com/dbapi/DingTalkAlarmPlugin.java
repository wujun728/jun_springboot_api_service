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

public class DingTalkAlarmPlugin extends AlarmPlugin {

    @Override
    public void init() {
    }

    @Override
    public void alarm(Exception e, ApiConfig apiConfig, HttpServletRequest request, String s) {
        String webhookUrl = PluginConf.getKey("DingTalkAlarmPlugin.WEBHOOK_URL");
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            this.logger.error("DingTalkAlarmPlugin.WEBHOOK_URL is not set");
            return;
        }
        sendTextMessage(webhookUrl,
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date()),
                apiConfig.getName(), apiConfig.getPath(), e.toString(), request.getRemoteAddr());
    }

    @Override
    public String getName() {
        return "钉钉告警插件";
    }

    @Override
    public String getDescription() {
        return "发送异常消息给钉钉群";
    }

    @Override
    public String getParamDescription() {
        return "无参数";
    }

    private void sendTextMessage(String webhookUrl, String time, String apiName, String uri, String error, String address) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String markdownMessage = "## DBAPI ALARM\n- **TIME:** %s\n- **API:** %s\n- **URI:** %s\n- **ERROR:** %s\n- **REMOTE ADDRESS:** %s";
            markdownMessage = String.format(markdownMessage, time, apiName, uri, error, address);

            String jsonPayload = String.format(
                    "{\"msgtype\": \"markdown\", \"markdown\": {\"title\": \"DBAPI ALARM\", \"text\": \"%s\"}}",
                    markdownMessage);

            try (OutputStream output = connection.getOutputStream()) {
                output.write(jsonPayload.getBytes("UTF-8"));
            }
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                this.logger.error("Failed to send message to DingTalk, response code: " + responseCode);
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }
}
