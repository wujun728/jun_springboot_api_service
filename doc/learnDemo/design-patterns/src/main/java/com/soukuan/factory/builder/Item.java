package com.soukuan.factory.builder;

/**
 * 食物条目和食物包装
 */
public interface Item {
   public String name();
   public Packing packing();
   public float price();    
}