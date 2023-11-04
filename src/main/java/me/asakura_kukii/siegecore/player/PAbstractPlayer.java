package me.asakura_kukii.siegecore.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public abstract class PAbstractPlayer extends PFile {

    public PAbstractPlayer(String id, File file, PType type) {
        super(id, file, type);
    }

    public PAbstractPlayer() {}

}
