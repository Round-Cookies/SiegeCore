package me.asakura_kukii.siegecore.util.math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

public class PVector extends Vector3f {

    public PVector() {
        super(0F, 0F, 0F);
    }

    public PVector(float x, float y, float z) {
        super(x, y, z);
    }

    @Override
    public PVector clone() {
        try {
            return (PVector) super.clone();
        } catch (Exception ignored) {
            return null;
        }
    }

    @JsonIgnore
    public Location getLocation(LivingEntity lE) {
        return getLocation(lE.getWorld());
    }

    @JsonIgnore
    public Location getLocation(World w) {
        return new Location(w, x, y, z);
    }

    @JsonIgnore
    public Vector getVector() {
        return new Vector(x, y, z);
    }

    public static PVector fromLocation(Location l) {
        return new PVector((float) l.getX(), (float) l.getY(), (float) l.getZ());
    }

    public static PVector fromVector(Vector v) {
        return new PVector((float) v.getX(), (float) v.getY(), (float) v.getZ());
    }

    public static PVector ran(float maxRadius) {
        float theta = PMath.ran() * PMath.pi;
        float phi = PMath.ran() * PMath.pi * 2;
        float radius = PMath.ran() * maxRadius;
        return new PVector(
                radius * PMath.sin(theta) * PMath.cos(phi),
                radius * PMath.cos(theta),
                radius * PMath.sin(theta) * PMath.sin(phi)
        );
    }

    public int r() {
        if (this.x < 0) return 0;
        if (this.x > 255) return 255;
        return Math.round(this.x);
    }

    public int g() {
        if (this.y < 0) return 0;
        if (this.y > 255) return 255;
        return Math.round(this.y);
    }

    public int b() {
        if (this.z < 0) return 0;
        if (this.z > 255) return 255;
        return Math.round(this.z);
    }
}
