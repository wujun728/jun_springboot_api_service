package com.soukuan.factory.builder;

import java.util.ArrayList;
import java.util.List;

public class MealBuilder {

   private List<Item> items = new ArrayList<>();

   public MealBuilder addItem(Item item){
      items.add(item);
      return this;
   }

   public float getCost(){
      float cost = 0.0f;
      for (Item item : items) {
         cost += item.price();
      }        
      return cost;
   }

   public MealBuilder showItems(){
      for (Item item : items) {
         System.out.print("Item : "+item.name());
         System.out.print(", Packing : "+item.packing().pack());
         System.out.println(", Price : "+item.price());
      }
      return this;
   }    
}