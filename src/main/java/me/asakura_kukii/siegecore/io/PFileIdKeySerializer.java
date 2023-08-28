package me.asakura_kukii.siegecore.io;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PFileIdKeySerializer extends JsonSerializer<PFile> {
    @Override
    public void serialize(PFile pFile, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (pFile == null) {
            jsonGenerator.writeFieldName("");
        } else {
            jsonGenerator.writeFieldName(pFile.type.id + "." + pFile.id);
        }
    }
}