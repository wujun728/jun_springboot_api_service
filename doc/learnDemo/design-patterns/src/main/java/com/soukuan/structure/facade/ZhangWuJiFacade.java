package com.soukuan.structure.facade;

/**
 * 外观类张无忌
 */
public class ZhangWuJiFacade {
    private JingMai jingMai;
    private ZhaoShi zhaoShi;
    private NeiGong neiGong;

    public ZhangWuJiFacade(){
        jingMai=new JingMai();
        zhaoShi=new ZhaoShi();
        neiGong=new NeiGong();
    }
    /**
     * 使用乾坤大挪移
     */
    public void Qiankun(){
        jingMai.jingmai();//开启经脉
        neiGong.QianKun();//使用内功乾坤大挪移

    }
    /**
     * 使用七伤拳
     */
    public void QiShang(){
        jingMai.jingmai(); //开启经脉
        neiGong.JiuYang();//使用内功九阳神功
        zhaoShi.QiShangQuan();//使用招式七伤拳
    }
}