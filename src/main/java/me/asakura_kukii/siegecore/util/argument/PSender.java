package me.asakura_kukii.siegecore.util.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.asakura_kukii.siegecore.Main.pluginPrefix;

public class PSender {

    public CommandSender sender = null;

    public PSender(CommandSender s) {
        this.sender = s;
    }

    public void nextLine() {
        sender.sendMessage("");
    }

    public void info(String s) {
        sender.sendMessage(pluginPrefix + ChatColor.WHITE + s);
    }

    public void warn(String s) {
        sender.sendMessage(pluginPrefix + ChatColor.YELLOW + s);
    }

    public void error(String s) {
        sender.sendMessage(pluginPrefix + ChatColor.RED + s);
    }

    public void raw(String s) {
        sender.sendMessage(s);
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public boolean hasPerm(String s) {
        if (!isPlayer()) return true;
        return sender.hasPermission(s);
    }
}
