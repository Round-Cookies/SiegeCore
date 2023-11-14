package me.asakura_kukii.siegecore.effect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.util.math.PVector;
import me.asakura_kukii.siegecore.util.math.PVectorDeserializer;
import me.asakura_kukii.siegecore.util.math.PVectorSerializer;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class PParticle extends PFile {

    public PParticle() {}

    public PParticle(String id, File file, PType type) {
        super(id, file, type);
    }

    public Particle particle = Particle.CLOUD;

    public Material material = Material.COOKIE;

    public int count = 1;

    public double offsetX = 0;

    public double offsetY = 0;

    public double offsetZ = 0;

    public float extra = 0F;

    public boolean force = false;

    @JsonSerialize(using = PVectorSerializer.class)
    @JsonDeserialize(using = PVectorDeserializer.class)
    public PVector rgb = new PVector();

    @JsonSerialize(using = PVectorSerializer.class)
    @JsonDeserialize(using = PVectorDeserializer.class)
    public PVector rgbExtra = new PVector();

    public boolean directionAsOffset = false;

    public boolean visibleToExecutor = true;

    public boolean visibleToOthers = true;

    public float visibleRadius = 50F;

    public void spawn(LivingEntity e, PVector l) {
        spawn(e, l, new PVector(0, 0, 1), null, null, 0F);
    }

    public void spawn(LivingEntity e, PVector l, PVector d) {
        spawn(e, l, d, null, null, 0F);
    }

    public void spawn(LivingEntity e, PVector l, Material m) {
        spawn(e, l, new PVector(0, 0, 1), m, null, 0F);
    }

    public void spawn(LivingEntity e, PVector l, PVector d, Material m) {
        spawn(e, l, d, m, null, 0F);
    }

    public void spawn(LivingEntity e, PVector l, PVector d, Material m, ItemStack iS, float polar) {
        Location location = new Location(e.getWorld(), l.x, l.y, l.z);
        Object T = null;
        double offsetX;
        double offsetY;
        double offsetZ;
        if (directionAsOffset) {
            offsetX = d.x;
            offsetY = d.y;
            offsetZ = d.z;
        } else {
            offsetX = this.offsetX;
            offsetY = this.offsetY;
            offsetZ = this.offsetZ;
        }
        if (particle.equals(Particle.BLOCK_CRACK) || particle.equals(Particle.BLOCK_DUST) || particle.equals(Particle.BLOCK_MARKER) || particle.equals(Particle.FALLING_DUST)) {
            if (m == null) {
                T = material.createBlockData();
            } else {
                T = m.createBlockData();
            }
            spawnParticle(e, particle, location, count, offsetX, offsetY, offsetZ, extra, T, force);
            return;
        }
        if (particle.equals(Particle.DUST_COLOR_TRANSITION)) {
            T = new Particle.DustTransition(Color.fromRGB(rgb.r(), rgb.g(), rgb.b()), Color.fromRGB(rgbExtra.r(), rgbExtra.g(), rgbExtra.b()), extra);
            spawnParticle(e, particle, location, count, offsetX, offsetY, offsetZ, extra, T, force);
            return;
        }
        if (particle.equals(Particle.ITEM_CRACK)) {
            if (iS == null) {
                T = new ItemStack(material);
            } else {
                T = new ItemStack(iS);
            }
            spawnParticle(e, particle, location, count, offsetX, offsetY, offsetZ, extra, T, force);
            return;
        }
        if (particle.equals(Particle.REDSTONE)) {
            T = new Particle.DustOptions(Color.fromRGB(rgb.r(), rgb.g(), rgb.b()), extra);
            spawnParticle(e, particle, location, count, offsetX, offsetY, offsetZ, extra, T, force);
            return;
        }
        if (particle.equals(Particle.SPELL_MOB) || particle.equals(Particle.SPELL_MOB_AMBIENT)) {
            if (count == 0) {
                spawnParticle(e, particle, location, count, rgb.r() / 255D, rgb.g() / 255D, rgb.b() / 255D, 1, null, force);
            } else {
                spawnParticle(e, particle, location, count, offsetX, offsetY, offsetZ, extra, T, force);
            }
            return;
        }
        if (particle.equals(Particle.SCULK_CHARGE)) {
            T = polar;
            spawnParticle(e, particle, location, count, offsetX, offsetY, offsetZ, extra, T, force);
            return;
        }
        spawnParticle(e, particle, location, count, offsetX, offsetY, offsetZ, extra, T, force);
    }

    public void spawnParticle(LivingEntity e, Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, Object T, boolean force) {
        if (this.force) {
            e.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, T, force);
            return;
        }
        if (this.visibleToExecutor && e instanceof Player) {
            Player p = (Player) e;
            p.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, T);
         }
        if (this.visibleToOthers) {
            for (Entity entity : e.getWorld().getNearbyEntities(location, visibleRadius, visibleRadius, visibleRadius, entity -> entity instanceof Player)) {
                if (entity.getUniqueId() == e.getUniqueId()) continue;
                Player player = (Player) entity;
                player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, T);
            }
        }
    }

    @Override
    public void finalizeDeserialization() {
    }
}
