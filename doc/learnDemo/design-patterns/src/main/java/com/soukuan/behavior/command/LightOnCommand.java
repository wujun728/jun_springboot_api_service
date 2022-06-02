package com.soukuan.behavior.command;

public class LightOnCommand implements Command{

    private Receiver receiver;

    public LightOnCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.process();
    }

    
}