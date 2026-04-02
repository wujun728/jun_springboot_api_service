/*    */ package com.dbapi;
/*    */ 
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class TreePlugin extends TransformPlugin
/*    */ {
/*    */   public void init()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Object transform(Object data, String param)
/*    */   {
/* 18 */     TreeUtil treeUtil = new TreeUtil();
/* 19 */     if (StringUtils.isNotBlank(param)) {
/* 20 */       JSONObject jsonObject = JSON.parseObject(param);
/* 21 */       if (StringUtils.isNotBlank(jsonObject.getString("idField"))) {
/* 22 */         treeUtil.setIdField(jsonObject.getString("idField"));
/*    */       }
/* 24 */       if (StringUtils.isNotBlank(jsonObject.getString("parentIdField"))) {
/* 25 */         treeUtil.setIdField(jsonObject.getString("parentIdField"));
/*    */       }
/*    */     }
/* 28 */     if (!(data instanceof List)) {
/*    */       throw new IllegalArgumentException("TreePlugin expects List data, but got: " + (data == null ? "null" : data.getClass().getName()));
/*    */     }
/* 28 */     List list = (List)data;
/* 29 */     List jsonObjects = treeUtil.listToTree(list);
/* 30 */     return jsonObjects;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 34 */     return "树状结构数据插件";
/*    */   }
/*    */ 
/*    */   public String getDescription() {
/* 38 */     return "将sql查询结果转换成树状结构（父子结构），可以递归转换任意多级";
/*    */   }
/*    */ 
/*    */   public String getParamDescription() {
/* 42 */     return "参数格式是{\"idField\":\"xxx\",\"parentIdField\":\"xxx\"}，指定数据的id字段名和父级id字段名，如果不传参数，将默认使用\"id\" \"parent_id\"作为id字段名和父级id字段名";
/*    */   }
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\tree-format-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.TreePlugin
 * JD-Core Version:    0.6.2
 */