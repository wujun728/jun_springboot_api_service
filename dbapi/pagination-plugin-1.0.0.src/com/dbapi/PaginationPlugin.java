/*    */ package com.dbapi;
/*    */ 
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.gitee.freakchicken.dbapi.common.ApiConfig;
/*    */ import com.gitee.freakchicken.dbapi.plugin.ParamProcessPlugin;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class PaginationPlugin extends ParamProcessPlugin
/*    */ {
/*    */   public String getName()
/*    */   {
/* 13 */     return "分页插件";
/*    */   }
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 18 */     return "根据分页参数pageNo、pageSize，自动生成新的参数 offset = (pageNo - 1) * pageSize";
/*    */   }
/*    */ 
/*    */   public String getParamDescription()
/*    */   {
/* 23 */     return "参数格式： {\"pageNoField\":\"pageNo\",\"pageSizeField\":\"pageSize\",\"offsetField\":\"offset\"}，非必填，有默认值";
/*    */   }
/*    */ 
/*    */   public void init()
/*    */   {
/* 28 */     this.logger.info("分页插件初始化完成");
/*    */   }
/*    */ 
/*    */   public void process(Map<String, Object> requestParam, ApiConfig apiConfig, String localPluginParam)
/*    */   {
/* 33 */     String pageNofield = "pageNo";
/* 34 */     String pageSizeField = "pageSize";
/* 35 */     String offsetField = "offset";
/*    */ 
/* 37 */     if ((localPluginParam != null) && (!localPluginParam.trim().isEmpty())) {
/* 38 */       JSONObject jsonObject = JSON.parseObject(localPluginParam);
/* 39 */       if (jsonObject.containsKey("pageNoField")) {
/* 40 */         pageNofield = jsonObject.getString("pageNoField");
/*    */       }
/* 42 */       if (jsonObject.containsKey("pageSizeField")) {
/* 43 */         pageSizeField = jsonObject.getString("pageSizeField");
/*    */       }
/* 45 */       if (jsonObject.containsKey("offsetField")) {
/* 46 */         offsetField = jsonObject.getString("offsetField");
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 51 */     Object pageNoObj = requestParam.get(pageNofield);
/*    */ 
/* 53 */     if (pageNoObj == null) {
/* 54 */       throw new RuntimeException("分页插件：参数" + pageNofield + "不能为空");
/*    */     }
/* 56 */     Integer pageNo = null;
/* 57 */     if ((pageNoObj instanceof Long))
/* 58 */       pageNo = Integer.valueOf(((Long)pageNoObj).intValue());
/* 59 */     else if ((pageNoObj instanceof Integer))
/* 60 */       pageNo = (Integer)pageNoObj;
/*    */     else {
/*    */       try {
/* 62 */         pageNo = Integer.parseInt(pageNoObj.toString());
/*    */       } catch (NumberFormatException e) {
/*    */         throw new RuntimeException("分页插件：参数" + pageNofield + "的值（" + pageNoObj + "）无法转换为整数");
/*    */       }
/*    */     }
/*    */     if (pageNo.intValue() < 1) {
/*    */       throw new RuntimeException("分页插件：参数" + pageNofield + "必须为正整数，当前值：" + pageNo);
/*    */     }
/*    */
/* 65 */     Object pageSizeObj = requestParam.get(pageSizeField);
/* 66 */     if (pageSizeObj == null) {
/* 67 */       throw new RuntimeException("分页插件：参数" + pageSizeField + "不能为空");
/*    */     }
/* 69 */     Integer pageSize = null;
/* 70 */     if ((pageSizeObj instanceof Long))
/* 71 */       pageSize = Integer.valueOf(((Long)pageSizeObj).intValue());
/* 72 */     else if ((pageSizeObj instanceof Integer))
/* 73 */       pageSize = (Integer)pageSizeObj;
/*    */     else {
/*    */       try {
/* 75 */         pageSize = Integer.parseInt(pageSizeObj.toString());
/*    */       } catch (NumberFormatException e) {
/*    */         throw new RuntimeException("分页插件：参数" + pageSizeField + "的值（" + pageSizeObj + "）无法转换为整数");
/*    */       }
/*    */     }
/*    */     if (pageSize.intValue() < 1) {
/*    */       throw new RuntimeException("分页插件：参数" + pageSizeField + "必须为正整数，当前值：" + pageSize);
/*    */     }
/* 77 */     Integer offset = Integer.valueOf((pageNo.intValue() - 1) * pageSize.intValue());
/* 78 */     requestParam.put(offsetField, offset);
/*    */   }
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\pagination-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.PaginationPlugin
 * JD-Core Version:    0.6.2
 */