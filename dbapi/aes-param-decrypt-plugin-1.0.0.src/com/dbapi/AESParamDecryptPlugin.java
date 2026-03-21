/*    */ package com.dbapi;
/*    */ 
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.gitee.freakchicken.dbapi.common.ApiConfig;
/*    */ import com.gitee.freakchicken.dbapi.plugin.ParamProcessPlugin;
/*    */ import com.gitee.freakchicken.dbapi.plugin.PluginConf;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Base64;
/*    */ import java.util.Base64.Decoder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.SecretKey;
/*    */ import javax.crypto.spec.SecretKeySpec;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class AESParamDecryptPlugin extends ParamProcessPlugin
/*    */ {
/*    */   public void init()
/*    */   {
/* 21 */     this.logger.info(getName() + "初始化");
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 26 */     return "AES参数解密插件";
/*    */   }
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 31 */     return "对参数值使用AES算法解密";
/*    */   }
/*    */ 
/*    */   public String getParamDescription()
/*    */   {
/* 36 */     return "参数格式：{\"fields\":[\"aa\",\"bb\"],\"secret\":\"xxx\"}，其中fields表示要解密的参数名称，secret表示密钥，必须是16位字符串。fields可不填，如果不填，则对所有参数进行解密；secret可不填，如果不填就从全局插件参数中获取secret";
/*    */   }
/*    */ 
/*    */   public void process(Map<String, Object> map, ApiConfig apiConfig, String param)
/*    */   {
/* 43 */     JSONObject p = new JSONObject();
/* 44 */     if ((param != null) && (!param.isEmpty())) {
/* 45 */       p = JSON.parseObject(param);
/*    */     }
/*    */ 
/* 48 */     String secret = p.getString("secret");
/* 49 */     if ((secret == null) || (secret.isEmpty())) {
/* 50 */       secret = PluginConf.getKey("AESParamDecryptPlugin.secret");
/*    */     }
/* 52 */     if ((secret == null) || (secret.isEmpty())) {
/* 53 */       this.logger.error("插件全局参数和局部参数中都缺少加密密钥secret");
/* 54 */       throw new RuntimeException("插件全局参数和局部参数中都缺少加密密钥secret");
/*    */     }
/* 56 */     if (secret.length() != 16) {
/* 57 */       this.logger.error("加密密钥secret长度必须16位");
/* 58 */       throw new RuntimeException("加密密钥secret长度必须16位");
/*    */     }
/*    */ 
/* 61 */     JSONArray fields = p.getJSONArray("fields");
/* 62 */     List paramToEncryptList = fields == null ? new ArrayList() : fields.toJavaList(String.class);
/*    */ 
/* 64 */     for (String key : map.keySet())
/* 65 */       if (((fields != null) && (paramToEncryptList.contains(key))) || (fields == null))
/*    */         try {
/* 67 */           map.put(key, decrypt(map.get(key).toString(), secret));
/*    */         } catch (Exception e) {
/* 69 */           this.logger.error(e.getMessage(), e);
/* 70 */           throw new RuntimeException(e);
/*    */         }
/*    */   }
/*    */ 
/*    */   private String decrypt(String encryptedData, String key)
/*    */     throws Exception
/*    */   {
/* 78 */     SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
/* 79 */     Cipher cipher = Cipher.getInstance("AES");
/* 80 */     cipher.init(2, secretKey);
/* 81 */     byte[] originalBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
/* 82 */     String decrypted = new String(originalBytes);
/* 83 */     this.logger.info("解密成功: {}", decrypted);
/* 84 */     return decrypted;
/*    */   }
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\aes-param-decrypt-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.AESParamDecryptPlugin
 * JD-Core Version:    0.6.2
 */