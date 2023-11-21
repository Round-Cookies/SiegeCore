package me.asakura_kukii.siegecore.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.asakura_kukii.siegecore.SiegeCore;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.item.PAbstractItem;
import me.asakura_kukii.siegecore.trigger.PTriggerSlot;
import me.asakura_kukii.siegecore.util.math.PMath;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

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
    public List<String> equipmentUUIDList = new ArrayList<>(Arrays.asList(null, null, null, null, null, null));

    @JsonIgnore
    public List<ItemStack> equipmentItemStackList = new ArrayList<>(Arrays.asList(null, null, null, null, null, null));

    @JsonIgnore
    public Pair<HashMap<PTriggerSlot, Pair<String, ItemStack>>, HashMap<PTriggerSlot, Pair<String, ItemStack>>> updateEquipmentList() {
        Player p = Bukkit.getPlayer(id);
        List<String> equipmentUUIDList = new ArrayList<>(Arrays.asList(null, null, null, null, null, null));
        List<ItemStack> equipmentItemStackList = new ArrayList<>(Arrays.asList(null, null, null, null, null, null));
        if (p == null) return null;
        HashMap<PTriggerSlot, Pair<String, ItemStack>> stockItemStackMap = new HashMap<>();
        HashMap<PTriggerSlot, Pair<String, ItemStack>> equipItemStackMap = new HashMap<>();
        equipmentUUIDList.set(0, PAbstractItem.getUUID(p.getInventory().getItemInMainHand()));
        equipmentUUIDList.set(1, PAbstractItem.getUUID(p.getInventory().getItemInOffHand()));
        equipmentUUIDList.set(2, PAbstractItem.getUUID(p.getInventory().getHelmet()));
        equipmentUUIDList.set(3, PAbstractItem.getUUID(p.getInventory().getChestplate()));
        equipmentUUIDList.set(4, PAbstractItem.getUUID(p.getInventory().getLeggings()));
        equipmentUUIDList.set(5, PAbstractItem.getUUID(p.getInventory().getBoots()));
        if (p.getInventory().getItemInMainHand() != null) equipmentItemStackList.set(0, p.getInventory().getItemInMainHand());
        if (p.getInventory().getItemInOffHand() != null) equipmentItemStackList.set(1, p.getInventory().getItemInOffHand());
        if (p.getInventory().getHelmet() != null) equipmentItemStackList.set(2, p.getInventory().getHelmet());
        if (p.getInventory().getChestplate() != null) equipmentItemStackList.set(3, p.getInventory().getChestplate());
        if (p.getInventory().getLeggings() != null) equipmentItemStackList.set(4, p.getInventory().getLeggings());
        if (p.getInventory().getBoots() != null) equipmentItemStackList.set(5, p.getInventory().getBoots());
        for (int i = 0; i < 6; i++) {
            if (!(Objects.equals(this.equipmentUUIDList.get(i), equipmentUUIDList.get(i)))) {
                if (this.equipmentUUIDList.get(i) != null) {
                    stockItemStackMap.put(slotList[i], Pair.of(this.equipmentUUIDList.get(i), this.equipmentItemStackList.get(i)));
                }
                if (equipmentUUIDList.get(i) != null) {
                    equipItemStackMap.put(slotList[i], Pair.of(equipmentUUIDList.get(i), equipmentItemStackList.get(i)));
                }
            }
        }
        this.equipmentUUIDList.clear();
        this.equipmentUUIDList.addAll(equipmentUUIDList);
        this.equipmentItemStackList.clear();
        this.equipmentItemStackList.addAll(equipmentItemStackList);
        return Pair.of(stockItemStackMap, equipItemStackMap);
    }

    @Override
    public void finalizeDeserialization() {

    }

    @Override
    public void defaultValue() {

    }
}
