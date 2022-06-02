package com.soukuan.behavior.visitor;

//超级访问者接口（它支持定义高层操作）
public interface Viewer{

    void viewAbstractBill(AbstractBill bill);

}