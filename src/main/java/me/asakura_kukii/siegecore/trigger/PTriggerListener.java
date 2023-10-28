package me.asakura_kukii.siegecore.trigger;

import me.asakura_kukii.siegecore.SiegeCore;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class PTriggerListener implements org.bukkit.event.Listener {
    // Combined UUID
    public static HashMap<String, BukkitTask> stateMap = new HashMap<>();

    public static PTask pT = null;

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (pT == null) {
            pT = new PTask() {
                @Override
                public void tick() {
                    e.getPlayer().sendMessage("Timer - " + lifeTime);
                }

                @Override
                public void goal() {

                }
            };
            pT.runTaskTimer(SiegeCore.pluginInstance, 0, 1);
        } else {
            pT.lifeTime = 10L;
        }
    }

    public boolean toggleEvent(LivingEntity lE, PTriggerType pTT, boolean previousState) {
        return false;
    }

    public boolean triggerEvent(LivingEntity lE, PTriggerType pTT) {
        boolean flagCancel = false;
        String key = lE.getUniqueId() + pTT.name();
        if (pTT.flagHold) {
            if (stateMap.containsKey(lE.getUniqueId() + pTT.name())) {

            } else {

            }
            // check whether this event is a hold event
            // if this event is a hold event, return immediately.
        }
        if (pTT.flagClick) {
            return false;
        }
        boolean result = false;
        // trigger the click, init 0 delay task (which is more responsive...)
        return flagCancel;
    }
}
