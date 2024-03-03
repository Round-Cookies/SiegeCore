package me.asakura_kukii.siegecore.trigger;

import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import me.asakura_kukii.siegecore.SiegeCore;
import me.asakura_kukii.siegecore.item.PAbstractItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.RegisteredListener;

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
    public void onDrop(PlayerDropItemEvent e) {
        // Block action if the item is locked
        if (PAbstractItem.getLock(e.getItemDrop().getItemStack())) e.setCancelled(true);
        // If player drops an item, a left click event will fire afterwards, block it using this set
        dropBlockUUIDSet.add(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        // Block action if the item is locked
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        if (PAbstractItem.getLock(e.getCurrentItem())) e.setCancelled(true);
        if (e.getAction() == InventoryAction.HOTBAR_SWAP) {
            if (PAbstractItem.getLock(e.getWhoClicked().getInventory().getItemInOffHand())) e.setCancelled(true);
        }
        // If the event is cancelled, no PlayerItemDropEvent will fire, so we should block left click event here
        if (e.getAction() == InventoryAction.DROP_ALL_SLOT || e.getAction() == InventoryAction.DROP_ONE_SLOT) {
            if (e.isCancelled()) dropBlockUUIDSet.add(e.getWhoClicked().getUniqueId());
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        // Block action if one of the two items is locked
        if (PAbstractItem.getLock(e.getMainHandItem())) e.setCancelled(true);
        if (PAbstractItem.getLock(e.getOffHandItem())) e.setCancelled(true);
        if (triggerEvent(e.getPlayer(), PTriggerType.SWAP)) e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (triggerEvent(e.getPlayer(), PTriggerType.LEFT)) e.setCancelled(true);
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (triggerEvent(e.getPlayer(), PTriggerType.RIGHT)) e.setCancelled(true);
            // special trigger toggle block
            if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SHIELD) || e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.SHIELD)) {
                toggleEvent(e.getPlayer(), PTriggerType.BLOCK, false);
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (triggerEvent((Player) e.getDamager(), PTriggerType.LEFT)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (triggerEvent(e.getPlayer(), PTriggerType.RIGHT)) e.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (triggerEvent(e.getPlayer(), PTriggerType.LEFT)) e.setCancelled(true);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        if (triggerEvent(e.getPlayer(), PTriggerType.RIGHT)) e.setCancelled(true);
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
    public void onStopUsingItem(PlayerStopUsingItemEvent e) {
        // special trigger toggle block
        if (e.getItem().getType().equals(Material.SHIELD)) toggleEvent(e.getPlayer(), PTriggerType.BLOCK, true);
    }

    public static boolean toggleEvent(Player p, PTriggerType pTT, boolean previousState) {
        String key = p.getUniqueId() + pTT.name();
        if (!pTT.flagHold) {
            new PTask() {
                @Override
                public void init() {
                    if (!previousState) {
                        PTrigger.trigger(p, pTT, PTriggerSubType.INIT, this.triggerTickTime);
                    } else {
                        PTrigger.trigger(p, pTT, PTriggerSubType.GOAL, this.triggerTickTime);
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
                    PTrigger.trigger(p, pTT, PTriggerSubType.INIT, this.triggerTickTime);
                }

                @Override
                public void hold() {
                    PTrigger.trigger(p, pTT, PTriggerSubType.HOLD, this.triggerTickTime);
                }

                @Override
                public void goal() {
                    if (this.lifeTickTime <= 0L) {
                        PTrigger.trigger(p, pTT, PTriggerSubType.GOAL, this.triggerTickTime);
                    }
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

    public static boolean triggerEvent(Player p, PTriggerType pTT) {
        String key = p.getUniqueId() + pTT.name();
        if (!pTT.flagHold) {
            new PTask() {
                @Override
                public void init() {
                    if (pTT == PTriggerType.LEFT) {
                        if (dropBlockUUIDSet.contains(p.getUniqueId())) {
                            dropBlockUUIDSet.remove(p.getUniqueId());
                            this.forceStop();
                            return;
                        }
                    }
                    PTrigger.trigger(p, pTT, PTriggerSubType.INIT, this.triggerTickTime);
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
            pTaskMap.get(key).setLifeTickTime(pTT.holdDetectDelay);
        } else {
            pTaskMap.put(key, new PTask() {
                @Override
                public void init() {
                    if (pTT == PTriggerType.LEFT) {
                        if (dropBlockUUIDSet.contains(p.getUniqueId())) {
                            dropBlockUUIDSet.remove(p.getUniqueId());
                            this.stop();
                            return;
                        }
                    }
                    PTrigger.trigger(p, pTT, PTriggerSubType.INIT, this.triggerTickTime);
                }

                @Override
                public void hold() {
                    PTrigger.trigger(p, pTT, PTriggerSubType.HOLD, this.triggerTickTime);
                }

                @Override
                public void goal() {
                    if (this.lifeTickTime <= 0L) {
                        PTrigger.trigger(p, pTT, PTriggerSubType.GOAL, this.triggerTickTime);
                    }
                    pTaskMap.remove(key);
                }
            }.runPTask(pTT.holdDetectDelay));
        }
        return PTrigger.checkTriggerCancel(p, pTT);
    }
}
