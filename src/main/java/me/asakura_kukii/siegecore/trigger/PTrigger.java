package me.asakura_kukii.siegecore.trigger;

import me.asakura_kukii.siegecore.item.PAbstractItem;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
}
