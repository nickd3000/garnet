package com.physmo.garnet.toolkit;

/**
 * Interface for objects that can receive and handle messages.
 */
@FunctionalInterface
public interface MessageListener {
    /**
     * Called when a message is received.
     *
     * @param name The name of the message.
     * @param data Optional data associated with the message.
     */
    void onMessage(String name, Object data);
}
