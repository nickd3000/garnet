package com.physmo.garnet.toolkit.simplecollision;

public class RelativeObject {
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getOriginX() {
        return originX;
    }

    public void setOriginX(double originX) {
        this.originX = originX;
    }

    public double getOriginY() {
        return originY;
    }

    public void setOriginY(double originY) {
        this.originY = originY;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public Collidable getOtherObject() {
        return otherObject;
    }

    public void setOtherObject(Collidable otherObject) {
        this.otherObject = otherObject;
    }

    public Collidable getThisObject() {
        return thisObject;
    }

    public void setThisObject(Collidable thisObject) {
        this.thisObject = thisObject;
    }

    public double distance;
    public double originX;
    public double originY;
    public double dx;
    public double dy;
    public Collidable otherObject;
    public Collidable thisObject;
}
