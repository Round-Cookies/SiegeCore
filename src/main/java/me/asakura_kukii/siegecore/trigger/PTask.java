package me.asakura_kukii.siegecore.trigger;

import me.asakura_kukii.siegecore.SiegeCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class PTask extends BukkitRunnable {

    public boolean flagLifeTickTime = true;

    public Long lifeTickTime = 10L;

    public Long triggerTickTime = 0L;

    public boolean flagInit = true;

    public boolean flagAlive = true;

    public abstract void init();

    public abstract void hold();

    public abstract void goal();

    public void stop() {
        if (!flagAlive) return;
        this.lifeTickTime = 0L;
        cancel();
        goal();
        flagAlive = false;
    }

    public void forceStop() {
        cancel();
        flagAlive = false;
    }


    public void setLifeTickTime(Long lifeTickTime) {
        this.lifeTickTime = lifeTickTime;
    }

    public boolean isFlagAlive() {
        return flagAlive;
    }

    public PTask runPTask() {
        this.flagLifeTickTime = false;
        this.triggerTickTime = SiegeCore.runTickTime;
        this.runTaskTimer(SiegeCore.pluginInstance, 0, 1);
        return this;
    }

    public PTask runPTask(Long lifeTime) {
        this.flagLifeTickTime = true;
        this.triggerTickTime = SiegeCore.runTickTime;
        this.lifeTickTime = lifeTime;
        this.runTaskTimer(SiegeCore.pluginInstance, 0, 1);
        return this;
    }

    @Override
    public void run() {
        if (this.flagLifeTickTime) {
            this.lifeTickTime--;
        }
        if (flagInit) {
            flagInit = false;
            init();
            return;
        }
        if (this.flagLifeTickTime && this.lifeTickTime <= 0) {
            stop();
            return;
        }
        hold();
    }
}
