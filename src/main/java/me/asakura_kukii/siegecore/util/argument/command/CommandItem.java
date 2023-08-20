package me.asakura_kukii.siegecore.util.argument.command;

import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.item.PItem;
import me.asakura_kukii.siegecore.util.argument.PArgument;
import me.asakura_kukii.siegecore.util.argument.PSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.asakura_kukii.siegecore.Main.*;

public class CommandItem {

    public static boolean onItem(PSender sender, PArgument argument) {
        if (!sender.hasPerm(pluginName + ".item")) {
            sender.error("Missing permission");
            return false;
        }
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
            sender.error("Invalid type id");
            return false;
        }
        String fileId = argument.nextString();
        if (!argument.success) {
            sender.error(argument.error);
            return false;
        }
        if (pT.getPFile(fileId) == null) {
            sender.error("Invalid file id");
            return false;
        }
        PItem pI = (PItem) pT.getPFile(fileId);
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
            sender.error("Invalid type");
            return false;
        }
        listItem(sender, pT);
        return true;
    }

    public static void listItem(PSender sender, PType pT) {
        List<PFile> pFileList = pT.getPFileList();
        if (pFileList.size() == 0) {
            sender.info("NO ITEM");
        } else if (pFileList.size() == 1) {
            sender.info(pFileList.size() + " ITEM LISTED:");
        } else {
            sender.info(pFileList.size() + " ITEMS LISTED:");
        }
        for (PFile pF : pT.getPFileList()) {
            sender.info("PATH: " + pF.file.getPath() + " FILE_ID: " + pF.id);
        }
    }
}
