/*    */ package com.dbapi;
/*    */ 
/*    */ import com.gitee.freakchicken.dbapi.common.ResponseDto;
/*    */ import com.gitee.freakchicken.dbapi.plugin.GlobalTransformPlugin;
/*    */ 
/*    */ public class DataExtractPlugin extends GlobalTransformPlugin
/*    */ {
/*    */   public void init()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Object transform(ResponseDto data, String params)
/*    */   {
/* 17 */     return data.getData();
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 22 */     return "Data取值插件";
/*    */   }
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 27 */     return "只返回data字段的数据，不返回msg和success字段";
/*    */   }
/*    */ 
/*    */   public String getParamDescription()
/*    */   {
/* 32 */     return "不需要填参数";
/*    */   }
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\data-extract-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.DataExtractPlugin
 * JD-Core Version:    0.6.2
 */