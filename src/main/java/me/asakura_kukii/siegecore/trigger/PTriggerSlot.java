package me.asakura_kukii.siegecore.trigger;

public enum PTriggerSlot {
    MAIN(-1),
    OFF(40),
    HEAD(36),
    CHEST(37),
    LEGS(38),
    FEET(39);

    PTriggerSlot(int slot) {
        this.slot = slot;
    }

    public int slot = 0;
}
