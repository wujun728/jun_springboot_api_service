package com.soukuan.behavior.command;

public class LightOffCommand implements Command{
    private Receiver receiver;

    public LightOffCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        this.receiver.process();
    }
    
}