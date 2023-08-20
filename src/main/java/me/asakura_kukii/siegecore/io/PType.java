package me.asakura_kukii.siegecore.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.asakura_kukii.siegecore.Main;
import me.asakura_kukii.siegecore.item.PItem;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PType {
    private static final HashMap<String, PType> pTypeIdMap = new HashMap<>();
    private static final HashMap<Class<?>, PType> pTypeClazzMap = new HashMap<>();
    private final HashMap<String, PFile> pFileIdMap = new HashMap<>();
    public String id;
    public File folder;
    public Class<?> clazz;
    public boolean isItem;

    public PType(JavaPlugin plugin, String id, Class<?> clazz) {
        File pluginFolder = plugin.getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }
        this.folder = new File(pluginFolder, id);
        if (!this.folder.exists()) {
            this.folder.mkdir();
        }
        this.id = plugin.getName() + "." + id;
        this.clazz = clazz;
        this.isItem = false;
        Class<?> c = clazz.getSuperclass();
        while(true) {
            if (c.equals(java.lang.Object.class)) break;
            if (c.equals(PItem.class)) {
                isItem = true;
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

    public void read() {
        pFileIdMap.clear();
        readRecursively(this.folder);
    }

    public void readRecursively(File f) {
        if (f.isDirectory()) {
            for (File f2 : Objects.requireNonNull(f.listFiles())) readRecursively(f2);
        } else {
            if (!f.getName().contains(".json")) return;
            ObjectMapper objectMapper = new ObjectMapper();
            PFile pF = null;
            try {
                pF = (PFile) objectMapper.readValue(f, this.clazz);
                pF.file = f;
                pF.type = this;
                pF.finalizeDeserialization();
                this.pFileIdMap.put(pF.id, pF);
            } catch (IOException e) {
                Main.error("Failed when loading [" + f.getName() + "] [" + e.getClass().getName() + "]");
                Main.error(e.getLocalizedMessage());
            }
        }
    }

    public void write() {
        for (PFile pF : this.pFileIdMap.values()) {
            try {
                FileWriter fileWriter = new FileWriter(pF.file);
                fileWriter.write("");
                fileWriter.write(pF.serialize());
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                Main.error("Failed when writing [" + pF.file.getName() + "]");
                Main.error(e.getLocalizedMessage());
            }
        }
    }

    public List<PFile> getPFileList() {
        Collection<PFile> pFileCollection = this.pFileIdMap.values();
        PFile[] pFileList = pFileCollection.toArray(new PFile[0]);
        Arrays.sort(pFileList, Comparator.comparing(o -> o.id));
        return Arrays.asList(pFileList);
    }

    public static void putPType(JavaPlugin plugin, String id, Class<?> clazz) {
        PType pT = new PType(plugin, id, clazz);
        pTypeIdMap.put(pT.id, pT);
        pTypeClazzMap.put(clazz, pT);
        pT.read();
    }

    public static PType getPType(String id) {
        if (pTypeIdMap.containsKey(id)) return pTypeIdMap.get(id);
        return null;
    }

    public static PType getPType(Class<?> clazz) {
        if (pTypeClazzMap.containsKey(clazz)) return pTypeClazzMap.get(clazz);
        return null;
    }

    public static void readAll() {
        for (PType pT : pTypeIdMap.values()) {
            pT.read();
        }
    }

    public static void writeAll() {
        for (PType pT : pTypeIdMap.values()) {
            pT.write();
        }
    }

    public static void clearAll() {
        pTypeIdMap.clear();
        pTypeClazzMap.clear();
    }

    public static List<PType> getPTypeList() {
        Collection<PType> pTypeCollection = pTypeIdMap.values();
        PType[] pTypeList = pTypeCollection.toArray(new PType[0]);
        Arrays.sort(pTypeList, Comparator.comparing(o -> o.id));
        return Arrays.asList(pTypeList);
    }
}
