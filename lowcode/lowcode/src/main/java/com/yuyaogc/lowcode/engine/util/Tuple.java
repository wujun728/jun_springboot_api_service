package com.yuyaogc.lowcode.engine.util;

import java.io.Serializable;

/**
 */
public class  Tuple<A, B> implements Serializable {
    private static final long serialVersionUID = -1L;

    A item1;
    B item2;

    public Tuple(A item1, B item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public A getItem1() {
        return item1;
    }

    public B getItem2() {
        return item2;
    }
}
