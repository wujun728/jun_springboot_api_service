/*    */ package com.dbapi;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class SingleValueExtractPlugin extends TransformPlugin
/*    */ {
/*    */   public void init()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Object transform(Object data, String localPluginParam)
/*    */   {
/* 18 */     List list = (List)data;
/* 19 */     if (list.size() == 1) {
/* 20 */       JSONObject rowData = (JSONObject)list.get(0);
/* 21 */       if (StringUtils.isNotBlank(localPluginParam)) {
/* 22 */         Object value = rowData.get(localPluginParam);
/* 23 */         return value;
/*    */       }
/* 25 */       return rowData;
/*    */     }
/* 27 */     if (list.size() > 1) {
/* 28 */       throw new RuntimeException("单条查询结果集大于1");
/*    */     }
/* 30 */     return null;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 37 */     return "单条数据取值插件";
/*    */   }
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 42 */     return "针对SQL单条数据查询，转换结果格式为单条数据，即单个对象，或者单个值（字符串、数字）。如果SQL查询结果为空，结果为null";
/*    */   }
/*    */ 
/*    */   public String getParamDescription()
/*    */   {
/* 47 */     return "参数非必填，如果不填参数，格式为单个对象；如果填参数，会将参数作为字段将对应的值取出来";
/*    */   }
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\single-value-extract-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.SingleValueExtractPlugin
 * JD-Core Version:    0.6.2
 */