package me.asakura_kukii.siegecore.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.asakura_kukii.siegecore.io.PType;

import java.io.File;

public class PDeco extends PItem {

    public PDeco(String id, File file, PType type) {
        super(id, file, type);
    }

    public PDeco() {}

    @Override
    public String serialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

    @Override
    public void finalizeDeserialization() {}
}
