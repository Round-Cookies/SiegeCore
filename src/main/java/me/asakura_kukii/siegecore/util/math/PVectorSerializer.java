package me.asakura_kukii.siegecore.util.math;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.asakura_kukii.siegecore.io.PFile;

import java.io.IOException;

public class PVectorSerializer extends JsonSerializer<PVector> {
    @Override
    public void serialize(PVector pVector, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (pVector == null) {
            jsonGenerator.writeArray(new double[]{0, 0, 0}, 0, 3);
        } else {
            jsonGenerator.writeArray(new double[]{pVector.x, pVector.y, pVector.z}, 0, 3);
        }
    }
}