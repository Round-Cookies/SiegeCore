package me.asakura_kukii.siegecore.effect;

import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;

import java.io.File;

public class PPotion extends PFile {

    public PPotion() {}

    public PPotion(String id, File file, PType type) {
        super(id, file, type);
    }

    @Override
    public void finalizeDeserialization() {

    }
}
