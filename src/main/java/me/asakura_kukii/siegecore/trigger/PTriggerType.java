package me.asakura_kukii.siegecore.trigger;

public enum PTriggerType {
    LEFT(true, false),
    RIGHT(true, true),
    SWAP(true, true),
    SNEAK(true, true);

    public boolean flagClick = false;
    public boolean flagHold = false;

    PTriggerType(boolean flagClick, boolean flagHold) {
        this.flagClick = flagClick;
        this.flagHold = flagHold;
    }
}
