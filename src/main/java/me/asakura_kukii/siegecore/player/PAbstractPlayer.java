package me.asakura_kukii.siegecore.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.util.math.PAxis;
import me.asakura_kukii.siegecore.util.math.PMath;
import me.asakura_kukii.siegecore.util.math.PVector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class PAbstractPlayer extends PFile {

    public PAbstractPlayer(String id, File file, PType type) {
        super(id, file, type);
    }

    public PAbstractPlayer() {}

    @JsonIgnore
    public PVector getDirection() {
        Player p = Bukkit.getPlayer(id);
        if (p == null) return null;
        Vector v = p.getLocation().getDirection();
        return new PVector((float) v.getX(), (float) v.getY(), (float) v.getZ());
    }

    @JsonIgnore
    public PAxis getPAxis() {
        Player p = Bukkit.getPlayer(id);
        if (p == null) return null;
        return new PAxis(this.getDirection(), p.getLocation().getYaw() / 180F * PMath.pi);
    }
}
