package me.asakura_kukii.siegecore.argument.command;

import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.item.PAbstractItem;
import me.asakura_kukii.siegecore.argument.PArgument;
import me.asakura_kukii.siegecore.argument.PSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PCommandItem {

    public static boolean onItem(PSender sender, PArgument argument) {
        String action = argument.nextString();
        if (!argument.success) {
            sender.error(argument.error);
            return false;
        }
        switch (action) {
            case "list":
                return onItemList(sender, argument);
            case "give":
                return onItemGive(sender, argument);
            default:
                sender.error("Invalid sub-argument");
                return false;
        }
    }

    public static boolean onItemGive(PSender sender, PArgument argument) {
        if (!sender.isPlayer()) {
            sender.error("Invalid sub-argument, player-side only");
            return false;
        }
        String typeId = argument.nextString();
        if (!argument.success) {
            sender.error(argument.error);
            return false;
        }
        PType pT = PType.getPType(typeId);
        if (pT == null || !pT.isItem) {
            sender.error("Invalid type_id");
            return false;
        }
        String fileId = argument.nextString();
        if (!argument.success) {
            sender.error(argument.error);
            return false;
        }
        if (pT.getPFile(fileId) == null) {
            sender.error("Invalid file_id");
            return false;
        }
        PAbstractItem pI = (PAbstractItem) pT.getPFile(fileId);
        ItemStack iS = pI.getItemStack(1);
        if (sender.isPlayer()) {
            Player p = (Player) sender.sender;
            if (p.getInventory().firstEmpty() == -1) {
                p.getWorld().dropItemNaturally(p.getEyeLocation(), iS);
            } else {
                if (p.getInventory().firstEmpty() == p.getInventory().getHeldItemSlot()) {
                    p.getInventory().setItem(p.getInventory().getHeldItemSlot(), iS);
                } else {
                    p.getInventory().setItem(p.getInventory().firstEmpty(), iS);
                }
            }
        }
        return true;
    }

    public static boolean onItemList(PSender sender, PArgument argument) {
        String typeId = argument.nextString();
        if (!argument.success) {
            sender.error(argument.error);
            return false;
        }
        PType pT = PType.getPType(typeId);
        if (pT == null || !pT.isItem) {
            sender.error("Invalid type_id");
            return false;
        }
        listItem(sender, pT);
        return true;
    }

    public static void listItem(PSender sender, PType pT) {
        List<PFile> pFileList = pT.getPFileList();
        if (pFileList.isEmpty()) {
            sender.log("no item");
        } else if (pFileList.size() == 1) {
            sender.log(pFileList.size() + " item listed:");
        } else {
            sender.log(pFileList.size() + " items listed:");
        }
        for (PFile pF : pT.getPFileList()) {
            sender.log("path: " + pF.file.getPath() + " file_id: " + pF.id);
        }
    }
}
