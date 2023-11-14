package me.asakura_kukii.siegecore.effect;

import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.util.math.PMath;
import me.asakura_kukii.siegecore.util.math.PVector;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PSound extends PFile {

    public PSound() {}

    public PSound(String id, File file, PType type) {
        super(id, file, type);
    }

    public List<String> soundList = new ArrayList<>();

    public float volumeMin = 1.0F;

    public float volumeMax = 1.0F;

    public float pitchMin = 1.0F;

    public float pitchMax = 1.0F;

    public boolean audibleToExecutor = true;

    public boolean audibleToOthers = true;

    public float audibleRadius = 50.0F;

    public void play(LivingEntity e, PVector l) {
        Location location = new Location(e.getWorld(), l.x, l.y, l.z);
        float volume = PMath.ran() * (volumeMax - volumeMin) + volumeMin;
        float pitch = PMath.ran() * (pitchMax - pitchMin) + pitchMin;

        if (audibleToExecutor && e instanceof Player) {
            for (String s : soundList) {
                ((Player) e).playSound(location, s, volume, pitch);
            }
        }
        if (audibleToOthers) {
            for (Entity entity : e.getWorld().getNearbyEntities(location, audibleRadius, audibleRadius, audibleRadius, entity -> entity instanceof Player)) {
                if (entity.getUniqueId() == e.getUniqueId()) continue;
                for (String s : soundList) {
                    ((Player) entity).playSound(location, s, volume, pitch);
                }
            }
        }
    }

    @Override
    public void finalizeDeserialization() {

    }
}
