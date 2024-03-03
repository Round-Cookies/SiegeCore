package me.asakura_kukii.siegecore.io.helper;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class PUnicodeStringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String s = jsonParser.getText();
        try {
            int i = Integer.parseInt(s, 16);
            char[] c = Character.toChars(i);
            return new String(c);
        } catch (Exception ignored) {
            throw new IOException("Could not resolve PUnicodeString [" + s + "]");
        }
    }
}
