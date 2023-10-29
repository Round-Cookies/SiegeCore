package me.asakura_kukii.siegecore.trigger;

public enum PTriggerType {
    LEFT(false),
    RIGHT(true),
    SWAP(true),
    DROP(false),
    SNEAK(true);

    public boolean flagHold = false;

    PTriggerType(boolean flagHold) {
        this.flagHold = flagHold;
    }
}
