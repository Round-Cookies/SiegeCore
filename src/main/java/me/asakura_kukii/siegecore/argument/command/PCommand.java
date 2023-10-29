package me.asakura_kukii.siegecore.argument.command;

import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.argument.PArgument;
import me.asakura_kukii.siegecore.argument.PSender;

public class PCommand {

    public static boolean onCommand(PSender sender, PArgument argument) {
        sender.nextLine();
        sender.log("Issued:");
        sender.raw(">> " + argument.colorize());

        String s = argument.nextString();
        if (!argument.success) {
            sender.error("Missing sub-argument");
            return false;
        }

        switch (s) {
            case "info":
                return onInfo(sender, argument);
            case "item":
                if (!sender.hasPerm("item")) {
                    sender.error("Missing permission");
                    return false;
                }
                return PCommandItem.onItem(sender, argument);
            case "file":
                if (!sender.hasPerm("file")) {
                    sender.error("Missing permission");
                    return false;
                }
                return PCommandFile.onFile(sender, argument);
            case "load":
                if (!sender.hasPerm("io")) {
                    sender.error("Missing permission");
                    return false;
                }
                PType.loadAll();
                return true;
            case "save":
                if (!sender.hasPerm("io")) {
                    sender.error("Missing permission");
                    return false;
                }
                PType.saveAll();
                return true;
            case "reload":
                if (!sender.hasPerm("io")) {
                    sender.error("Missing permission");
                    return false;
                }
                PType.saveAll();
                PType.loadAll();
                return true;
            default:
                sender.error("Invalid sub-argument");
                return false;
        }
    }

    public static boolean onInfo(PSender sender, PArgument argument) {
        sender.info("Standby!");
        return true;
    }
}
