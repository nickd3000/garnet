package com.physmo.garnet.toolkit.support;

import com.physmo.garnet.toolkit.scene.Scene;

import java.util.List;

public class MainGameScene extends Scene {

    List<String> messageList;
    int tickCount = 0;

    public MainGameScene(String name) {
        super(name);
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }

    @Override
    public void init() {
        messageList.add("MainGameScene init");
    }

    @Override
    public void tick(double delta) {
        messageList.add("MainGameScene tick " + tickCount);
        tickCount++;
    }

    @Override
    public void draw() {
        messageList.add("MainGameScene draw");
    }

    @Override
    public void onMakeActive() {
        messageList.add("MainGameScene onMakeActive");
    }

    @Override
    public void onMakeInactive() {
        messageList.add("MainGameScene onMakeInactive");
    }
}
