/*    */ package com.dbapi;
/*    */ 
/*    */ import com.gitee.freakchicken.dbapi.common.ApiConfig;
/*    */ import com.gitee.freakchicken.dbapi.plugin.AlarmPlugin;
/*    */ import com.gitee.freakchicken.dbapi.plugin.PluginConf;
/*    */ import java.text.MessageFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.commons.mail.DefaultAuthenticator;
/*    */ import org.apache.commons.mail.Email;
/*    */ import org.apache.commons.mail.EmailException;
/*    */ import org.apache.commons.mail.SimpleEmail;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class EmailAlarmPlugin extends AlarmPlugin
/*    */ {
/*    */   private String username;
/*    */   private String password;
/*    */   private String host;
/*    */ 
/*    */   public void init()
/*    */   {
/* 27 */     this.username = PluginConf.getKey("EMAIL_USERNAME");
/* 28 */     this.password = PluginConf.getKey("EMAIL_PASSWORD");
/* 29 */     this.host = PluginConf.getKey("EMAIL_HOST");
/*    */   }
/*    */ 
/*    */   public void alarm(Exception e, ApiConfig config, HttpServletRequest request, String pluginParam)
/*    */   {
/* 35 */     String title = MessageFormat.format("API ERROR: {0}", new Object[] { config.getName() });
/* 36 */     String content = MessageFormat.format("TIME:  {0}\n API_ID:  {5}\nNAME:  {1}\n URL:  {2}\n REMOTE ADDRESS:  {3}\n\n{4}", new Object[] { new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS")
/* 38 */       .format(new Date()), 
/* 38 */       config
/* 39 */       .getName(), request.getRequestURI(), request.getRemoteAddr(), e.toString(), config.getId() });
/* 40 */     sendSimpleTextEmail(pluginParam, title, content);
/*    */   }
/*    */ 
/*    */   public void sendSimpleTextEmail(String to, String title, String content) {
/*    */     try {
/* 45 */       Email email = new SimpleEmail();
/* 46 */       email.setHostName(this.host);
/*    */ 
/* 49 */       email.setAuthenticator(new DefaultAuthenticator(this.username, this.password));
/* 50 */       email.setSSLOnConnect(true);
/* 51 */       email.setCharset("UTF-8");
/*    */ 
/* 53 */       email.setFrom(this.username);
/*    */ 
/* 55 */       email.addTo(to);
/* 56 */       email.setSubject(title);
/* 57 */       email.setMsg(content);
/* 58 */       email.send();
/*    */     } catch (EmailException e) {
/* 60 */       this.logger.info(e.getMessage(), e);
/*    */     }
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 67 */     return "邮件告警插件";
/*    */   }
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 72 */     return "使用该插件，异常信息会发送邮件通知。使用前需要在plugin.properties配置全局参数：发件人的信息。";
/*    */   }
/*    */ 
/*    */   public String getParamDescription()
/*    */   {
/* 77 */     return "插件参数请填写收件人邮箱，多个用英文分号;隔开";
/*    */   }
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\email-alarm-plugin-1.0.0.src\email-alarm-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.EmailAlarmPlugin
 * JD-Core Version:    0.6.2
 */