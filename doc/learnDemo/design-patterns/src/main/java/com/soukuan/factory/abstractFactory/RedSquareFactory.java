package com.soukuan.factory.abstractFactory;

public class RedSquareFactory extends AbstractFactory {

    public Color getColor(String colorType){
       return new Red();
   }

    @Override
    public Shape getShape(String shape) {
        return new Square();
    }

}
