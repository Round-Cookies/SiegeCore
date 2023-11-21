package me.asakura_kukii.siegecore.item;

import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.trigger.PTriggerSlot;
import me.asakura_kukii.siegecore.trigger.PTriggerSubType;
import me.asakura_kukii.siegecore.trigger.PTriggerType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class PItem extends PAbstractItem {

    public PItem(String id, File file, PType type) {
        super(id, file, type);
    }

    public PItem() {}

    @Override
    public void trigger(Player p, PTriggerType pTT, PTriggerSubType pTST, PTriggerSlot pTS, ItemStack iS) {
    }

    @Override
    public ItemStack finalizeGetItemStack(ItemStack iS) {
        return iS;
    }

    @Override
    public void finalizeDeserialization() {}

    @Override
    public void defaultValue() {
    }
}
