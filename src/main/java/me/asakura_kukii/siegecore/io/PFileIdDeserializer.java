package me.asakura_kukii.siegecore.io;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class PFileIdDeserializer extends JsonDeserializer<PFile> {

    @Override
    public PFile deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String s = jsonParser.getText();
        if (!s.contains(".") && s.split("\\.").length != 3) throw new IOException("Could not resolve PFile [" + s + "] " + jsonParser.getCurrentLocation());
        String typeId = s.split("\\.")[0] + "." + s.split("\\.")[1];
        String fileId = s.split("\\.")[2];
        PType type = PType.getPType(typeId);
        if (type == null) throw new IOException("Could not resolve PType [" + typeId + "] " + jsonParser.getCurrentLocation());
        if (type.getPFile(fileId) != null) {
            return type.getPFile(fileId);
        }
        throw new IOException("Could not resolve PFile [" + fileId + "] " + jsonParser.getCurrentLocation());
    }
}