package me.asakura_kukii.siegecore.util.math;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PVectorDeserializer extends JsonDeserializer<PVector> {

    @Override
    public PVector deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        List<Double> valueList = new ArrayList<>();
        if (jsonParser.currentToken() == JsonToken.START_ARRAY) {
            jsonParser.nextToken();
            while (jsonParser.currentToken() != JsonToken.END_ARRAY) {
                valueList.add(jsonParser.getDoubleValue());
                jsonParser.nextToken();
            }
        }
        if (valueList.size() != 3) throw new IOException("Could not resolve PVector [" + jsonParser.getText() + "]");
        return new PVector(valueList.get(0).floatValue(), valueList.get(1).floatValue(), valueList.get(2).floatValue());
    }
}