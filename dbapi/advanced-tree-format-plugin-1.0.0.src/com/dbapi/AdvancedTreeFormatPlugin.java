/*    */ package com.dbapi;
/*    */ 
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.dbapi.entity.GroupingParam;
/*    */ import com.dbapi.util.CustomeTreeUtils;
/*    */ import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class AdvancedTreeFormatPlugin extends TransformPlugin
/*    */ {
/*    */   public void init()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Object transform(Object data, String param)
/*    */   {
/* 20 */     if (!(data instanceof List)) {
/*    */       throw new IllegalArgumentException("AdvancedTreeFormatPlugin expects List data, but got: " + (data == null ? "null" : data.getClass().getName()));
/*    */     }
/* 20 */     List source = (List)data;
/* 21 */     List groupingParams = JSON.parseArray(param, GroupingParam.class);
/* 22 */     List list = CustomeTreeUtils.groupData(source, groupingParams, new ArrayList());
/* 23 */     return list;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 28 */     return "高级树状数据结构插件";
/*    */   }
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 33 */     return "将SQL查询结果转换成树状结构，可设置每一层级的分组字段和取值字段";
/*    */   }
/*    */ 
/*    */   public String getParamDescription()
/*    */   {
/* 38 */     return "[{\"groupBy\":\"xxx\",\"childrenName\":\"yyy\",\"fields\":[\"nn\",\"mmm\",...]},...]";
/*    */   }
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\advanced-tree-format-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.AdvancedTreeFormatPlugin
 * JD-Core Version:    0.6.2
 */