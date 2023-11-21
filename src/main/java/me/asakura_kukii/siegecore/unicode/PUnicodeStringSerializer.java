package me.asakura_kukii.siegecore.unicode;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PUnicodeStringSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        StringBuilder sB = new StringBuilder();
        for (char c : s.toCharArray()) {
            sB.append(Integer.toHexString(c));
        }
        jsonGenerator.writeString(sB.toString());
    }
}
