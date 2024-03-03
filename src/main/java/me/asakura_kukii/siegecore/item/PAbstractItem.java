package me.asakura_kukii.siegecore.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.asakura_kukii.siegecore.SiegeCore;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.trigger.PTrigger;
import me.asakura_kukii.siegecore.trigger.PTriggerSlot;
import me.asakura_kukii.siegecore.trigger.PTriggerSubType;
import me.asakura_kukii.siegecore.trigger.PTriggerType;
import me.asakura_kukii.siegecore.util.nbt.PNBT;
import me.asakura_kukii.siegecore.util.nbt.PNBTType;
import me.asakura_kukii.siegecore.util.format.PFormat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.*;

public abstract class PAbstractItem extends PFile {
    public String name = "";
    public Material material = Material.AMETHYST_SHARD;
    public int customModelData = 100;
    public List<String> lore = new ArrayList<>();
    public Set<PTriggerType> triggerCancelSet = new HashSet<>();
    public Set<PTriggerSlot> triggerSlotSet = new HashSet<>();
    public float price = 0.0F;
    public boolean lock = false;
    public boolean unique = false;

    @JsonIgnore
    public static NamespacedKey typeKey = new NamespacedKey(SiegeCore.pluginInstance, "type");

    @JsonIgnore
    public static NamespacedKey idKey = new NamespacedKey(SiegeCore.pluginInstance, "id");

    @JsonIgnore
    public static NamespacedKey uuidKey = new NamespacedKey(SiegeCore.pluginInstance, "uuid");

    @JsonIgnore
    public static NamespacedKey lockKey = new NamespacedKey(SiegeCore.pluginInstance, "lock");

    @JsonIgnore
    public static NamespacedKey nullKey = new NamespacedKey(SiegeCore.pluginInstance, "null");

    public abstract void trigger(Player p, PTriggerType pTT, PTriggerSubType pTST, PTriggerSlot pTS, ItemStack iS, long hTT);

    public abstract ItemStack finalizeGetItemStack(ItemStack iS);

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
        ItemStack iS = new ItemStack(material);
        ItemMeta iM = Bukkit.getServer().getItemFactory().getItemMeta(material);
        if (iM == null) return null;
        PersistentDataContainer pDC = iM.getPersistentDataContainer();
        pDC.set(typeKey, PersistentDataType.STRING, type.id);
        pDC.set(idKey, PersistentDataType.STRING, id);
        if (unique) pDC.set(uuidKey, PersistentDataType.STRING, UUID.randomUUID().toString());
        pDC.set(lockKey, PersistentDataType.BOOLEAN, lock);
        pDC.set(nullKey, PersistentDataType.BOOLEAN, true);
        iM.setDisplayName(PFormat.format(name));
        iM.setCustomModelData(customModelData);
        iM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        iM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        iM.setLore(PFormat.format(lore));
        iS.setItemMeta(iM);
        iS.setAmount(amount);
        return finalizeGetItemStack(iS);
    }

    @JsonIgnore
    public static PAbstractItem getPItem(ItemStack iS) {
        if (iS == null) return null;
        if (iS.getItemMeta() == null) return null;
        ItemMeta iM = iS.getItemMeta();
        PersistentDataContainer pDC = iM.getPersistentDataContainer();
        PType pT = PType.getPType(pDC.get(typeKey, PersistentDataType.STRING));
        String id = pDC.get(idKey, PersistentDataType.STRING);
        if (pT == null || !pT.isItem || id == null) {
            return null;
        }
        if (!(pT.getPFile(id) instanceof PAbstractItem)) return null;
        return (PAbstractItem) pT.getPFile(id);
    }

    @JsonIgnore
    public static String getUUID(ItemStack iS) {
        if (iS == null) return null;
        if (iS.getItemMeta() == null) return null;
        ItemMeta iM = iS.getItemMeta();
        PersistentDataContainer pDC = iM.getPersistentDataContainer();
        return pDC.get(uuidKey, PersistentDataType.STRING);
    }

    @JsonIgnore
    public static Boolean getLock(ItemStack iS) {
        if (iS == null) return false;
        if (iS.getItemMeta() == null) return false;
        ItemMeta iM = iS.getItemMeta();
        PersistentDataContainer pDC = iM.getPersistentDataContainer();
        Boolean flagLock = pDC.get(lockKey, PersistentDataType.BOOLEAN);
        if (flagLock == null) return false;
        return flagLock;
    }

    @JsonIgnore
    public static void triggerRiseAnimation(ItemStack iS) {
        if (iS == null) return;
        if (iS.getItemMeta() == null) return;
        ItemMeta iM = iS.getItemMeta();
        PersistentDataContainer pDC = iM.getPersistentDataContainer();
        Boolean flagNull = pDC.get(nullKey, PersistentDataType.BOOLEAN);
        if (flagNull == null || flagNull) {
            pDC.set(nullKey, PersistentDataType.BOOLEAN, false);
        } else {
            pDC.set(nullKey, PersistentDataType.BOOLEAN, true);
        }
        iS.setItemMeta(iM);
    }

    @JsonIgnore
    public static boolean checkTriggerCancel(PTriggerType pTT, PTriggerSlot pTS, ItemStack iS) {
        PAbstractItem pAI = getPItem(iS);
        if (pAI == null) return false;
        return pAI.triggerCancelSet.contains(pTT) && pAI.triggerSlotSet.contains(pTS);
    }

    public static void checkTrigger(Player p, PTriggerType pTT, PTriggerSubType pTST, PTriggerSlot pTS, ItemStack iS, long hTT) {
        PAbstractItem pAI = getPItem(iS);
        if (pAI == null) return;
        if (!pAI.triggerSlotSet.contains(pTS)) return;
        if (SiegeCore.debug) p.sendMessage(pAI.id + " " + pTT.name() + " " + pTST.name() + " " + pTS.name() + " " + hTT + " " + SiegeCore.runTickTime);
        pAI.trigger(p, pTT, pTST, pTS, iS, hTT);
    }
}
