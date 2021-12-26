package com.physmo.garnet.input;

/*
action:
    GLFW_RELEASE = 0;
    GLFW_PRESS = 1;
    GLFW_REPEAT = 2;

key:
    GLFW_KEY_SPACE = 32;
    GLFW_KEY_0 = 48;
    GLFW_KEY_A = 65;
*/

public interface KeyboardCallback {
    void invoke(int key, int scancode, int action, int mods);
}

