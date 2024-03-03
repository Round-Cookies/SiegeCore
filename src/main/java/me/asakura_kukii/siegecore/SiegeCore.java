package me.asakura_kukii.siegecore;

import me.asakura_kukii.siegecore.effect.PParticle;
import me.asakura_kukii.siegecore.effect.PSound;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.io.PTypeListener;
import me.asakura_kukii.siegecore.item.PItem;
import me.asakura_kukii.siegecore.player.PPlayer;
import me.asakura_kukii.siegecore.trigger.PTrigger;
import me.asakura_kukii.siegecore.trigger.PTriggerListener;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SiegeCore extends JavaPlugin {
    public static boolean flagEnabled = false;
    public static UUID sessionUUID = UUID.randomUUID();
    public static String pluginColorCode = "&c";
    public static Server server = null;
    public static String pluginName;
    public static String pluginPrefix;
    public static String consolePluginPrefix;
    public static JavaPlugin pluginInstance = null;
    public static File pluginFolder = null;
    public static long runTickTime = 0L;
    public static boolean debug = false;
    public static HashMap<JavaPlugin, BukkitTask> updaterRegister = new HashMap<>();

    public static void registerEvent() {
        Bukkit.getPluginManager().registerEvents(new PTriggerListener(), pluginInstance);
        Bukkit.getPluginManager().registerEvents(new PTypeListener(), pluginInstance);
    }

    public static void registerType() {
        PType.putPType(pluginInstance, "unicode", PUnicode.class);
        PType.putPType(pluginInstance, "particle", PParticle.class);
        PType.putPType(pluginInstance, "sound", PSound.class);
        PType.putPType(pluginInstance, "item", PItem.class);
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
        flagEnabled = true;
        sessionUUID = UUID.randomUUID();
        server.getScheduler().scheduleSyncDelayedTask(this, () -> {
            info("Loading all types...");
            PType.loadAll();
        });
    }

    public void onDisable() {
        PType.savePlayerData();
        PType.unloadAll();
        flagEnabled = false;
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

    public static void info(String rawText) {
        info(consolePluginPrefix, rawText);
    }

    public static void info(String prefix, String rawText) {
        msg(PFormat.ANSI_GREEN, prefix, rawText);
    }

    public static void log(String rawText) {
        log(consolePluginPrefix, rawText);
    }

    public static void log(String prefix, String rawText) {
        msg(PFormat.ANSI_WHITE, prefix, rawText);
    }

    public static void warn(String rawText) {
        warn(consolePluginPrefix, rawText);
    }

    public static void warn(String prefix, String rawText) {
        msg(PFormat.ANSI_YELLOW, prefix, rawText);
    }

    public static void error(String rawText) {
        error(consolePluginPrefix, rawText);
    }

    public static void error(String prefix, String rawText) {
        msg(PFormat.ANSI_RED, prefix, rawText);
    }

    public static void msg(String color, String prefix, String rawText) {
        String[] textList = rawText.split("\n");
        for (String text : textList) {
            if (text.trim().length() >= 200) {
                raw(color + prefix + text.trim().substring(0, 199) + "..." + PFormat.ANSI_WHITE);
            } else {
                raw(color + prefix + text.trim() + PFormat.ANSI_WHITE);
            }
        }
    }

    public static void raw(String s) {
        server.getConsoleSender().sendMessage(s);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
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
    public boolean onCommand(@NotNull CommandSender commandSender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase(pluginName)) {
            PArgument argument = new PArgument(label, args);
            PSender sender = new PSender(pluginName, pluginPrefix, commandSender);
            return PCommand.onCommand(sender, argument);
        }
        return true;
    }
}