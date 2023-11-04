package me.asakura_kukii.siegecore.io;

import me.asakura_kukii.siegecore.player.PAbstractPlayer;
import me.asakura_kukii.siegecore.trigger.PTriggerType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PTypeListener implements org.bukkit.event.Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        for (PType pT : PType.getPlayerPTypeList()) {
            String id = e.getPlayer().getName();
            if (pT.getPFile(id) == null) {
                pT.createPFile(id);
            } else {
                pT.getPFile(id);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        for (PType pT : PType.getPlayerPTypeList()) {
            String id = e.getPlayer().getName();
            if (pT.getPFile(id) == null) {
                pT.createPFile(id);
            } else {
                pT.getPFile(id);
            }
        }
    }
}
