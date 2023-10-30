package me.asakura_kukii.siegecore.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.trigger.PTriggerSubType;
import me.asakura_kukii.siegecore.trigger.PTriggerType;
import me.asakura_kukii.siegecore.util.nbt.PNBT;
import me.asakura_kukii.siegecore.util.nbt.PNBTType;
import me.asakura_kukii.siegecore.util.format.PFormat;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

public abstract class PAbstractItem extends PFile {
    public String name = "";
    public Material material = Material.COOKIE;
    public int customModelData = 0;
    public List<String> lore = new ArrayList<>();
    public Set<PTriggerType> triggerCancelSet = new HashSet<>();

    public abstract void trigger(LivingEntity lE, PTriggerType pTT, PTriggerSubType pTST, ItemStack iS, int slot);

    public PAbstractItem(String id, File file, PType type) {
        super(id, file, type);
    }

    public PAbstractItem() {}

    @JsonIgnore
    public ItemStack getItemStack() {
        return getItemStack(1);
    }

    @JsonIgnore
    public ItemStack getItemStack(int amount) {
        PNBT nbt = new PNBT(material);
        nbt.set("type", type.id, PNBTType.String);
        nbt.set("id", id, PNBTType.String);
        ItemStack iS = nbt.toItemStack();
        ItemMeta iM = iS.getItemMeta();
        assert iM != null;
        iM.setDisplayName(PFormat.format(name));
        iM.setCustomModelData(customModelData);
        iM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        iM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        iM.setLore(PFormat.format(lore));
        iS.setItemMeta(iM);
        iS.setAmount(amount);
        return iS;
    }

    @JsonIgnore
    public static PAbstractItem getPItem(ItemStack iS) {
        if (iS == null) return null;
        PNBT nbt = new PNBT(iS);
        if (!nbt.valid) {
            return null;
        }
        PType pT = PType.getPType((String) nbt.get("type", PNBTType.String));
        String id = (String) nbt.get("id", PNBTType.String);
        if (pT == null || !pT.isItem || id == null) {
            return null;
        }
        return (PAbstractItem) pT.getPFile(id);
    }

    @JsonIgnore
    public static boolean checkTriggerCancel(PTriggerType pTT, ItemStack iS) {
        PAbstractItem pAI = getPItem(iS);
        if (pAI == null) return false;
        return pAI.triggerCancelSet.contains(pTT);
    }

    public static void checkTrigger(LivingEntity lE, PTriggerType pTT, PTriggerSubType pTST, ItemStack iS, int slot) {
        PAbstractItem pAI = getPItem(iS);
        if (pAI == null) return;
        pAI.trigger(lE, pTT, pTST, iS, slot);
    }
}
