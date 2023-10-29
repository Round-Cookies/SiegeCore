package me.asakura_kukii.siegecore.trigger;

import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PTrigger {

    public PTrigger(Player p, PTriggerType pTT, PTriggerSubType pTST) {
        this.pTT = pTT;
        this.pTST = pTST;
    }

    public void trigger() {

    }

    public LivingEntity lE;
    public PTriggerType pTT;
    public PTriggerSubType pTST;

    public ItemStack iS;
    public int slot;
    public boolean cancel = false;
}
