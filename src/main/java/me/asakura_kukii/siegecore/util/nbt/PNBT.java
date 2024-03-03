package me.asakura_kukii.siegecore.util.nbt;

import me.asakura_kukii.siegecore.SiegeCore;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;

public class PNBT {
    public ItemStack iS;
    public boolean valid = false;
    public CompoundTag root = new CompoundTag();

    public PNBT(ItemStack iS) {
        this.iS = iS;
        getRoot();
    }

    public PNBT(Material m) {
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

    public Object get(String s, PNBTType t) {
        if (!valid) return null;
        if (s.matches("") && t.equals(PNBTType.Compound)) return root.toString();
        if (!root.contains(s)) return null;
        if (t.equals(PNBTType.Int)) return root.getInt(s);
        if (t.equals(PNBTType.Boolean)) return root.getBoolean(s);
        if (t.equals(PNBTType.Double)) return root.getDouble(s);
        if (t.equals(PNBTType.Float)) return root.getFloat(s);
        if (t.equals(PNBTType.String)) return root.getString(s);
        if (t.equals(PNBTType.Byte)) return root.getByte(s);
        if (t.equals(PNBTType.Long)) return root.getLong(s);
        if (t.equals(PNBTType.Compound)) return root.getCompound(s).toString();
        if (t.equals(PNBTType.IntArray)) return root.getIntArray(s);
        if (t.equals(PNBTType.ByteArray)) return root.getByteArray(s);
        if (t.equals(PNBTType.LongArray)) return root.getLongArray(s);
        return root.get(s);
    }

    public void set(String s, Object o, PNBTType t) {
        if (!valid) return;
        if (root.contains(s)) root.remove(s);
        if (t.equals(PNBTType.Int)) root.putInt(s, (int) o);
        if (t.equals(PNBTType.Boolean)) root.putBoolean(s, (Boolean) o);
        if (t.equals(PNBTType.Double)) root.putDouble(s, (Double) o);
        if (t.equals(PNBTType.Float)) root.putFloat(s, (Float) o);
        if (t.equals(PNBTType.String)) root.putString(s, (String) o);
        if (t.equals(PNBTType.Byte)) root.putByte(s, (Byte) o);
        if (t.equals(PNBTType.Long)) root.putLong(s, (Long) o);
        if (t.equals(PNBTType.Compound)) {
            try {
                if (s.matches("")) {
                    root = TagParser.parseTag((String) o);
                } else {
                    root.put(s, TagParser.parseTag((String) o));
                }
            } catch (Exception ignored) {
            }
        }
        if (t.equals(PNBTType.IntArray)) root.putIntArray(s, (List<Integer>) o);
        if (t.equals(PNBTType.ByteArray)) root.putByteArray(s, (List<Byte>) o);
        if (t.equals(PNBTType.LongArray)) root.putLongArray(s, (List<Long>) o);
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
