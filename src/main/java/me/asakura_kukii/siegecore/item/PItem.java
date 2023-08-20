package me.asakura_kukii.siegecore.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.util.nbt.NBTHandler;
import me.asakura_kukii.siegecore.util.nbt.NBTType;
import me.asakura_kukii.siegecore.util.format.FormatHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class PItem extends PFile {
    public String name = "";
    public Material material = Material.COOKIE;
    public int customModelData = 0;
    public List<String> lore = new ArrayList<>();

    public PItem(String id, File file, PType type) {
        super(id, file, type);
    }

    public PItem() {}

    @JsonIgnore
    public ItemStack getItemStack() {
        return getItemStack(1);
    }

    @JsonIgnore
    public ItemStack getItemStack(int amount) {
        NBTHandler nbt = new NBTHandler(material);
        nbt.set("type", type.id, NBTType.String);
        nbt.set("id", id, NBTType.String);
        ItemStack iS = nbt.toItemStack();
        ItemMeta iM = iS.getItemMeta();
        assert iM != null;
        iM.setDisplayName(FormatHandler.format(name));
        iM.setCustomModelData(customModelData);
        iM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        iM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        iM.setLore(FormatHandler.format(lore));
        iS.setItemMeta(iM);
        iS.setAmount(amount);
        return iS;
    }

    @JsonIgnore
    public static PItem getPItem(ItemStack iS) {
        NBTHandler nbt = new NBTHandler(iS);
        if (!nbt.valid) {
            return null;
        }
        PType pT = PType.getPType((String) nbt.get("type", NBTType.String));
        String id = (String) nbt.get("id", NBTType.String);
        if (pT == null || !pT.isItem || id == null) {
            return null;
        }
        return (PItem) pT.getPFile(id);
    }
}
