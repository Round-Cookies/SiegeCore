package me.asakura_kukii.siegecore.unicode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import me.asakura_kukii.siegecore.io.*;
import me.asakura_kukii.siegecore.io.helper.PUnicodeStringDeserializer;
import me.asakura_kukii.siegecore.io.helper.PUnicodeStringSerializer;

import java.io.File;
import java.util.HashMap;

public class PUnicode extends PFile {

    public PUnicode(String id, File file, PType type) {
        super(id, file, type);
    }

    public PUnicode() {}

    @JsonSerialize(contentUsing = PUnicodeStringSerializer.class)
    @JsonDeserialize(contentUsing = PUnicodeStringDeserializer.class)
    public HashMap<String, String> map = new HashMap<>();

    @Override
    public String serialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

    @Override
    public void finalizeDeserialization() {

    }

    @Override
    public void defaultValue() {

    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }
}
