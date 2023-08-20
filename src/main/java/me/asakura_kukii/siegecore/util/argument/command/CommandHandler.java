package me.asakura_kukii.siegecore.util.argument.command;

import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.util.argument.PArgument;
import me.asakura_kukii.siegecore.util.argument.PSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import static me.asakura_kukii.siegecore.Main.pluginName;


public class CommandHandler {

    public static boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        PArgument argument = new PArgument(args);
        PSender sender = new PSender(commandSender);
        sender.nextLine();
        sender.info("Issued:");
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
                return CommandItem.onItem(sender, argument);
            case "read":
                if (!sender.hasPerm(pluginName + ".io")) {
                    sender.error("Missing permission");
                    return false;
                }
                PType.readAll();
                return true;
            case "write":
                if (!sender.hasPerm(pluginName + ".io")) {
                    sender.error("Missing permission");
                    return false;
                }
                PType.writeAll();
                return true;
            case "reload":
                if (!sender.hasPerm(pluginName + ".io")) {
                    sender.error("Missing permission");
                    return false;
                }
                PType.writeAll();
                PType.readAll();
                return true;
            default:
                sender.error("Invalid sub-argument");
                return false;
        }
    }

    public static boolean onInfo(PSender sender, PArgument argument) {
        return true;
    }
}
