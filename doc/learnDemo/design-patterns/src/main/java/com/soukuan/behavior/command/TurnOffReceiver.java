package com.soukuan.behavior.command;

public class TurnOffReceiver implements Receiver{

    @Override
    public void process() {
        System.out.println("执行关灯操作~");
    }
}