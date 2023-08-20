package me.asakura_kukii.siegecore.unicode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;

import java.io.File;
import java.util.HashMap;

public class PUnicode extends PFile {

    public PUnicode(String id, File file, PType type) {
        super(id, file, type);
    }

    public PUnicode() {}

    public HashMap<String, String> map = new HashMap<>();

    @Override
    public String serialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

    @Override
    public void finalizeDeserialization() {
    }
}
