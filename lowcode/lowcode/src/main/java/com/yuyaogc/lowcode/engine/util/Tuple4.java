package com.yuyaogc.lowcode.engine.util;

/**
 */
public class Tuple4<A, B, C, D> {
    A item1;
    B item2;
    C item3;
    D item4;

    public Tuple4(A item1, B item2, C item3, D item4) {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
    }

    public A getItem1() {
        return item1;
    }

    public B getItem2() {
        return item2;
    }

    public C getItem3() {
        return item3;
    }

    public D getItem4() {
        return item4;
    }
}
