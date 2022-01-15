package com.physmo.garnet.collision;

import com.physmo.garnet.entity.Entity;

public class CollisionPacket {

    public Entity sourceEntity;
    public Entity targetEntity;

    public CollisionPacket(Entity sourceEntity, Entity targetEntity) {
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
    }
}
