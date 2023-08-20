package me.asakura_kukii.siegecore.util.argument.command;

import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.util.argument.PArgument;
import me.asakura_kukii.siegecore.util.argument.PSender;

import java.util.List;

import static me.asakura_kukii.siegecore.Main.pluginName;

public class CommandFile {

    public static boolean onFile(PSender sender, PArgument argument) {
        if (!sender.hasPerm(pluginName + ".file")) {
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
                return onFileList(sender, argument);
            default:
                sender.error("Invalid sub-argument");
                return false;
        }
    }

    public static boolean onFileList(PSender sender, PArgument argument) {
        String typeId = argument.nextString();
        if (!argument.success) {
            sender.error(argument.error);
            return false;
        }
        PType pT = PType.getPType(typeId);
        if (pT == null) {
            sender.error("Invalid type");
            return false;
        }
        listFile(sender, pT);
        return true;
    }

    public static void listFile(PSender sender, PType pT) {
        List<PFile> pFileList = pT.getPFileList();
        if (pFileList.size() == 0) {
            sender.info("NO FILE");
        } else if (pFileList.size() == 1) {
            sender.info(pFileList.size() + " FILE LISTED:");
        } else {
            sender.info(pFileList.size() + " FILES LISTED:");
        }
        for (PFile pF : pT.getPFileList()) {
            sender.info("PATH: " + pF.file.getPath() + " FILE_ID: " + pF.id);
        }
    }
}
