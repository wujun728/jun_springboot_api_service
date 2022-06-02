package com.soukuan.structure.decorater;

public class Milk extends CondimentDecorator {
    public Milk(Drink decoratorDrink) {
        super(decoratorDrink);
    }

    @Override
    public float cost() {
        return super.cost() + 2;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " milk";
    }
}