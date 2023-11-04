package me.asakura_kukii.siegecore.trigger;

import me.asakura_kukii.siegecore.SiegeCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PTriggerListener implements org.bukkit.event.Listener {

    public static HashMap<String, PTask> pTaskMap = new HashMap<>();

    public static Set<UUID> dropBlockUUIDSet = new HashSet<>();
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
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (dropBlockUUIDSet.contains(e.getPlayer().getUniqueId())) {
                dropBlockUUIDSet.remove(e.getPlayer().getUniqueId());
                return;
            }
            e.setCancelled(triggerEvent(e.getPlayer(), PTriggerType.LEFT));
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            e.setCancelled(triggerEvent(e.getPlayer(), PTriggerType.RIGHT));
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            e.setCancelled(triggerEvent((Player) e.getDamager(), PTriggerType.LEFT));
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        e.setCancelled(triggerEvent(e.getPlayer(), PTriggerType.RIGHT));
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        e.setCancelled(triggerEvent(e.getPlayer(), PTriggerType.LEFT));
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        e.setCancelled(triggerEvent(e.getPlayer(), PTriggerType.RIGHT));
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        e.setCancelled(triggerEvent(e.getPlayer(), PTriggerType.SWAP));
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        toggleEvent(e.getPlayer(), PTriggerType.SNEAK, e.getPlayer().isSneaking());
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent e) {
        toggleEvent(e.getPlayer(), PTriggerType.SPRINT, e.getPlayer().isSprinting());
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
                    if (!previousState) {
                        PTrigger.trigger(p, pTT, PTriggerSubType.INIT);
                    } else {
                        PTrigger.trigger(p, pTT, PTriggerSubType.GOAL);
                    }
                }

                @Override
                public void hold() {
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
                    PTrigger.trigger(p, pTT, PTriggerSubType.INIT);
                }

                @Override
                public void hold() {
                    PTrigger.trigger(p, pTT, PTriggerSubType.HOLD);
                }

                @Override
                public void goal() {
                    PTrigger.trigger(p, pTT, PTriggerSubType.GOAL);
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
                public void hold() {
                }

                @Override
                public void goal() {
                }
            }.runPTask(1L);
            return PTrigger.checkTriggerCancel(p, pTT);
        }
        if (pTaskMap.containsKey(key)) {
            pTaskMap.get(key).setLifeTime(pTT.holdDetectDelay);
        } else {
            pTaskMap.put(key, new PTask() {
                @Override
                public void init() {
                    PTrigger.trigger(p, pTT, PTriggerSubType.INIT);
                }

                @Override
                public void hold() {
                    PTrigger.trigger(p, pTT, PTriggerSubType.HOLD);
                }

                @Override
                public void goal() {
                    PTrigger.trigger(p, pTT, PTriggerSubType.GOAL);
                    pTaskMap.remove(key);
                }
            }.runPTask(pTT.holdDetectDelay));
        }
        return PTrigger.checkTriggerCancel(p, pTT);
    }
}
