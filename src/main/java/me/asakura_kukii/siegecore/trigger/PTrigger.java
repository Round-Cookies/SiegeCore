package me.asakura_kukii.siegecore.trigger;

import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.item.PAbstractItem;
import me.asakura_kukii.siegecore.player.PPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
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

    public static Set<ItemStack> getEquipmentSet(Player p) {
        Set<ItemStack> equipmentSet = new HashSet<>();
        if (p.isDead()) return equipmentSet;
        equipmentSet.add(p.getInventory().getItemInMainHand());
        equipmentSet.add(p.getInventory().getItemInOffHand());
        equipmentSet.add(p.getInventory().getHelmet());
        equipmentSet.add(p.getInventory().getChestplate());
        equipmentSet.add(p.getInventory().getLeggings());
        equipmentSet.add(p.getInventory().getBoots());
        return equipmentSet;
    }

    public static void update() {
        PType pT = PType.getPType(PPlayer.class);
        if (pT == null) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (pT.getPFile(p.getName()) == null) {
                pT.createPFile(p.getName());
            }
            PPlayer pP = (PPlayer) pT.getPFile(p.getName());
            Set<ItemStack> equipmentSet = getEquipmentSet(p);
            Set<ItemStack> equipSet = new HashSet<>(equipmentSet);
            equipSet.removeAll(pP.equipmentSet);
            for (ItemStack iS : equipSet) {
                PAbstractItem.checkTrigger(p, PTriggerType.EQUIP, PTriggerSubType.INIT, iS, -1);
            }
            pP.equipmentSet.removeAll(equipmentSet);
            for (ItemStack iS : pP.equipmentSet) {
                PAbstractItem.checkTrigger(p, PTriggerType.STOCK, PTriggerSubType.INIT, iS, -1);
            }
            pP.equipmentSet.clear();
            pP.equipmentSet.addAll(equipmentSet);

            //PTrigger.trigger(p, PTriggerType.TICK, PTriggerSubType.HOLD);
        }
    }
}
