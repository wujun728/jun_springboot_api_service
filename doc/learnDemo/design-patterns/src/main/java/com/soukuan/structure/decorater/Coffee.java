package com.soukuan.structure.decorater;

public class Coffee implements Drink {
    final private String description = "coffee";
    //每杯 coffee 售价 10 元
    public float cost() {
        return 10;
    }

    public String getDescription() {
        return description;
    }
}