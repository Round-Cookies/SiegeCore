package me.asakura_kukii.siegecore.util.nbt;

import me.asakura_kukii.siegecore.SiegeCore;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;

public class NBTHandler {
    public ItemStack iS;
    public boolean valid = false;
    public CompoundTag root = new CompoundTag();

    public NBTHandler(ItemStack iS) {
        this.iS = iS;
        getRoot();
    }

    public NBTHandler(Material m) {
        this.iS = new ItemStack(m);
        ItemMeta iM = iS.getItemMeta();
        if (iM == null) {
            iM = Bukkit.getServer().getItemFactory().getItemMeta(m);
        }
        iS.setItemMeta(iM);
        valid = true;
    }

    public void getRoot() {
        if (iS == null || iS.getItemMeta() == null) {
            return;
        }
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(iS.clone());
        if (!nmsItemStack.hasTag()) {
            return;
        }
        CompoundTag root = nmsItemStack.getTag();
        assert root != null;
        if (root.contains(SiegeCore.pluginName)) {
            this.root = (CompoundTag) root.get(SiegeCore.pluginName);
            valid = true;
        }
    }

    public Object get(String s, NBTType t) {
        if (!valid) return null;
        if (s.matches("") && t.equals(NBTType.Compound)) return root.toString();
        if (!root.contains(s)) return null;
        if (t.equals(NBTType.Int)) return root.getInt(s);
        if (t.equals(NBTType.Boolean)) return root.getBoolean(s);
        if (t.equals(NBTType.Double)) return root.getDouble(s);
        if (t.equals(NBTType.Float)) return root.getFloat(s);
        if (t.equals(NBTType.String)) return root.getString(s);
        if (t.equals(NBTType.Byte)) return root.getByte(s);
        if (t.equals(NBTType.Long)) return root.getLong(s);
        if (t.equals(NBTType.Compound)) return root.getCompound(s).toString();
        if (t.equals(NBTType.IntArray)) return root.getIntArray(s);
        if (t.equals(NBTType.ByteArray)) return root.getByteArray(s);
        if (t.equals(NBTType.LongArray)) return root.getLongArray(s);
        return root.get(s);
    }

    public void set(String s, Object o, NBTType t) {
        if (!valid) return;
        if (root.contains(s)) root.remove(s);
        if (t.equals(NBTType.Int)) root.putInt(s, (int) o);
        if (t.equals(NBTType.Boolean)) root.putBoolean(s, (Boolean) o);
        if (t.equals(NBTType.Double)) root.putDouble(s, (Double) o);
        if (t.equals(NBTType.Float)) root.putFloat(s, (Float) o);
        if (t.equals(NBTType.String)) root.putString(s, (String) o);
        if (t.equals(NBTType.Byte)) root.putByte(s, (Byte) o);
        if (t.equals(NBTType.Long)) root.putLong(s, (Long) o);
        if (t.equals(NBTType.Compound)) {
            try {
                if (s.matches("")) {
                    root = TagParser.parseTag((String) o);
                } else {
                    root.put(s, TagParser.parseTag((String) o));
                }
            } catch (Exception ignored) {
            }
        }
        if (t.equals(NBTType.IntArray)) root.putIntArray(s, (List<Integer>) o);
        if (t.equals(NBTType.ByteArray)) root.putByteArray(s, (List<Byte>) o);
        if (t.equals(NBTType.LongArray)) root.putLongArray(s, (List<Long>) o);
    }

    public boolean has(String s) {
        return root.contains(s);
    }

    public void remove(String s) {
        root.remove(s);
    }

    public ItemStack toItemStack() {
        if (iS == null) return null;
        if (!valid) return iS;
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(iS.clone());
        CompoundTag NBTCompound = (nmsItemStack.hasTag()) ? nmsItemStack.getTag() : new CompoundTag();
        assert NBTCompound != null;
        NBTCompound.remove(SiegeCore.pluginName);
        NBTCompound.put(SiegeCore.pluginName, root);
        nmsItemStack.setTag(NBTCompound);
        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }

    public static String itemToJson(ItemStack itemStack) throws RuntimeException {
        net.minecraft.world.item.ItemStack nmsItemStack;
        CompoundTag root = new CompoundTag();
        try {
            nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
            root = nmsItemStack.save(new CompoundTag());
        } catch (Exception ignored) {
        }
        return root.toString();
    }
}
