package me.asakura_kukii.siegecore.trigger;

import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.item.PAbstractItem;
import me.asakura_kukii.siegecore.player.PPlayer;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PTrigger {

    public static boolean checkTriggerCancel(Player p, PTriggerType pTT) {
        // for performance, check one by one
        if (PAbstractItem.checkTriggerCancel(pTT, p.getInventory().getItemInMainHand())) return true;
        if (PAbstractItem.checkTriggerCancel(pTT, p.getInventory().getItemInOffHand())) return true;
        if (PAbstractItem.checkTriggerCancel(pTT, p.getInventory().getHelmet())) return true;
        if (PAbstractItem.checkTriggerCancel(pTT, p.getInventory().getChestplate())) return true;
        if (PAbstractItem.checkTriggerCancel(pTT, p.getInventory().getLeggings())) return true;
        if (PAbstractItem.checkTriggerCancel(pTT, p.getInventory().getBoots())) return true;
        return false;
    }

    public static void trigger(Player p, PTriggerType pTT, PTriggerSubType pTST) {
        PAbstractItem.checkTrigger(p, pTT, pTST, p.getInventory().getItemInMainHand(), p.getInventory().getHeldItemSlot());
        PAbstractItem.checkTrigger(p, pTT, pTST, p.getInventory().getItemInOffHand(), 40);
        PAbstractItem.checkTrigger(p, pTT, pTST, p.getInventory().getHelmet(), 36);
        PAbstractItem.checkTrigger(p, pTT, pTST, p.getInventory().getChestplate(), 37);
        PAbstractItem.checkTrigger(p, pTT, pTST, p.getInventory().getLeggings(), 38);
        PAbstractItem.checkTrigger(p, pTT, pTST, p.getInventory().getBoots(), 39);
    }

    public static void update() {
        PType pT = PType.getPType(PPlayer.class);
        if (pT == null) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            PPlayer pP = (PPlayer) pT.getPFile(p.getName());
            if (pP == null) continue;
            Pair<HashMap<Integer, ItemStack>, HashMap<Integer, ItemStack>> iSP = pP.updateEquipmentList();
            HashMap<Integer, ItemStack> stockItemStackMap = iSP.getLeft();
            HashMap<Integer, ItemStack> equipItemStackMap = iSP.getRight();
            for (Integer key : stockItemStackMap.keySet()) {
                PAbstractItem.checkTrigger(p, PTriggerType.STOCK, PTriggerSubType.INIT, stockItemStackMap.get(key), key);
            }
            for (Integer key : equipItemStackMap.keySet()) {
                PAbstractItem.checkTrigger(p, PTriggerType.EQUIP, PTriggerSubType.INIT, equipItemStackMap.get(key), key);
            }
            //PTrigger.trigger(p, PTriggerType.TICK, PTriggerSubType.HOLD);
        }
    }
}
