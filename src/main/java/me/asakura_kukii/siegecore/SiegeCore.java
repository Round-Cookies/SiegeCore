package me.asakura_kukii.siegecore;

import me.asakura_kukii.siegecore.util.format.FormatHandler;
import me.asakura_kukii.siegecore.util.argument.command.CommandHandler;
import me.asakura_kukii.siegecore.util.argument.tab.TabHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SiegeCore extends JavaPlugin {

    public static File dataFolder;
    private static SiegeCore siegeCore;
    public static SiegeCore getInstance() {
        return siegeCore;
    }
    public static String pluginPrefix = ChatColor.translateAlternateColorCodes('&',"&8[&eSiegeCore&8] &f");
    public static String consolePluginPrefix = "[SiegeCore]->>";
    public static String pluginName = "SiegeCore";
    public static String pluginCode = "sc";

    @Override
    public void onEnable() {
        siegeCore = this;
        getServer().getConsoleSender().sendMessage(FormatHandler.ANSI_GREEN + consolePluginPrefix + "Enabling " + pluginName + FormatHandler.ANSI_WHITE);
        dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
            getServer().getConsoleSender().sendMessage(FormatHandler.ANSI_GREEN + pluginPrefix + "Creating data folder" + FormatHandler.ANSI_WHITE);
        }
        Main.onEnable(getServer(), dataFolder, pluginName, pluginCode, pluginPrefix, consolePluginPrefix, this);
        getServer().getConsoleSender().sendMessage(FormatHandler.ANSI_GREEN + consolePluginPrefix + pluginName + " enabled" + FormatHandler.ANSI_WHITE);
    }

    @Override
    public void onDisable() {
        Main.onDisable();
        getServer().getConsoleSender().sendMessage(FormatHandler.ANSI_GREEN + consolePluginPrefix + pluginName + " disabled" + FormatHandler.ANSI_WHITE);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> s = new ArrayList<String>();
        if (args.length > 0) {
            s = TabHandler.onTab(sender, command, alias, args);
            return s;
        }

        return s;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(pluginName)) {
            return CommandHandler.onCommand(sender, command, label, args);
        }
        return true;
    }
}

