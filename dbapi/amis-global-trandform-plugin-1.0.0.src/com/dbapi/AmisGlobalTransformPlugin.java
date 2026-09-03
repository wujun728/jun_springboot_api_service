/*    */ package com.dbapi;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.gitee.freakchicken.dbapi.common.ResponseDto;
/*    */ import com.gitee.freakchicken.dbapi.plugin.GlobalTransformPlugin;
/*    */ 
/*    */ public class AmisGlobalTransformPlugin extends GlobalTransformPlugin
/*    */ {
/*    */   public void init()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Object transform(ResponseDto data, String params)
/*    */   {
/* 18 */     JSONObject obj = new JSONObject();
/* 19 */     if (data.getSuccess()) {
/* 20 */       obj.put("status", Integer.valueOf(0));
/* 21 */       obj.put("msg", data.getMsg());
/* 22 */       obj.put("data", data.getData());
/*    */     } else {
/* 24 */       obj.put("status", Integer.valueOf(-1));
/* 25 */       obj.put("msg", data.getMsg());
/* 26 */       obj.put("data", data.getData());
/*    */     }
/*    */ 
/* 29 */     return obj;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 34 */     return "AMIS格式转换插件";
/*    */   }
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 39 */     return "将数据格式转化成AMIS框架需要的格式：{\"msg\":\"xxx\",\"status\":0,\"data\":{\"key\":\"value\"}}";
/*    */   }
/*    */ 
/*    */   public String getParamDescription()
/*    */   {
/* 44 */     return "不需要填参数";
/*    */   }
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\amis-global-trandform-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.AmisGlobalTransformPlugin
 * JD-Core Version:    0.6.2
 */