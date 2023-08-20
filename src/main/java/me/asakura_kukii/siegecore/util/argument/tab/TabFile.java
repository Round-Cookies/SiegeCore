package me.asakura_kukii.siegecore.util.argument.tab;

import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.util.argument.PArgument;
import me.asakura_kukii.siegecore.util.argument.PSender;

import java.util.ArrayList;
import java.util.List;

public class TabFile {
    public static List<String> tabFile(PSender sender, PArgument argument) {
        List<String> sL = new ArrayList<>();

        String s = argument.nextString();
        if (!argument.success) {
            return new ArrayList<>();
        }
        switch (s) {
            case "list":
                return tabFileList(sender, argument);
            default:
                if (PArgument.completeString("list", s)) sL.add("list");
                return TabHandler.noTabNext(sL, argument);
        }
    }

    public static List<String> tabFileList(PSender sender, PArgument argument) {
        List<String> sL = new ArrayList<>();

        String typeId = argument.nextString();
        if (!argument.success) {
            return new ArrayList<>();
        }

        if (PType.getPType(typeId) == null) {
            return tabType(typeId);
        }
        return TabHandler.noTabNext(sL, argument);
    }

    public static List<String> tabType(String s) {
        List<String> sL = new ArrayList<>();
        for (PType pT : PType.getPTypeList()) {
            if (PArgument.completeString(pT.id, s)) {
                sL.add(pT.id);
            }
        }
        if (sL.isEmpty()) {
            sL.add("<TYPE_ID>");
        }
        return sL;
    }
}
