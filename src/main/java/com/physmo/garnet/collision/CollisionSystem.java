package com.physmo.garnet.collision;

import com.physmo.garnet.entity.Component;
import com.physmo.garnet.entity.Entity;
import com.physmo.garnet.entity.EntityGroup;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;

public class CollisionSystem {

    List<CollisionPacket> collisions;

    public void tick(EntityGroup entityGroup) {
        collisions = new ArrayList<>();
        List<Entity> entityList = entityGroup.getAll();

        for (Entity entityA : entityList) {
            if (entityA.getActive()==false) continue;
            List<Collider> collidersA = entityA.getColliders();
            if (collidersA==null || collidersA.size()==0) continue;
            for (Entity entityB : entityList) {
                if (entityB.getActive()==false) continue;
                if (entityA==entityB) continue;
                List<Collider> collidersB = entityB.getColliders();
                if (collidersB==null || collidersB.size()==0) continue;
                processColliders(entityA,entityB);
            }
        }

        for (CollisionPacket collision : collisions) {
            sendCollisionNotifications(collision);
        }

    }

    public void processColliders(Entity entityA, Entity entityB) {
        List<Collider> collidersA = entityA.getColliders();
        List<Collider> collidersB = entityB.getColliders();
        for (Collider colA : collidersA) {
            for (Collider colB : collidersB) {
                boolean colliding = colA.testCollision(colB);
                if (colliding) {
                    collisions.add(new CollisionPacket(entityA, entityB));
                }
            }
        }

    }

    public void sendCollisionNotifications(CollisionPacket collisionPacket) {

        for (Component component : collisionPacket.sourceEntity.getComponents()) {
            component.onCollisionStart(collisionPacket);
        }
        for (Component component : collisionPacket.targetEntity.getComponents()) {
            component.onCollisionStart(collisionPacket);
        }
    }

    public void debugRender(EntityGroup entityGroup) {
        glColor3f(0.2f, 1.0f, 0.2f);
        glDisable(GL_TEXTURE_2D);

        for (Entity entity : entityGroup.getAll()) {
            for (Collider collider : entity.getColliders()) {
                collider.render();
            }
        }
    }
}
