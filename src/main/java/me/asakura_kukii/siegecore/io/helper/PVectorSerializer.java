package me.asakura_kukii.siegecore.io.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.asakura_kukii.siegecore.util.math.PVector;

import java.io.IOException;

public class PVectorSerializer extends JsonSerializer<PVector> {
    @Override
    public void serialize(PVector pVector, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (pVector == null) {
            jsonGenerator.writeArray(new double[]{0, 0, 0}, 0, 4);
        } else {
            jsonGenerator.writeArray(new double[]{pVector.x, pVector.y, pVector.z}, 0, 3);
        }
    }
}