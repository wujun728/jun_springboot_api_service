/*    */ package com.dbapi;
/*    */ 
/*    */ import com.gitee.freakchicken.dbapi.common.ApiConfig;
/*    */ import com.gitee.freakchicken.dbapi.plugin.AlarmPlugin;
/*    */ import com.gitee.freakchicken.dbapi.plugin.PluginConf;
/*    */ import java.io.OutputStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DingTalkAlarmPlugin extends AlarmPlugin
/*    */ {
/*    */   public void init()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void alarm(Exception e, ApiConfig apiConfig, HttpServletRequest request, String s)
/*    */   {
/* 23 */     String webhookUrl = PluginConf.getKey("DingTalkAlarmPlugin.WEBHOOK_URL");
/* 24 */     if ((webhookUrl == null) || (webhookUrl.isEmpty())) {
/* 25 */       this.logger.error("DingTalkAlarmPlugin.WEBHOOK_URL is not set");
/* 26 */       return;
/*    */     }
/*    */ 
/* 29 */     sendTextMessage(webhookUrl, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date()), apiConfig.getName(), apiConfig
/* 30 */       .getPath(), e.toString(), request.getRemoteAddr());
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 35 */     return "钉钉告警插件";
/*    */   }
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 40 */     return "发送异常消息给钉钉群";
/*    */   }
/*    */ 
/*    */   public String getParamDescription()
/*    */   {
/* 45 */     return "无参数";
/*    */   }
/*    */ 
/*    */   private void sendTextMessage(String webhookUrl, String time, String apiName, String uri, String error, String address) {
/*    */     try {
/* 50 */       URL url = new URL(webhookUrl);
/* 51 */       HttpURLConnection connection = (HttpURLConnection)url.openConnection();
/* 52 */       connection.setDoOutput(true);
/* 53 */       connection.setRequestMethod("POST");
/* 54 */       connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
/*    */ 
/* 56 */       String markdownMessage = "## DBAPI ALARM\n- **TIME:** %s\n- **API:** %s\n- **URI:** %s\n- **ERROR:** %s\n- **REMOTE ADDRESS:** %s";
/* 57 */       markdownMessage = String.format(markdownMessage, new Object[] { time, apiName, uri, error, address });
/*    */ 
/* 60 */       String jsonPayload = String.format("{\"msgtype\": \"markdown\", \"markdown\": {\"title\": \"DBAPI ALARM\", \"text\": \"%s\"}}", new Object[] { markdownMessage });
/*    */ 
/* 65 */       OutputStream output = connection.getOutputStream(); Throwable localThrowable3 = null;
/*    */       try { output.write(jsonPayload.getBytes("UTF-8")); }
/*    */       catch (Throwable localThrowable1)
/*    */       {
/* 65 */         localThrowable3 = localThrowable1; throw localThrowable1;
/*    */       } finally {
/* 67 */         if (output != null) if (localThrowable3 != null) try { output.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else output.close();
/*    */       }
/* 69 */       int responseCode = connection.getResponseCode();
/* 70 */       if (responseCode != 200)
/* 71 */         this.logger.error("Failed to send message to DingTalk, response code: " + responseCode);
/*    */     }
/*    */     catch (Exception e) {
/* 74 */       this.logger.error(e.getMessage(), e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\dingtalk-alarm-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.DingTalkAlarmPlugin
 * JD-Core Version:    0.6.2
 */