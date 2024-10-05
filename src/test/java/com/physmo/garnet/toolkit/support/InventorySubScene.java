package com.physmo.garnet.toolkit.support;

import com.physmo.garnet.toolkit.scene.Scene;
import com.physmo.garnet.toolkit.scene.SceneManager;

import java.util.List;

public class InventorySubScene extends Scene {

    List<String> messageList;
    int tickCount = 0;

    public InventorySubScene(String name) {
        super(name);
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }

    @Override
    public void init() {
        messageList.add("InventorySubScene init");
    }

    @Override
    public void tick(double delta) {
        messageList.add("InventorySubScene tick " + tickCount);
        tickCount++;
        if (tickCount == 3) {
            messageList.add("InventorySubScene requesting pop, name: " + this.getName());
            SceneManager.popSubScene(this.getName());
        }
    }

    @Override
    public void draw() {
        messageList.add("InventorySubScene draw");
    }

    @Override
    public void onMakeActive() {
        messageList.add("InventorySubScene onMakeActive");
    }

    @Override
    public void onMakeInactive() {
        messageList.add("InventorySubScene onMakeInactive");
    }
}
