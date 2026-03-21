/*    */ package com.dbapi;
/*    */ 
/*    */ import com.gitee.freakchicken.dbapi.common.ApiConfig;
/*    */ import com.gitee.freakchicken.dbapi.plugin.AlarmPlugin;
/*    */ import com.gitee.freakchicken.dbapi.plugin.PluginConf;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FeishuAlarmPlugin extends AlarmPlugin
/*    */ {
/*    */   public void init()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void alarm(Exception e, ApiConfig apiConfig, HttpServletRequest request, String s)
/*    */   {
/* 23 */     String webhookUrl = PluginConf.getKey("FeishuAlarmPlugin.WEBHOOK_URL");
/* 24 */     if ((webhookUrl == null) || (webhookUrl.isEmpty())) {
/* 25 */       this.logger.error("FeishuAlarmPlugin.WEBHOOK_URL is not set");
/* 26 */       return;
/*    */     }
/*    */ 
/* 29 */     sendTextMessage(webhookUrl, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date()), apiConfig.getName(), apiConfig
/* 30 */       .getPath(), e.toString(), request.getRemoteAddr());
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 35 */     return "飞书告警插件";
/*    */   }
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 40 */     return "发送异常消息给飞书群";
/*    */   }
/*    */ 
/*    */   public String getParamDescription()
/*    */   {
/* 45 */     return "无参数";
/*    */   }
/*    */ 
/*    */   public void sendTextMessage(String webhookUrl, String time, String apiName, String uri, String error, String address) {
/*    */     try {
/* 50 */       URL url = new URL(webhookUrl);
/* 51 */       HttpURLConnection connection = (HttpURLConnection)url.openConnection();
/* 52 */       connection.setDoOutput(true);
/* 53 */       connection.setRequestMethod("POST");
/* 54 */       connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
/*    */ 
/* 56 */       String msg = "{\n  \"msg_type\": \"interactive\",\n  \"card\": {\n    \"config\": { \"wide_screen_mode\": false },\n    \"header\": {\n      \"title\": { \"content\": \"DBAPI ERROR: %s\", \"tag\": \"plain_text\" },\n      \"template\": \"carmine\"\n    },\n    \"elements\": [\n      {\n        \"tag\": \"div\",\n        \"text\": {\n          \"content\": \"%s\",\n          \"tag\": \"lark_md\"\n        }\n      }\n    ]\n  }\n}\n";
/*    */ 
/* 76 */       String content = "TIME: %s\\nAPI: %s\\nURI: %s\\nERROR: %s\\nREMOTE ADDRESS: %s";
/* 77 */       content = String.format(content, new Object[] { time, apiName, uri, error, address });
/* 78 */       String jsonPayload = String.format(msg, new Object[] { apiName, content });
/*    */ 
/* 80 */       System.out.println(jsonPayload);
/* 81 */       OutputStream output = connection.getOutputStream(); Throwable localThrowable3 = null;
/*    */       try { output.write(jsonPayload.getBytes("UTF-8")); }
/*    */       catch (Throwable localThrowable1)
/*    */       {
/* 81 */         localThrowable3 = localThrowable1; throw localThrowable1;
/*    */       } finally {
/* 83 */         if (output != null) if (localThrowable3 != null) try { output.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else output.close();
/*    */       }
/* 85 */       int responseCode = connection.getResponseCode();
/* 86 */       if (responseCode == 200);
/*    */     }
/*    */     catch (Exception localException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\feishu-alarm-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.FeishuAlarmPlugin
 * JD-Core Version:    0.6.2
 */