package me.asakura_kukii.siegecore.io.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;

public class WorldDeserializer extends JsonDeserializer<World> {

    @Override
    public World deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String s = jsonParser.getText();
        World w = Bukkit.getWorld(s);
        if (w == null) throw new IOException("Could not resolve World [" + s + "]");
        return w;
    }
}
