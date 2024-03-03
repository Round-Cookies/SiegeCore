package me.asakura_kukii.siegecore.io.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;

import java.io.IOException;

public class PFileIdKeyDeserializer extends KeyDeserializer {
    @Override
    public PFile deserializeKey(String s, DeserializationContext deserializationContext) throws IOException {
        if (!s.contains(".") && s.split("\\.").length != 3) throw new IOException("Could not resolve PFile [" + s + "]");
        String typeId = s.split("\\.")[0] + "." + s.split("\\.")[1];
        String fileId = s.split("\\.")[2];
        PType type = PType.getPType(typeId);
        if (type == null) throw new IOException("Could not resolve PType [" + typeId + "]");
        if (type.getPFile(fileId) != null) {
            return type.getPFile(fileId);
        }
        throw new IOException("Could not resolve PFile [" + fileId + "]");
    }
}