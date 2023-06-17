package com.physmo.garnet.graphics;

public class CameraManager {
    public static final int MAX_CAMERAS = 101;
    public static final int DEBUG_CAMERA = MAX_CAMERAS - 1;
    public double xOffset;
    public double yOffset;
    Camera[] cameras;
    int windowWidth;
    int windowHeight;
    int activeCameraId = -1;


    public CameraManager(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        cameras = new Camera[MAX_CAMERAS];

        for (int i = 0; i < MAX_CAMERAS; i++) {
            cameras[i] = new Camera(i, windowWidth, windowHeight);
        }

        setActiveCamera(0);
    }

    public void refreshCurrentValues() {
        Camera camera = getCamera(activeCameraId);
        xOffset = camera.getX();
        yOffset = camera.getY();
    }

    public Camera getCamera(int id) {
        if (id < 0 || id >= MAX_CAMERAS) throw new RuntimeException("Max cameras exceeded: " + MAX_CAMERAS);
        return cameras[id];
    }

    public Camera getActiveCamera() {
        return getCamera(activeCameraId);
    }

    void setActiveCamera(int id) {
        if (id == activeCameraId) return;
        if (id >= MAX_CAMERAS) throw new RuntimeException("Max cameras exceeded: " + MAX_CAMERAS);
        activeCameraId = id;
        refreshCurrentValues();
    }

    public int getActiveCameraId() {
        return activeCameraId;
    }
}
