package me.asakura_kukii.siegecore.io.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.asakura_kukii.siegecore.util.math.PQuaternion;

import java.io.IOException;

public class PQuaternionSerializer extends JsonSerializer<PQuaternion> {
    @Override
    public void serialize(PQuaternion pQuaternion, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (pQuaternion == null) {
            jsonGenerator.writeArray(new double[]{0, 0, 0, 0}, 0, 4);
        } else {
            jsonGenerator.writeArray(new double[]{pQuaternion.x, pQuaternion.y, pQuaternion.z, pQuaternion.w}, 0, 4);
        }
    }
}