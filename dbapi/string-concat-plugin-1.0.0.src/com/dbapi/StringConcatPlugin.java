/*    */ package com.dbapi;
/*    */ 
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.gitee.freakchicken.dbapi.common.ApiConfig;
/*    */ import com.gitee.freakchicken.dbapi.plugin.ParamProcessPlugin;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class StringConcatPlugin extends ParamProcessPlugin
/*    */ {
/*    */   public void init()
/*    */   {
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 19 */     return "字符串拼接插件";
/*    */   }
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 24 */     return "给参数的值首尾拼接字符串，默认首尾拼接字符%";
/*    */   }
/*    */ 
/*    */   public String getParamDescription()
/*    */   {
/* 29 */     return "参数格式是{\"fields\":[\"xx\",\"yy\"], \"position\":\"all\", \"content\":\"%\"}。fields代表要进行字符串拼接的参数名，是数组格式；position可选值是all/start/end，分别代表首尾拼接、开头拼接、结尾拼接，可以不指定，默认是all；content表示要拼接的字符串内容，可以不指定，默认是%。";
/*    */   }
/*    */ 
/*    */   public void process(Map<String, Object> map, ApiConfig apiConfig, String param)
/*    */   {
/* 34 */     JSONObject p = new JSONObject();
/* 35 */     if ((param != null) && (!param.isEmpty())) {
/* 36 */       p = JSON.parseObject(param);
/*    */     }
/*    */ 
/* 39 */     String position = p.getOrDefault("position", "all").toString();
/* 40 */     String content = p.getOrDefault("content", "%").toString();
/* 41 */     List fields = p.getJSONArray("fields").toJavaList(String.class);
/*    */ 
/* 43 */     for (String field : fields) {
/* 44 */       Object o = map.get(field);
/* 45 */       if (o != null) {
/* 46 */         String value = map.get(field).toString();
/* 47 */         if (position.equals("all"))
/* 48 */           map.put(field, content + value + content);
/* 49 */         else if (position.equals("start"))
/* 50 */           map.put(field, content + value);
/* 51 */         else if (position.equals("end"))
/* 52 */           map.put(field, value + content);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\string-concat-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.StringConcatPlugin
 * JD-Core Version:    0.6.2
 */