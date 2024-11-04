package com.physmo.garnet.toolkit.support;

import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.toolkit.Component;

public class MonsterLogicComponent extends Component {
    @Override
    public void tick(double t) {

    }

    @Override
    public void init() {

    }

    @Override
    public void draw(Graphics g) {

    }

    public InventoryComponent accessInventoryComponent() {
        InventoryComponent inventoryComponent = parent.getComponent(InventoryComponent.class);
        return inventoryComponent;
    }
}
