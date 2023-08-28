package me.asakura_kukii.siegecore.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PFileIdDeserializer;
import me.asakura_kukii.siegecore.io.PFileIdSerializer;
import me.asakura_kukii.siegecore.io.PType;

import java.io.File;

public class PDeco extends PItem {

    public PDeco(String id, File file, PType type) {
        super(id, file, type);
    }

    public PDeco() {}

    @Override
    public void finalizeDeserialization() {}
}
