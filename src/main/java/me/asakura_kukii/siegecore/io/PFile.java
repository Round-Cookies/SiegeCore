package me.asakura_kukii.siegecore.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.asakura_kukii.siegecore.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class PFile {
    public String id = "";

    @JsonIgnore
    public File file = null;
    @JsonIgnore
    public PType type = null;

    public PFile(String id, File file, PType type) {
        this.id = id;
        this.file = file;
        this.type = type;
    }

    public PFile() {}

    public abstract String serialize() throws JsonProcessingException;

    public abstract void finalizeDeserialization();

    public static PFile read(PFile pF) {
        if (pF.file.exists()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                pF = (PFile) objectMapper.readValue(pF.file, pF.type.clazz);
                pF.type.putPFile(pF.id, pF);
                Main.info("Loaded [" + pF.file.getName() + "] [" + pF.id + "]");
                return pF;
            } catch (IOException e) {
                Main.error("Failed when reading [" + pF.file.getName() + "]");
                Main.error(e.getLocalizedMessage());
            }
        }
        return null;
    }

    public static void write(PFile pF) {
        try {
            FileWriter fileWriter = new FileWriter(pF.file);
            fileWriter.write("");
            fileWriter.write(pF.serialize());
            fileWriter.flush();
            fileWriter.close();
            Main.info("Saved [" + pF.file.getName() + "] [" + pF.id + "]");
        } catch (IOException e) {
            Main.error("Failed when writing [" + pF.file.getName() + "]");
            Main.error(e.getLocalizedMessage());
        }
    }
}
