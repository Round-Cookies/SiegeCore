package me.asakura_kukii.siegecore.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PSender {

    public CommandSender sender;
    public String pluginName;
    public String pluginPrefix;

    public PSender(String pluginName, String pluginPrefix, CommandSender s) {
        this.pluginName = pluginName;
        this.pluginPrefix = pluginPrefix;
        this.sender = s;
    }

    public void nextLine() {
        sender.sendMessage("");
    }

    public void info(String s) {
        sender.sendMessage(pluginPrefix + ChatColor.GREEN + s);
    }

    public void log(String s) {
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

    public Player getPlayer() {
        if (isPlayer()) return (Player) sender;
        return null;
    }

    public boolean hasPerm(String s) {
        if (!isPlayer()) return true;
        return sender.hasPermission(pluginName + "." + s);
    }
}
