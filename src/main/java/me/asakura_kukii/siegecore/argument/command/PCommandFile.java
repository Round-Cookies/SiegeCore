package me.asakura_kukii.siegecore.argument.command;

import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.argument.PArgument;
import me.asakura_kukii.siegecore.argument.PSender;

import java.util.List;

public class PCommandFile {

    public static boolean onFile(PSender sender, PArgument argument) {
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
            sender.error("Invalid type_id");
            return false;
        }
        listFile(sender, pT);
        return true;
    }

    public static void listFile(PSender sender, PType pT) {
        List<PFile> pFileList = pT.getPFileList();
        if (pFileList.size() == 0) {
            sender.log("No file");
        } else if (pFileList.size() == 1) {
            sender.log(pFileList.size() + " file listed:");
        } else {
            sender.log(pFileList.size() + " files listed:");
        }
        for (PFile pF : pT.getPFileList()) {
            sender.log("path: " + pF.file.getPath() + " file_id: " + pF.id);
        }
    }
}
