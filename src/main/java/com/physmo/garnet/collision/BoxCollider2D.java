package com.physmo.garnet.collision;

import com.physmo.garnet.Rect;
import com.physmo.garnet.entity.Entity;

public class BoxCollider2D implements Collider {

    Entity parent;
    double offsetX;
    double offsetY;
    double width;
    double height;

    public void setValues(Entity parent, double offsetX, double offsetY, double width, double height) {
        this.parent = parent;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
    }

    public Rect getWorldRect() {
        return new Rect(parent.position.x+offsetX,parent.position.y+offsetY, width,height);
    }

    @Override
    public boolean testCollision(Collider other) {
        Rect thisRect = getWorldRect();
        Rect otherRect = ((BoxCollider2D)other).getWorldRect();

        return thisRect.intersect(otherRect);
    }

}
