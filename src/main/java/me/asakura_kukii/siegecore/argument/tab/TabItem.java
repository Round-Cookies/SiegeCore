package me.asakura_kukii.siegecore.argument.tab;

import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.argument.PArgument;
import me.asakura_kukii.siegecore.argument.PSender;

import java.util.ArrayList;
import java.util.List;

public class TabItem {
    public static List<String> tabItem(PSender sender, PArgument argument) {
        List<String> sL = new ArrayList<>();

        String s = argument.nextString();
        if (!argument.success) {
            return new ArrayList<>();
        }
        switch (s) {
            case "list":
                return tabItemList(sender, argument);
            case "give":
                return tabItemGive(sender, argument);
            default:
                if (PArgument.completeString("give", s)) sL.add("give");
                if (PArgument.completeString("list", s)) sL.add("list");
                return TabHandler.noTabNext(sL, argument);
        }
    }

    public static List<String> tabItemGive(PSender sender, PArgument argument) {
        List<String> sL = new ArrayList<>();

        String typeId = argument.nextString();
        if (!argument.success) {
            return new ArrayList<>();
        }

        if (PType.getPType(typeId) == null) {
            return tabType(typeId);
        }

        PType pT = PType.getPType(typeId);

        String fileId = argument.nextString();
        if (!argument.success) {
            return new ArrayList<>();
        }

        assert pT != null;
        if (pT.getPFile(fileId) == null) {
            return tabFile(pT, fileId);
        }
        return TabHandler.noTabNext(sL, argument);
    }

    public static List<String> tabItemList(PSender sender, PArgument argument) {
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
            if (!pT.isItem) continue;
            if (PArgument.completeString(pT.id, s)) {
                sL.add(pT.id);
            }
        }
        if (sL.isEmpty()) {
            sL.add("<TYPE_ID>");
        }
        return sL;
    }

    public static List<String> tabFile(PType pT, String s) {
        List<String> sL = new ArrayList<>();
        for (PFile pF : pT.getPFileList()) {
            if (PArgument.completeString(pF.id, s)) {
                sL.add(pF.id);
            }
        }
        if (sL.isEmpty()) {
            sL.add("<FILE_ID>");
        }
        return sL;
    }
}
