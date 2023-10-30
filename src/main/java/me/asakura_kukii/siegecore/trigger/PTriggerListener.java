package me.asakura_kukii.siegecore.trigger;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PTriggerListener implements org.bukkit.event.Listener {

    public static HashMap<String, PTask> pTaskMap = new HashMap<>();

    public static Set<UUID> dropBlockUUIDSet = new HashSet<>();

    public static final Long holdDetectDelay = 5L;

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        for (PTriggerType pTT : PTriggerType.values()) {
            String key = e.getEntity().getUniqueId() + pTT.name();
            if (pTaskMap.containsKey(key)) {
                pTaskMap.get(key).stop();
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        for (PTriggerType pTT : PTriggerType.values()) {
            String key = e.getPlayer().getUniqueId() + pTT.name();
            if (pTaskMap.containsKey(key)) {
                pTaskMap.get(key).stop();
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (dropBlockUUIDSet.contains(e.getPlayer().getUniqueId())) {
                dropBlockUUIDSet.remove(e.getPlayer().getUniqueId());
                return;
            }
            triggerEvent(e.getPlayer(), PTriggerType.LEFT);
        }
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            triggerEvent(e.getPlayer(), PTriggerType.RIGHT);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            triggerEvent((Player) e.getDamager(), PTriggerType.LEFT);
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        triggerEvent(e.getPlayer(), PTriggerType.RIGHT);
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        triggerEvent(e.getPlayer(), PTriggerType.SWAP);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        toggleEvent(e.getPlayer(), PTriggerType.SNEAK, e.getPlayer().isSneaking());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        triggerEvent(e.getPlayer(), PTriggerType.DROP);
        dropBlockUUIDSet.add(e.getPlayer().getUniqueId());
    }

    public boolean toggleEvent(Player p, PTriggerType pTT, boolean previousState) {
        String key = p.getUniqueId() + pTT.name();
        if (!pTT.flagHold) {
            new PTask() {
                @Override
                public void init() {
                    p.sendMessage("INIT - " + pTT.name());
                }

                @Override
                public void tick() {
                }

                @Override
                public void goal() {
                }
            }.runPTask(1L);
            return false;
        }
        if (!previousState) {
            if (pTaskMap.containsKey(key)) {
                pTaskMap.get(key).stop();
            }
            pTaskMap.put(key, new PTask() {
                @Override
                public void init() {
                    Bukkit.broadcastMessage("INIT - " + pTT.name());
                }

                @Override
                public void tick() {
                    Bukkit.broadcastMessage("TICK - " + pTT.name());
                }

                @Override
                public void goal() {
                    Bukkit.broadcastMessage("GOAL - " + pTT.name());
                    pTaskMap.remove(key);
                }
            }.runPTask());
        } else {
            if (pTaskMap.containsKey(key)) {
                pTaskMap.get(key).stop();
            }
        }
        return false;
    }

    public boolean triggerEvent(Player p, PTriggerType pTT) {
        String key = p.getUniqueId() + pTT.name();
        if (!pTT.flagHold) {
            new PTask() {
                @Override
                public void init() {
                    PTrigger.trigger(p, pTT, PTriggerSubType.INIT);
                }

                @Override
                public void tick() {
                }

                @Override
                public void goal() {
                }
            }.runPTask(1L);
            return false;
        }
        if (pTaskMap.containsKey(key)) {
            pTaskMap.get(key).setLifeTime(holdDetectDelay);
        } else {
            pTaskMap.put(key, new PTask() {
                @Override
                public void init() {
                    p.sendMessage("INIT - " + pTT.name());
                }

                @Override
                public void tick() {
                    p.sendMessage("TICK - " + pTT.name());
                }

                @Override
                public void goal() {
                    p.sendMessage("GOAL - " + pTT.name());
                    pTaskMap.remove(key);
                }
            }.runPTask(holdDetectDelay));
        }
        return false;
    }
}
