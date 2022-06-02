package com.soukuan.factory.abstractFactory;

public class ColorFactory extends AbstractFactory {

    public Color getColor(String colorType){
       if(colorType == null){
           return null;
       }
       if("BLUE".equalsIgnoreCase(colorType)){
           return new Blue();
       } else if("RED".equalsIgnoreCase(colorType)){
           return new Red();
       } else if("GREEN".equalsIgnoreCase(colorType)){
           return new Green();
       }
       return null;
   }

    @Override
    public Shape getShape(String shape) {
        return null;
    }

}
