package me.asakura_kukii.siegecore.stringanim;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.trigger.PTask;
import me.asakura_kukii.siegecore.trigger.PTrigger;
import me.asakura_kukii.siegecore.util.format.PFormat;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PStringAnim extends PFile {

    public PStringAnim(String id, File file, PType type) {
        super(id, file, type);
    }

    public PStringAnim() {}

    public static HashMap<String, PTask> taskMap = new HashMap<>();

    public List<String> stringList = new ArrayList<>();

    @JsonIgnore
    public String getString() {
        if (stringList.isEmpty()) return null;
        return PFormat.format(stringList.get(0));
    }

    @JsonIgnore
    public String getString(float f) {
        if (stringList.isEmpty()) return null;
        int index = (int) Math.floor(f * stringList.size());
        if (index == stringList.size()) index = stringList.size() - 1;
        return PFormat.format(stringList.get(index));
    }

    public static void sendSubTitle(Player p, PStringAnim pSA, int duration, boolean durationForEachString) {
        if (pSA == null) return;
        if (durationForEachString) duration = duration * pSA.stringList.size();
        if (taskMap.containsKey(p.getUniqueId().toString())) {
            taskMap.get(p.getUniqueId().toString()).stop();
        }
        float durationFloat = duration;
        PTask pT = new PTask() {
            @Override
            public void init() {
                p.sendTitle("", pSA.getString(0F), 0, 2, 0);
            }

            @Override
            public void hold() {
                p.sendTitle("", pSA.getString((durationFloat - this.lifeTime) / durationFloat), 0, 2, 0);
            }

            @Override
            public void goal() {
                taskMap.remove(p.getUniqueId().toString());
            }
        };
        pT.runPTask((long) duration);
        taskMap.put(p.getUniqueId().toString(), pT);
    }

    public static void stop(Player p) {
        if (taskMap.containsKey(p.getUniqueId().toString())) {
            taskMap.get(p.getUniqueId().toString()).stop();
        }
    }

    @Override
    public void finalizeDeserialization() {

    }

    @Override
    public void defaultValue() {

    }
}
