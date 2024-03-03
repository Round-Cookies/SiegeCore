package me.asakura_kukii.siegecore.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.util.math.PAxis;
import me.asakura_kukii.siegecore.util.math.PMath;
import me.asakura_kukii.siegecore.util.math.PVector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class PAbstractPlayer extends PFile {

    public PAbstractPlayer(String id, File file, PType type) {
        super(id, file, type);
    }

    public PAbstractPlayer() {}

    @JsonIgnore
    public void setVelocity(PVector pV) {
        Player p = getPlayer();
        if (p == null) return;
        p.setVelocity(pV.getVector());
    }

    @JsonIgnore
    public void giveItemStack(ItemStack iS) {
        Player p = getPlayer();
        if (p == null) return;
        Item i = p.getWorld().dropItem(p.getLocation(), iS);
        i.setPickupDelay(0);
        i.setOwner(p.getUniqueId());
    }

    @JsonIgnore
    public Player getPlayer() {
        Player p = Bukkit.getPlayer(UUID.fromString(this.id));
        if (p != null && p.isOnline()) return p;
        return null;
    }

    @JsonIgnore
    public PVector getLocation() {
        Player p = getPlayer();
        if (p == null) return null;
        Location v = p.getEyeLocation();
        return PVector.fromLocation(v);
    }

    @JsonIgnore
    public PVector getFeetLocation() {
        Player p = getPlayer();
        if (p == null) return null;
        Location v = p.getLocation();
        return PVector.fromLocation(v);
    }

    @JsonIgnore
    public PVector getDirection() {
        Player p = getPlayer();
        if (p == null) return null;
        Vector v = p.getLocation().getDirection();
        return PVector.fromVector(v);
    }

    @JsonIgnore
    public PAxis getAxis() {
        Player p = getPlayer();
        if (p == null) return null;
        return new PAxis(this.getLocation(), this.getDirection(), p.getLocation().getYaw() / 180F * PMath.pi);
    }

    @JsonIgnore
    public PAxis getAxis(PVector bias) {
        Player p = getPlayer();
        if (p == null) return null;
        PAxis pA = new PAxis(this.getLocation(), this.getDirection(), p.getLocation().getYaw() / 180F * PMath.pi);
        if (p.getMainHand() == MainHand.LEFT) pA.translate(-bias.x, bias.y, bias.z);
        if (p.getMainHand() == MainHand.RIGHT) pA.translate(bias.x, bias.y, bias.z);
        return pA;
    }
}
