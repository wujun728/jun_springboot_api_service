/*    */ package com.dbapi;
/*    */ 
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class TreeUtil
/*    */ {
/* 12 */   private String idField = "id";
/* 13 */   private String parentIdField = "parent_id";
/*    */ 
/*    */   public void setIdField(String idField) {
/* 16 */     this.idField = idField;
/*    */   }
/*    */ 
/*    */   public void setParentIdField(String parentIdField) {
/* 20 */     this.parentIdField = parentIdField;
/*    */   }
/*    */ 
/*    */   public List<JSONObject> listToTree(List<JSONObject> list) {
/* 24 */     List trees = new ArrayList();
/* 25 */     for (JSONObject entity : list) {
/* 26 */       String parentId = entity.getString(this.parentIdField);
/* 27 */       if (parentId == null) {
/* 28 */         trees.add(findChildren(entity, list));
/*    */       }
/*    */     }
/* 31 */     return trees;
/*    */   }
/*    */ 
/*    */   private JSONObject findChildren(JSONObject entity, List<JSONObject> list) {
/* 35 */     for (JSONObject item : list) {
/* 36 */       if ((entity.containsKey(this.idField)) && (entity.getString(this.idField).equals(item.getString(this.parentIdField)))) {
/* 37 */         if (entity.getJSONArray("children") == null) {
/* 38 */           entity.put("children", new JSONArray());
/*    */         }
/* 40 */         entity.getJSONArray("children").add(findChildren(item, list));
/*    */       }
/*    */     }
/* 43 */     return entity;
/*    */   }
/*    */ 
/*    */   public static void main(String[] args) {
/* 47 */     String s = "[{\"id\":1,\"title\":\"test1\",\"parent_id\":null},{\"id\":2,\"title\":\"test2\",\"parent_id\":1},{\"id\":3,\"title\":\"test3\",\"parent_id\":1}]";
/* 48 */     List data = JSON.parseArray(s, JSONObject.class);
/* 49 */     List jsonObjects = new TreeUtil().listToTree(data);
/* 50 */     System.out.println(JSON.toJSONString(jsonObjects));
/*    */   }
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\tree-format-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.TreeUtil
 * JD-Core Version:    0.6.2
 */