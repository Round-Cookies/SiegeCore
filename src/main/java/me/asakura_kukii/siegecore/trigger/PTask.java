package me.asakura_kukii.siegecore.trigger;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class PTask extends BukkitRunnable {

    public Long lifeTime = 10L;

    public abstract void tick();

    public abstract void goal();

    public void setLifeTime(Long lifeTime) {
        this.lifeTime = lifeTime;
    }

    @Override
    public void run() {
        this.lifeTime--;
        tick();
        if (this.lifeTime == 0) {
            goal();
            cancel();
        }
    }
}
