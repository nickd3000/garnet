package com.physmo.garnet.toolkit.simplecollision;


/**
 * Represents a data packet for managing collision events between two Collidable entities.
 * This class is used to encapsulate details about the source and target entities involved
 * in a collision or related interaction.
 */
public class CollisionPacket {

    public Collidable sourceEntity;
    public Collidable targetEntity;

    public CollisionPacket(Collidable sourceEntity, Collidable targetEntity) {
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
    }
}
