package me.asakura_kukii.siegecore.util.math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.joml.Math;
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
    public PQuaternion rotationToExceptZ(PVector to) {
        if (this.length() == 0 || to.length() == 0) return null;
        PQuaternion rotation = (PQuaternion) this.clone().normalize().rotationTo(to.clone().normalize(), new PQuaternion());
        Vector3f eulerAngle = rotation.getEulerAnglesYXZ(new PVector());
        if (eulerAngle.y > PMath.pi) eulerAngle.y = eulerAngle.y - 2 * PMath.pi;
        if (eulerAngle.y < -PMath.pi) eulerAngle.y = eulerAngle.y + 2 * PMath.pi;
        return (PQuaternion) new PQuaternion().rotateLocalY(eulerAngle.y).rotateX(eulerAngle.x);
    }

    @JsonIgnore
    public int[] getIntArray() {
        return new int[] {(int) x, (int) y, (int) z};
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

    public static PVector fromIntArray(int[] i) {
        if (i.length != 3) return null;
        return new PVector(i[0], i[1], i[2]);
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

    public BlockFace getBlockFace() {
        if (Math.abs(x) > Math.abs(y)) {
            if (Math.abs(x) > Math.abs(z)) {
                return x > 0 ? BlockFace.EAST : BlockFace.WEST;
            } else {
                return z > 0 ? BlockFace.SOUTH : BlockFace.NORTH;
            }
        } else {
            if (Math.abs(y) > Math.abs(z)) {
                return y > 0 ? BlockFace.UP : BlockFace.DOWN;
            } else {
                return z > 0 ? BlockFace.SOUTH : BlockFace.NORTH;
            }
        }
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
