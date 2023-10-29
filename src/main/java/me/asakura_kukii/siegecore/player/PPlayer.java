package me.asakura_kukii.siegecore.player;

import me.asakura_kukii.siegecore.io.PType;

import java.io.File;

public class PPlayer extends PAbstractPlayer{

    public PPlayer(String id, File file, PType type) {
        super(id, file, type);
    }

    public PPlayer() {}

    @Override
    public void finalizeDeserialization() {

    }
}
