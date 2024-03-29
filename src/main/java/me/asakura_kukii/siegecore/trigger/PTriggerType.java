package me.asakura_kukii.siegecore.trigger;

public enum PTriggerType {
    LEFT(false, 5L),
    RIGHT(true, 5L),
    SWAP(true, 12L),
    DROP(false, 12L),
    SNEAK(true, 0L),
    SPRINT(false, 0L),
    TICK(false, 0L),
    EQUIP(false, 0L),
    STOCK(false, 0L),

    BLOCK(true, 0L)
    ;

    public boolean flagHold = false;

    public Long holdDetectDelay = 5L;

    PTriggerType(boolean flagHold, Long holdDetectDelay) {
        this.flagHold = flagHold;
        this.holdDetectDelay = holdDetectDelay;
    }
}
