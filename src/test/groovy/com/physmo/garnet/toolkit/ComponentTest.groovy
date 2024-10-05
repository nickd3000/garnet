package com.physmo.garnet.toolkit

import com.physmo.garnet.toolkit.support.InventoryComponent
import com.physmo.garnet.toolkit.support.Monster
import com.physmo.garnet.toolkit.support.MonsterLogicComponent
import com.physmo.garnet.toolkit.support.MoveRightComponent
import spock.lang.Specification


class ComponentTest extends Specification {

    def "Accessing sibling components within GameObject"() {
        given: "A monster with various components"
            Monster monster = new Monster("monster")
            monster.addComponent(new MonsterLogicComponent())
            monster.addComponent(new MoveRightComponent())
            monster.addComponent(new InventoryComponent())

        when: "The MonsterLogicComponent accesses the InventoryComponent"
            MonsterLogicComponent monsterLogicComponent = monster.getComponent(MonsterLogicComponent.class)
            InventoryComponent inventoryComponent = monsterLogicComponent.accessInventoryComponent()

        then: "The InventoryComponent should be accessible and of the correct type"
            inventoryComponent != null
            inventoryComponent instanceof InventoryComponent
    }
}
