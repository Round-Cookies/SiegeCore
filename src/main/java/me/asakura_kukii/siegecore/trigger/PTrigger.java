package me.asakura_kukii.siegecore.trigger;

import me.asakura_kukii.siegecore.SiegeCore;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.item.PAbstractItem;
import me.asakura_kukii.siegecore.player.PPlayer;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PTrigger {

    public static boolean checkTriggerCancel(Player p, PTriggerType pTT) {
        // for performance, check one by one
        if (PAbstractItem.checkTriggerCancel(pTT, PTriggerSlot.MAIN, p.getInventory().getItemInMainHand())) return true;
        if (PAbstractItem.checkTriggerCancel(pTT, PTriggerSlot.OFF, p.getInventory().getItemInOffHand())) return true;
        if (PAbstractItem.checkTriggerCancel(pTT, PTriggerSlot.HEAD, p.getInventory().getHelmet())) return true;
        if (PAbstractItem.checkTriggerCancel(pTT, PTriggerSlot.CHEST, p.getInventory().getChestplate())) return true;
        if (PAbstractItem.checkTriggerCancel(pTT, PTriggerSlot.LEGS, p.getInventory().getLeggings())) return true;
        if (PAbstractItem.checkTriggerCancel(pTT, PTriggerSlot.FEET, p.getInventory().getBoots())) return true;
        return false;
    }

    public static void trigger(Player p, PTriggerType pTT, PTriggerSubType pTST, long triggerTickTime) {
        PType pT = PType.getPType(PPlayer.class);
        if (pT == null) return;
        PPlayer pP = (PPlayer) pT.getPFileSafely(p.getUniqueId().toString());
        if (pP == null) return;
        // due to the priority of bukkit tasks, some modifications must be made
        long mainHoldTickTime = SiegeCore.runTickTime - Math.max(pP.getEquipmentTickTime(PTriggerSlot.MAIN), triggerTickTime) - 1;
        if (mainHoldTickTime < 0) mainHoldTickTime = 0;
        long offHoldTickTime = SiegeCore.runTickTime - Math.max(pP.getEquipmentTickTime(PTriggerSlot.OFF), triggerTickTime) - 1;
        if (offHoldTickTime < 0) offHoldTickTime = 0;
        PAbstractItem.checkTrigger(p, pTT, pTST, PTriggerSlot.MAIN, p.getInventory().getItemInMainHand(), mainHoldTickTime);
        PAbstractItem.checkTrigger(p, pTT, pTST, PTriggerSlot.OFF, p.getInventory().getItemInOffHand(), offHoldTickTime);

        long headHoldTickTime = SiegeCore.runTickTime - Math.max(pP.getEquipmentTickTime(PTriggerSlot.HEAD), triggerTickTime) - 1;
        if (headHoldTickTime < 0) headHoldTickTime = 0;
        long chestHoldTickTime = SiegeCore.runTickTime - Math.max(pP.getEquipmentTickTime(PTriggerSlot.CHEST), triggerTickTime) - 1;
        if (chestHoldTickTime < 0) chestHoldTickTime = 0;
        long legsHoldTickTime = SiegeCore.runTickTime - Math.max(pP.getEquipmentTickTime(PTriggerSlot.LEGS), triggerTickTime) - 1;
        if (legsHoldTickTime < 0) legsHoldTickTime = 0;
        long feetHoldTickTime = SiegeCore.runTickTime - Math.max(pP.getEquipmentTickTime(PTriggerSlot.FEET), triggerTickTime) - 1;
        if (feetHoldTickTime < 0) feetHoldTickTime = 0;
        PAbstractItem.checkTrigger(p, pTT, pTST, PTriggerSlot.HEAD, p.getInventory().getHelmet(), headHoldTickTime);
        PAbstractItem.checkTrigger(p, pTT, pTST, PTriggerSlot.CHEST, p.getInventory().getChestplate(), chestHoldTickTime);
        PAbstractItem.checkTrigger(p, pTT, pTST, PTriggerSlot.LEGS, p.getInventory().getLeggings(), legsHoldTickTime);
        PAbstractItem.checkTrigger(p, pTT, pTST, PTriggerSlot.FEET, p.getInventory().getBoots(), feetHoldTickTime);
    }

    public static void update() {
        PType pT = PType.getPType(PPlayer.class);
        if (pT == null) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            PPlayer pP = (PPlayer) pT.getPFileSafely(p.getUniqueId().toString());
            if (pP == null) continue;
            Pair<HashMap<PTriggerSlot, Pair<String, ItemStack>>, HashMap<PTriggerSlot, Pair<String, ItemStack>>> iSP = pP.updateEquipmentList();
            HashMap<PTriggerSlot, Pair<String, ItemStack>> stockItemStackMap = iSP.getLeft();
            HashMap<PTriggerSlot, Pair<String, ItemStack>> equipItemStackMap = iSP.getRight();
            for (PTriggerSlot slot : stockItemStackMap.keySet()) {
                ItemStack iS = stockItemStackMap.get(slot).getRight();
                if (iS == null || iS.getType() == Material.AIR) {
                    iS = recoverItemStack(p, stockItemStackMap.get(slot).getLeft());
                }
                if (iS != null) PAbstractItem.checkTrigger(p, PTriggerType.STOCK, PTriggerSubType.INIT, slot, iS, 0);
                // special trigger toggle block
                if (iS != null && iS.getType().equals(Material.SHIELD)) PTriggerListener.toggleEvent(p, PTriggerType.BLOCK, true);
            }
            for (PTriggerSlot slot : equipItemStackMap.keySet()) {
                ItemStack iS = equipItemStackMap.get(slot).getRight();
                if (iS != null) PAbstractItem.checkTrigger(p, PTriggerType.EQUIP, PTriggerSubType.INIT, slot, equipItemStackMap.get(slot).getRight(), 0);
            }
        }
    }

    public static ItemStack recoverItemStack(Player p, String uuid) {
        if (uuid == null) return null;
        // cursor
        if (Objects.equals(PAbstractItem.getUUID(p.getOpenInventory().getCursor()), uuid)) {
            return p.getOpenInventory().getCursor();
        }
        // inventory
        for (ItemStack itemStack : p.getOpenInventory().getBottomInventory().getContents()) {
            if (Objects.equals(PAbstractItem.getUUID(itemStack), uuid)) {
                return itemStack;
            }
        }
        for (ItemStack itemStack : p.getOpenInventory().getTopInventory().getContents()) {
            if (Objects.equals(PAbstractItem.getUUID(itemStack), uuid)) {
                return itemStack;
            }
        }
        List<Entity> entityList = p.getNearbyEntities(1, 1, 1);
        for (Entity e : entityList) {
            if (e instanceof Item) {
                Item i = (Item) e;
                if (Objects.equals(PAbstractItem.getUUID(i.getItemStack()), uuid)) {
                    return i.getItemStack();
                }
            }
        }
        return null;
    }
}
