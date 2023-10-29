package me.asakura_kukii.siegecore.item;

import me.asakura_kukii.siegecore.io.PType;

import java.io.File;

public class PItem extends PAbstractItem {

    public PItem(String id, File file, PType type) {
        super(id, file, type);
    }

    public PItem() {}

    @Override
    public void finalizeDeserialization() {}
}
