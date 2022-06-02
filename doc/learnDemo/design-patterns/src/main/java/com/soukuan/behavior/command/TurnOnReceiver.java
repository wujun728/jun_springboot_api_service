package com.soukuan.behavior.command;

public class TurnOnReceiver implements Receiver {

    @Override
    public void process() {
        System.out.println("执行开灯操作~");
    }
}