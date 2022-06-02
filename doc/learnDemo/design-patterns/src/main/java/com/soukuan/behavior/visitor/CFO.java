package com.soukuan.behavior.visitor;

//财务主管类，查看账本的类之一，作用于高层的层次结构
public class CFO implements Viewer {

    //财务主管对每一个单子都要核对项目和金额
    public void viewAbstractBill(AbstractBill bill) {
        if(bill instanceof ConsumeBill){
            System.out.println("财务主管查看账本时，每一个都核对项目和金额, 消费金额是" + bill.getAmount() + "，项目是" + bill.getItem());
        }else if (bill instanceof IncomeBill){
            System.out.println("财务主管查看账本时，每一个都核对项目和金额，收入金额是" + bill.getAmount() + "，项目是" + bill.getItem());
        }
    }

}