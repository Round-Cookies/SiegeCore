package me.asakura_kukii.siegecore.argument.tab;

import me.asakura_kukii.siegecore.argument.PArgument;
import me.asakura_kukii.siegecore.argument.PSender;

import java.util.ArrayList;
import java.util.List;

public class TabHandler {
    public static List<String> noTabNext(List<String> sL, PArgument argument) {
        if (argument.nextString() != null) {
            sL.clear();
        }
        return sL;
    }

    public static List<String> onTab(PSender sender, PArgument argument) {
        List<String> sL = new ArrayList<>();

        String s = argument.nextString();
        if (!argument.success) {
            return new ArrayList<>();
        }
        switch (s) {
            case "item":
                return TabItem.tabItem(sender, argument);
            case "file":
                return TabFile.tabFile(sender, argument);
            default:
                if (PArgument.completeString("info", s)) sL.add("info");
                if (PArgument.completeString("item", s)) sL.add("item");
                if (PArgument.completeString("file", s)) sL.add("file");
                if (PArgument.completeString("load", s)) sL.add("load");
                if (PArgument.completeString("save", s)) sL.add("save");
                if (PArgument.completeString("reload", s)) sL.add("reload");
                return noTabNext(sL, argument);
        }
    }
}
