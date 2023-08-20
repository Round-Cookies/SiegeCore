package me.asakura_kukii.siegecore.util.argument.tab;

import me.asakura_kukii.siegecore.util.argument.PArgument;
import me.asakura_kukii.siegecore.util.argument.PSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TabHandler {
    public static List<String> noTabNext(List<String> sL, PArgument argument) {
        if (argument.nextString() != null) {
            sL.clear();
        }
        return sL;
    }

    public static List<String> onTab(CommandSender commandSender, Command command, String alias, String[] args) {
        PArgument argument = new PArgument(args);
        PSender sender = new PSender(commandSender);
        List<String> sL = new ArrayList<>();

        String s = argument.nextString();
        if (!argument.success) {
            return new ArrayList<>();
        }
        switch (s) {
            case "item":
                return TabItem.tabItem(sender, argument);
            default:
                if (PArgument.completeString("item", s)) sL.add("item");
                if (PArgument.completeString("read", s)) sL.add("read");
                if (PArgument.completeString("write", s)) sL.add("write");
                if (PArgument.completeString("reload", s)) sL.add("reload");
                return noTabNext(sL, argument);
        }
    }
}
