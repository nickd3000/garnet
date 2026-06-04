package com.physmo.garnet.toolkit;

import com.physmo.garnet.toolkit.support.InventoryComponent;
import com.physmo.garnet.toolkit.support.Monster;
import com.physmo.garnet.toolkit.support.MonsterLogicComponent;
import com.physmo.garnet.toolkit.support.MoveRightComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ComponentTest {

    @Test
    void componentCanAccessSiblingComponentThroughParentGameObject() {
        Monster monster = new Monster("monster");
        monster.addComponent(new MonsterLogicComponent());
        monster.addComponent(new MoveRightComponent());
        monster.addComponent(new InventoryComponent());

        MonsterLogicComponent monsterLogicComponent = monster.getComponent(MonsterLogicComponent.class);
        InventoryComponent inventoryComponent = monsterLogicComponent.accessInventoryComponent();

        assertNotNull(inventoryComponent);
        assertInstanceOf(InventoryComponent.class, inventoryComponent);
    }
}
