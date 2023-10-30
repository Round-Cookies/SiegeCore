package me.asakura_kukii.siegecore.trigger;

import me.asakura_kukii.siegecore.SiegeCore;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class PTask extends BukkitRunnable {

    public boolean flagLifeTime = true;

    public Long lifeTime = 10L;

    public boolean flagInit = true;

    public boolean flagAlive = true;

    public abstract void init();

    public abstract void hold();

    public abstract void goal();

    public void stop() {
        if (!flagAlive) return;
        cancel();
        goal();
        flagAlive = false;
    }

    public void setLifeTime(Long lifeTime) {
        this.lifeTime = lifeTime;
    }

    public boolean isFlagAlive() {
        return flagAlive;
    }

    public PTask runPTask() {
        this.flagLifeTime = false;
        this.runTaskTimer(SiegeCore.pluginInstance, 0, 1);
        return this;
    }

    public PTask runPTask(Long lifeTime) {
        this.flagLifeTime = true;
        this.lifeTime = lifeTime;
        this.runTaskTimer(SiegeCore.pluginInstance, 0, 1);
        return this;
    }

    @Override
    public void run() {
        if (this.flagLifeTime) {
            this.lifeTime--;
        }
        if (flagInit) {
            flagInit = false;
            init();
        } else {
            hold();
        }
        if (this.flagLifeTime && this.lifeTime <= 0) {
            stop();
        }
    }
}
