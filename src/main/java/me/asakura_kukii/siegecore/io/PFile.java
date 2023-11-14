package me.asakura_kukii.siegecore.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import me.asakura_kukii.siegecore.SiegeCore;

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

    public String serialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

    public abstract void finalizeDeserialization();

    public boolean write() {
        try {
            FileWriter fileWriter = new FileWriter(this.file);
            fileWriter.write("");
            fileWriter.write(this.serialize());
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            SiegeCore.error("Failed when writing [" + this.file.getName() + "]");
            SiegeCore.error(e.getLocalizedMessage());
            return false;
        }
    }
}
