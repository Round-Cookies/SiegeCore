package me.asakura_kukii.siegecore.io.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bukkit.World;

import java.io.IOException;

public class WorldSerializer extends JsonSerializer<World> {
    @Override
    public void serialize(World w, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (w == null) {
            jsonGenerator.writeString("");
        } else {
            jsonGenerator.writeString(w.getName());
        }
    }
}
