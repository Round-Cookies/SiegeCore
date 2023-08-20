package me.asakura_kukii.siegecore;

import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.item.PDeco;
import me.asakura_kukii.siegecore.unicode.PUnicode;
import me.asakura_kukii.siegecore.util.format.FormatHandler;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

public class Main {
    public static Server server = null;
    public static JavaPlugin pluginInstance = null;
    public static File pluginFolder = null;
    public static String pluginName = null;
    public static String pluginCode = null;
    public static String pluginPrefix = null;
    public static String consolePluginPrefix = null;
    public static Random random = new Random();
    public static HashMap<JavaPlugin, BukkitTask> updaterRegister = new HashMap<>();

    public static void registerEvent() {
    }

    public static void registerType() {
        PType.putPType(pluginInstance, "deco", PDeco.class);
        PType.putPType(pluginInstance, "unicode", PUnicode.class);
    }

    public static void onEnable(Server s, File pF, String pN, String pC, String pP, String cPP, JavaPlugin p) {

        server = s;
        pluginInstance = p;
        pluginFolder = pF;
        pluginName = pN;
        pluginCode = pC;
        pluginPrefix = pP;
        consolePluginPrefix = cPP;

        registerEvent();
        registerType();

        updater();
    }

    public static void onDisable() {
        PType.clearAll();
    }

    public static void updater() {
        if (updaterRegister.containsKey(pluginInstance)) {
            updaterRegister.get(pluginInstance).cancel();
            updaterRegister.remove(pluginInstance);
        }
        updaterRegister.put(pluginInstance, new BukkitRunnable() {
            @Override
            public void run() {
            }
        }.runTaskTimer(pluginInstance , 0, 1));
    }

    public static void info(String s) {
        String[] msgList = s.split("\n");
        for (String msg : msgList) {
            if (msg.trim().length() >= 200) {
                server.getConsoleSender().sendMessage(FormatHandler.ANSI_WHITE + consolePluginPrefix + msg.trim().substring(0, 199) + "..." + FormatHandler.ANSI_WHITE);
            } else {
                server.getConsoleSender().sendMessage(FormatHandler.ANSI_WHITE + consolePluginPrefix + msg.trim() + FormatHandler.ANSI_WHITE);
            }
        }
    }

    public static void warn(String s) {
        String[] msgList = s.split("\n");
        for (String msg : msgList) {
            if (msg.trim().length() >= 200) {
                server.getConsoleSender().sendMessage(FormatHandler.ANSI_YELLOW + consolePluginPrefix + msg.trim().substring(0, 199) + "..." + FormatHandler.ANSI_WHITE);
            } else {
                server.getConsoleSender().sendMessage(FormatHandler.ANSI_YELLOW + consolePluginPrefix + msg.trim() + FormatHandler.ANSI_WHITE);
            }
        }
    }

    public static void error(String s) {
        String[] msgList = s.split("\n");
        for (String msg : msgList) {
            if (msg.trim().length() >= 200) {
                server.getConsoleSender().sendMessage(FormatHandler.ANSI_RED + consolePluginPrefix + msg.trim().substring(0, 199) + "..." + FormatHandler.ANSI_WHITE);
            } else {
                server.getConsoleSender().sendMessage(FormatHandler.ANSI_RED + consolePluginPrefix + msg.trim() + FormatHandler.ANSI_WHITE);
            }
        }
    }

    public static void raw(String s) {
        server.getConsoleSender().sendMessage(s);
    }
}