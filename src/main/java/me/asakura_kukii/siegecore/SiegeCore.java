package me.asakura_kukii.siegecore;

import me.asakura_kukii.siegecore.effect.PParticle;
import me.asakura_kukii.siegecore.effect.PSound;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.io.PTypeListener;
import me.asakura_kukii.siegecore.item.PItem;
import me.asakura_kukii.siegecore.player.PPlayer;
import me.asakura_kukii.siegecore.trigger.PTrigger;
import me.asakura_kukii.siegecore.trigger.PTriggerListener;
import me.asakura_kukii.siegecore.trigger.PTriggerSubType;
import me.asakura_kukii.siegecore.trigger.PTriggerType;
import me.asakura_kukii.siegecore.unicode.PUnicode;
import me.asakura_kukii.siegecore.argument.PArgument;
import me.asakura_kukii.siegecore.argument.PSender;
import me.asakura_kukii.siegecore.argument.command.PCommand;
import me.asakura_kukii.siegecore.argument.tab.PTab;
import me.asakura_kukii.siegecore.util.format.PFormat;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SiegeCore extends JavaPlugin {
    public static String pluginColorCode = "&c";

    public static Server server = null;
    public static String pluginName;
    public static String pluginPrefix;
    public static String consolePluginPrefix;
    public static JavaPlugin pluginInstance = null;
    public static File pluginFolder = null;
    public static long runTickTime = 0L;
    public static HashMap<JavaPlugin, BukkitTask> updaterRegister = new HashMap<>();

    public static void registerEvent() {
        Bukkit.getPluginManager().registerEvents(new PTriggerListener(), pluginInstance);
        Bukkit.getPluginManager().registerEvents(new PTypeListener(), pluginInstance);
    }

    public static void registerType() {
        PType.putPType(pluginInstance, "particle", PParticle.class);
        PType.putPType(pluginInstance, "sound", PSound.class);
        PType.putPType(pluginInstance, "item", PItem.class);
        PType.putPType(pluginInstance, "unicode", PUnicode.class);
        PType.putPType(pluginInstance, "player", PPlayer.class);
    }

    @Override
    public void onEnable() {
        server = getServer();
        pluginName = getName();
        pluginPrefix = PFormat.format("&8[" + pluginColorCode + pluginName + "&8] &f");
        consolePluginPrefix = "[" + pluginName + "]->>";
        info("Enabling " + pluginName);
        pluginInstance = this;
        pluginFolder = getDataFolder();
        if (!pluginFolder.exists() && pluginFolder.mkdirs()) warn("Creating plugin folder [" + pluginName + "]");

        registerEvent();
        registerType();

        updater();
        info(pluginName + " enabled");

        server.getScheduler().scheduleSyncDelayedTask(this, () -> {
            info("Loading all types...");
            PType.loadAll();
        });
    }

    public void onDisable() {
        info("Disabling " + pluginName);
        info(pluginName + " disabled");
    }

    public static void updater() {
        if (updaterRegister.containsKey(pluginInstance)) {
            updaterRegister.get(pluginInstance).cancel();
            updaterRegister.remove(pluginInstance);
        }
        updaterRegister.put(pluginInstance, new BukkitRunnable() {
            @Override
            public void run() {
                SiegeCore.runTickTime = SiegeCore.runTickTime + 1;
                PTrigger.update();
            }
        }.runTaskTimer(pluginInstance , 0, 1));
    }

    public static void info(String s) {
        String[] msgList = s.split("\n");
        for (String msg : msgList) {
            if (msg.trim().length() >= 200) {
                raw(PFormat.ANSI_GREEN + consolePluginPrefix + msg.trim().substring(0, 199) + "..." + PFormat.ANSI_WHITE);
            } else {
                raw(PFormat.ANSI_GREEN + consolePluginPrefix + msg.trim() + PFormat.ANSI_WHITE);
            }
        }
    }

    public static void log(String s) {
        String[] msgList = s.split("\n");
        for (String msg : msgList) {
            if (msg.trim().length() >= 200) {
                raw(PFormat.ANSI_WHITE + consolePluginPrefix + msg.trim().substring(0, 199) + "..." + PFormat.ANSI_WHITE);
            } else {
                raw(PFormat.ANSI_WHITE + consolePluginPrefix + msg.trim() + PFormat.ANSI_WHITE);
            }
        }
    }

    public static void warn(String s) {
        String[] msgList = s.split("\n");
        for (String msg : msgList) {
            if (msg.trim().length() >= 200) {
                raw(PFormat.ANSI_YELLOW + consolePluginPrefix + msg.trim().substring(0, 199) + "..." + PFormat.ANSI_WHITE);
            } else {
                raw(PFormat.ANSI_YELLOW + consolePluginPrefix + msg.trim() + PFormat.ANSI_WHITE);
            }
        }
    }

    public static void error(String s) {
        String[] msgList = s.split("\n");
        for (String msg : msgList) {
            if (msg.trim().length() >= 200) {
                raw(PFormat.ANSI_RED + consolePluginPrefix + msg.trim().substring(0, 199) + "..." + PFormat.ANSI_WHITE);
            } else {
                raw(PFormat.ANSI_RED + consolePluginPrefix + msg.trim() + PFormat.ANSI_WHITE);
            }
        }
    }

    public static void raw(String s) {
        server.getConsoleSender().sendMessage(s);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> sL = new ArrayList<>();
        if (args.length > 0) {
            PArgument argument = new PArgument(label, args);
            PSender sender = new PSender(pluginName, pluginPrefix, commandSender);
            sL = PTab.onTab(sender, argument);
            return sL;
        }
        return sL;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(pluginName)) {
            PArgument argument = new PArgument(label, args);
            PSender sender = new PSender(pluginName, pluginPrefix, commandSender);
            return PCommand.onCommand(sender, argument);
        }
        return true;
    }
}