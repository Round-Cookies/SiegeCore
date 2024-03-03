package me.asakura_kukii.siegecore.io.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.asakura_kukii.siegecore.io.PFile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemStackSerializer extends JsonSerializer<ItemStack> {
    @Override
    public void serialize(ItemStack itemStack, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (itemStack == null) {
            jsonGenerator.writeNull();
        } else {
            String s;
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
                dataOutput.writeObject(itemStack);
                dataOutput.close();
                s = Base64Coder.encodeLines(outputStream.toByteArray());
                jsonGenerator.writeString(s);
            } catch (IOException e) {
                e.printStackTrace();
                jsonGenerator.writeNull();
            }
        }
    }
}