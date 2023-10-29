package me.asakura_kukii.siegecore.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.asakura_kukii.siegecore.SiegeCore;
import me.asakura_kukii.siegecore.item.PAbstractItem;
import me.asakura_kukii.siegecore.player.PAbstractPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

public class PType {
    private static final HashMap<String, PType> pTypeIdMap = new HashMap<>();
    private static final HashMap<Class<?>, PType> pTypeClazzMap = new HashMap<>();
    private static final List<PType> pTypeList = new ArrayList<>();
    private static final List<PType> itemPTypeList = new ArrayList<>();
    private static final List<PType> playerPTypeList = new ArrayList<>();

    private final HashMap<String, PFile> pFileIdMap = new HashMap<>();

    public String id;
    public File folder;
    public Class<?> clazz;
    public boolean isItem;
    public boolean isPlayer;

    public PType(JavaPlugin plugin, String name, Class<?> clazz) {
        this.id = plugin.getName() + "." + name;
        File pluginFolder = plugin.getDataFolder();
        if (!pluginFolder.exists() && pluginFolder.mkdirs()) SiegeCore.log("Creating plugin folder [" + plugin.getName() + "]");
        this.folder = new File(pluginFolder, name);
        if (!this.folder.exists() && this.folder.mkdirs()) SiegeCore.log("Creating type folder [" + name + "]");
        this.clazz = clazz;
        this.isItem = false;
        this.isPlayer = false;
        Class<?> c = clazz.getSuperclass();
        while(true) {
            if (c.equals(java.lang.Object.class)) break;
            if (c.equals(PAbstractItem.class)) {
                isItem = true;
                break;
            }
            if (c.equals(PAbstractPlayer.class)) {
                isPlayer = true;
                break;
            }
            c = c.getSuperclass();
        }
    }

    public void putPFile(String id, PFile pFile) {
        pFileIdMap.put(id, pFile);
    }

    public PFile getPFile(String id) {
        if (pFileIdMap.containsKey(id)) return pFileIdMap.get(id);
        return null;
    }

    public PFile createPFile(String id) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            PFile pF = (PFile) constructor.newInstance();
            pF.id = id;
            pF.file = new File(this.folder, id + ".json");
            pF.type = this;
            pF.finalizeDeserialization();
            this.pFileIdMap.put(pF.id, pF);
            return pF;
        } catch (Exception e) {
            SiegeCore.error("Failed when creating [" + this.id + "." + id + "] [" + e.getClass().getName() + "]");
            SiegeCore.error(e.getLocalizedMessage());
        }
        return null;
    }

    public void load() {
        pFileIdMap.clear();
        loadRecursively(this.folder);
        SiegeCore.log("Loaded " + pFileIdMap.size() + " files of type [" + id + "]");
    }

    public void loadRecursively(File f) {
        if (f.isDirectory()) {
            for (File f2 : Objects.requireNonNull(f.listFiles())) loadRecursively(f2);
        } else {
            if (!f.getName().contains(".json")) return;
            ObjectMapper objectMapper = new ObjectMapper();
            PFile pF;
            try {
                pF = (PFile) objectMapper.readValue(f, this.clazz);
                pF.file = f;
                pF.type = this;
                pF.finalizeDeserialization();
                this.pFileIdMap.put(pF.id, pF);
            } catch (IOException e) {
                SiegeCore.error("Failed when loading [" + f.getName() + "] [" + e.getClass().getName() + "]");
                SiegeCore.error(e.getLocalizedMessage());
            }
        }
    }

    public void save() {
        int successCount = 0;
        for (PFile pF : this.pFileIdMap.values()) {
            try {
                FileWriter fileWriter = new FileWriter(pF.file);
                fileWriter.write("");
                fileWriter.write(pF.serialize());
                fileWriter.flush();
                fileWriter.close();
                successCount = successCount + 1;
            } catch (IOException e) {
                SiegeCore.error("Failed when writing [" + pF.file.getName() + "]");
                SiegeCore.error(e.getLocalizedMessage());
            }
        }
        SiegeCore.log("Saved " + successCount + " files of type [" + this.id + "]");
    }

    public List<PFile> getPFileList() {
        Collection<PFile> pFileCollection = this.pFileIdMap.values();
        PFile[] pFileList = pFileCollection.toArray(new PFile[0]);
        Arrays.sort(pFileList, Comparator.comparing(o -> o.id));
        return Arrays.asList(pFileList);
    }

    public static void putPType(JavaPlugin plugin, String name, Class<?> clazz) {
        if (getPType(plugin, name) != null) return;
        PType pT = new PType(plugin, name, clazz);
        pTypeIdMap.put(pT.id, pT);
        pTypeClazzMap.put(clazz, pT);
        pTypeList.add(pT);
        if (pT.isItem) itemPTypeList.add(pT);
        if (pT.isPlayer) playerPTypeList.add(pT);
        SiegeCore.log("Registered type [" + pT.id + "]");
    }

    public static PType getPType(JavaPlugin plugin, String name) {
        String id = plugin.getName() + "." + name;
        if (pTypeIdMap.containsKey(id)) return pTypeIdMap.get(id);
        return null;
    }

    public static PType getPType(String id) {
        if (pTypeIdMap.containsKey(id)) return pTypeIdMap.get(id);
        return null;
    }

    public static PType getPType(Class<?> clazz) {
        if (pTypeClazzMap.containsKey(clazz)) return pTypeClazzMap.get(clazz);
        return null;
    }

    public static void loadAll() {
        for (PType pT : pTypeList) {
            pT.load();
        }
    }

    public static void saveAll() {
        for (PType pT : pTypeList) {
            pT.save();
        }
    }

    public static List<PType> getPTypeList() {
        return pTypeList;
    }

    public static List<PType> getItemPTypeList() {
        return itemPTypeList;
    }

    public static List<PType> getPlayerPTypeList() {
        return playerPTypeList;
    }
}
