package com.soukuan.factory.abstractFactory;

public class FactoryProducer {

   public static AbstractFactory getFactory(String choice){
      if(choice.equalsIgnoreCase("SHAPE")){
         return new ShapeFactory();
      } else if(choice.equalsIgnoreCase("COLOR")){
         return new ColorFactory();
      } else if(choice.equalsIgnoreCase("RED_SQUARE")){
         return new RedSquareFactory();
      }
      return null;
   }

}