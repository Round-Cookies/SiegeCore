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

    public abstract void finalizeDeserialization() throws IOException;

    public abstract void defaultValue();

    public abstract void load();

    public abstract void unload();
}
