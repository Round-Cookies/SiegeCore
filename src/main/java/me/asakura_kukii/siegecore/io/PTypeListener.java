package me.asakura_kukii.siegecore.io;

import me.asakura_kukii.siegecore.SiegeCore;
import me.asakura_kukii.siegecore.player.PAbstractPlayer;
import me.asakura_kukii.siegecore.trigger.PTriggerType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PTypeListener implements org.bukkit.event.Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        for (PType pT : PType.getPlayerPTypeList()) {
            String uuid = e.getPlayer().getUniqueId().toString();
            PFile pF = pT.getPFileSafely(uuid);
            pT.savePFile(pF);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        for (PType pT : PType.getPlayerPTypeList()) {
            String uuid = e.getPlayer().getUniqueId().toString();
            PFile pF = pT.getPFileSafely(uuid);
            pT.savePFile(pF);
        }
    }
}
