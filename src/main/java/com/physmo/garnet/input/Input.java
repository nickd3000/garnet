package com.physmo.garnet.input;

import com.physmo.garnet.Garnet;

import java.util.ArrayList;
import java.util.List;

public class Input {

    static Garnet garnet;
    static List<ButtonConfig> buttonConfigList;

    ;

    public static void init(Garnet _garnet) {

        garnet = _garnet;

        buttonConfigList = new ArrayList<>();

        buttonConfigList.add(new ButtonConfig(0, VirtualButton.LEFT));
        buttonConfigList.add(new ButtonConfig(2, VirtualButton.RIGHT));
        buttonConfigList.add(new ButtonConfig(49, VirtualButton.FIRE1));

        garnet.addKeyboardCallback((key, scancode, action, mods) -> {
            //System.out.println("keyboard handler" + scancode + "  " + action);

            // a=0, d=2, space =49 / action 1/0 down/up

            for (ButtonConfig buttonConfig : buttonConfigList) {
                if (scancode == buttonConfig.keyboardKey) {
                    if (action == 1) {
                        buttonConfig.pressed = true;
                    } else if (action == 0) {
                        buttonConfig.pressed = false;
                    }
                }
            }


        });

    }

    public static boolean isPressed(VirtualButton button) {
        for (ButtonConfig buttonConfig : buttonConfigList) {
            if (buttonConfig.virtualButton == button) {
                return buttonConfig.pressed;
            }
        }

        return false;
    }

    public boolean isPressedThisFrame(VirtualButton button) {

        return false;
    }

    public enum VirtualButton {
        LEFT, RIGHT, FIRE1, FIRE2
    }
}
