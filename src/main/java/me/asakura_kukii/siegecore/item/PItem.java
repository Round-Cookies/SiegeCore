package me.asakura_kukii.siegecore.item;

import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.trigger.PTriggerSubType;
import me.asakura_kukii.siegecore.trigger.PTriggerType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class PItem extends PAbstractItem {

    public PItem(String id, File file, PType type) {
        super(id, file, type);
    }

    public PItem() {}

    @Override
    public void trigger(LivingEntity lE, PTriggerType pTT, PTriggerSubType pTST, ItemStack iS, int slot) {
        lE.sendMessage(pTT.name() + pTST.name() + iS.getType().name() + slot);
    }

    @Override
    public ItemStack finalizeGetItemStack(ItemStack iS) {
        return iS;
    }

    @Override
    public void finalizeDeserialization() {}
}
