package com.qinnuan.engine.xmpp.message;

/**
 * Created by showu on 13-7-9.
 */
public enum MessageSRC {
    TO(0), FROME(1);
    private int value;

    MessageSRC(int i) {
        this.value = i;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
