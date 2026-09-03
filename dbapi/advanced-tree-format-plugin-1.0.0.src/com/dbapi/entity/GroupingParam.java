/*    */ package com.dbapi.entity;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class GroupingParam
/*    */ {
/*    */   private String groupBy;
/*    */   private String childrenName;
/* 15 */   private List<String> fields = new ArrayList();
/* 16 */   private List<String> orderBy = new ArrayList();
/*    */ 
/*    */   public String getGroupBy()
/*    */   {
/* 13 */     return this.groupBy; } 
/* 14 */   public String getChildrenName() { return this.childrenName; } 
/* 15 */   public List<String> getFields() { return this.fields; } 
/* 16 */   public List<String> getOrderBy() { return this.orderBy; }
/*    */ 
/*    */ 
/*    */   public GroupingParam setGroupBy(String groupBy)
/*    */   {
/* 10 */     this.groupBy = groupBy; return this; } 
/* 10 */   public GroupingParam setChildrenName(String childrenName) { this.childrenName = childrenName; return this; } 
/* 10 */   public GroupingParam setFields(List<String> fields) { this.fields = fields; return this; } 
/* 10 */   public GroupingParam setOrderBy(List<String> orderBy) { this.orderBy = orderBy; return this; } 
/* 10 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof GroupingParam)) return false; GroupingParam other = (GroupingParam)o; if (!other.canEqual(this)) return false; Object this$groupBy = getGroupBy(); Object other$groupBy = other.getGroupBy(); if (this$groupBy == null ? other$groupBy != null : !this$groupBy.equals(other$groupBy)) return false; Object this$childrenName = getChildrenName(); Object other$childrenName = other.getChildrenName(); if (this$childrenName == null ? other$childrenName != null : !this$childrenName.equals(other$childrenName)) return false; Object this$fields = getFields(); Object other$fields = other.getFields(); if (this$fields == null ? other$fields != null : !this$fields.equals(other$fields)) return false; Object this$orderBy = getOrderBy(); Object other$orderBy = other.getOrderBy(); return this$orderBy == null ? other$orderBy == null : this$orderBy.equals(other$orderBy); } 
/* 10 */   protected boolean canEqual(Object other) { return other instanceof GroupingParam; } 
/* 10 */   public int hashCode() { int PRIME = 59; int result = 1; Object $groupBy = getGroupBy(); result = result * 59 + ($groupBy == null ? 43 : $groupBy.hashCode()); Object $childrenName = getChildrenName(); result = result * 59 + ($childrenName == null ? 43 : $childrenName.hashCode()); Object $fields = getFields(); result = result * 59 + ($fields == null ? 43 : $fields.hashCode()); Object $orderBy = getOrderBy(); result = result * 59 + ($orderBy == null ? 43 : $orderBy.hashCode()); return result; } 
/* 10 */   public String toString() { return "GroupingParam(groupBy=" + getGroupBy() + ", childrenName=" + getChildrenName() + ", fields=" + getFields() + ", orderBy=" + getOrderBy() + ")"; }
/*    */ 
/*    */ }

/* Location:           D:\workspace\github\temp\dbapi\advanced-tree-format-plugin-1.0.0.jar
 * Qualified Name:     com.dbapi.entity.GroupingParam
 * JD-Core Version:    0.6.2
 */