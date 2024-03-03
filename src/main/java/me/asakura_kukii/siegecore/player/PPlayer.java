package me.asakura_kukii.siegecore.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.asakura_kukii.siegecore.SiegeCore;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.item.PAbstractItem;
import me.asakura_kukii.siegecore.trigger.PTriggerSlot;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class PPlayer extends PAbstractPlayer{

    public PPlayer(String id, File file, PType type) {
        super(id, file, type);
    }

    public PPlayer() {}

    @JsonIgnore
    public static final PTriggerSlot[] slotList = {PTriggerSlot.MAIN, PTriggerSlot.OFF, PTriggerSlot.HEAD, PTriggerSlot.CHEST, PTriggerSlot.LEGS, PTriggerSlot.FEET};

    @JsonIgnore
    public HashMap<PTriggerSlot, String> equipmentUUIDMap = new HashMap<>();

    @JsonIgnore
    public HashMap<PTriggerSlot, ItemStack> equipmentItemStackMap = new HashMap<>();

    @JsonIgnore
    public HashMap<PTriggerSlot, Long> equipmentTickTimeList = new HashMap<>();

    @JsonIgnore
    public long getEquipmentTickTime(PTriggerSlot pTS) {
        if (!equipmentTickTimeList.containsKey(pTS)) return SiegeCore.runTickTime;
        if (equipmentTickTimeList.get(pTS) == null) return SiegeCore.runTickTime;
        // modifications due to bukkit task order
        return equipmentTickTimeList.get(pTS) - 2;
    }

    @JsonIgnore
    public Pair<HashMap<PTriggerSlot, Pair<String, ItemStack>>, HashMap<PTriggerSlot, Pair<String, ItemStack>>> updateEquipmentList() {
        Player p = getPlayer();
        if (p == null) return null;
        HashMap<PTriggerSlot, ItemStack> equipmentItemStackMap = new HashMap<>();
        HashMap<PTriggerSlot, String> equipmentUUIDMap = new HashMap<>();
        HashMap<PTriggerSlot, Pair<String, ItemStack>> stockItemStackMap = new HashMap<>();
        HashMap<PTriggerSlot, Pair<String, ItemStack>> equipItemStackMap = new HashMap<>();
        equipmentUUIDMap.put(PTriggerSlot.MAIN, PAbstractItem.getUUID(p.getInventory().getItemInMainHand()));
        equipmentUUIDMap.put(PTriggerSlot.OFF, PAbstractItem.getUUID(p.getInventory().getItemInOffHand()));
        equipmentUUIDMap.put(PTriggerSlot.HEAD, PAbstractItem.getUUID(p.getInventory().getHelmet()));
        equipmentUUIDMap.put(PTriggerSlot.CHEST, PAbstractItem.getUUID(p.getInventory().getChestplate()));
        equipmentUUIDMap.put(PTriggerSlot.LEGS, PAbstractItem.getUUID(p.getInventory().getLeggings()));
        equipmentUUIDMap.put(PTriggerSlot.FEET, PAbstractItem.getUUID(p.getInventory().getBoots()));
        if (p.getInventory().getItemInMainHand() != null) equipmentItemStackMap.put(PTriggerSlot.MAIN, p.getInventory().getItemInMainHand());
        if (p.getInventory().getItemInOffHand() != null) equipmentItemStackMap.put(PTriggerSlot.OFF, p.getInventory().getItemInOffHand());
        if (p.getInventory().getHelmet() != null) equipmentItemStackMap.put(PTriggerSlot.HEAD, p.getInventory().getHelmet());
        if (p.getInventory().getChestplate() != null) equipmentItemStackMap.put(PTriggerSlot.CHEST, p.getInventory().getChestplate());
        if (p.getInventory().getLeggings() != null) equipmentItemStackMap.put(PTriggerSlot.LEGS, p.getInventory().getLeggings());
        if (p.getInventory().getBoots() != null) equipmentItemStackMap.put(PTriggerSlot.FEET, p.getInventory().getBoots());
        for (PTriggerSlot pTS : slotList) {
            if (!(Objects.equals(this.equipmentUUIDMap.get(pTS), equipmentUUIDMap.get(pTS)))) {
                if (this.equipmentUUIDMap.get(pTS) != null) {
                    stockItemStackMap.put(pTS, Pair.of(this.equipmentUUIDMap.get(pTS), this.equipmentItemStackMap.get(pTS)));
                    this.equipmentTickTimeList.put(pTS, null);
                }
                if (equipmentUUIDMap.get(pTS) != null) {
                    equipItemStackMap.put(pTS, Pair.of(equipmentUUIDMap.get(pTS), equipmentItemStackMap.get(pTS)));
                    this.equipmentTickTimeList.put(pTS, SiegeCore.runTickTime);
                }
            }
        }
        this.equipmentUUIDMap.clear();
        this.equipmentUUIDMap.putAll(equipmentUUIDMap);
        this.equipmentItemStackMap.clear();
        this.equipmentItemStackMap.putAll(equipmentItemStackMap);
        return Pair.of(stockItemStackMap, equipItemStackMap);
    }

    @Override
    public void finalizeDeserialization() {

    }

    @Override
    public void defaultValue() {

    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }
}
