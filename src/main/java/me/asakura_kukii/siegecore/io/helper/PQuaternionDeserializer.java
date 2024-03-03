package me.asakura_kukii.siegecore.io.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import me.asakura_kukii.siegecore.util.math.PQuaternion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PQuaternionDeserializer extends JsonDeserializer<PQuaternion> {

    @Override
    public PQuaternion deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        List<Double> valueList = new ArrayList<>();
        if (jsonParser.currentToken() == JsonToken.START_ARRAY) {
            jsonParser.nextToken();
            while (jsonParser.currentToken() != JsonToken.END_ARRAY) {
                valueList.add(jsonParser.getDoubleValue());
                jsonParser.nextToken();
            }
        }
        if (valueList.size() != 4) throw new IOException("Could not resolve PQuaternion [" + jsonParser.getText() + "]");
        return new PQuaternion(valueList.get(0).floatValue(), valueList.get(1).floatValue(), valueList.get(2).floatValue(), valueList.get(3).floatValue());
    }
}