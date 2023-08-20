package me.asakura_kukii.siegecore.util.format;
import me.asakura_kukii.siegecore.Main;
import me.asakura_kukii.siegecore.io.PFile;
import me.asakura_kukii.siegecore.io.PType;
import me.asakura_kukii.siegecore.unicode.PUnicode;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormatHandler {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static List<String> format(List<String> stringList) {
        List<String> mappedStringList = new ArrayList<>();
        for (String s : stringList) {
            mappedStringList.add(format(s));
        }
        return mappedStringList;
    }

    public static String format(String s) {
        if (s == null) {
            return null;
        }
        if(s.contains("&")) {
            String[] subStringList = s.split("&");
            subStringList[0] = "";
            for (String subString : subStringList) {
                if (subString.length() != 6) continue;
                try {
                    ChatColor c = ChatColor.of("#" + subString);
                    s = s.replaceAll("&" + subString + "&", c.toString());
                } catch (Exception ignored) {
                }
            }
        }
        if(s.contains("$") && PType.getPType(PUnicode.class) != null) {
            PType pT = PType.getPType(PUnicode.class);
            assert pT != null;
            String[] subStringList = s.split("\\$");
            subStringList[0] = "";
            Main.error(Arrays.toString(subStringList));
            for (String subString : subStringList) {
                if (subString.length() == 0) continue;
                for (PFile pF : pT.getPFileList()) {
                    PUnicode pU = (PUnicode) pF;
                    if (pU.map.containsKey(subString)) {
                        s = s.replaceAll("\\$" + subString + "\\$", pU.map.get(subString));
                    }
                }
            }
        }
        s = ChatColor.translateAlternateColorCodes('&', s);
        return s;
    }

    public static ChatColor gen(int r, int g, int b) {
        if (r < 0) r = 0;
        if (r > 255) r = 255;
        if (g < 0) g = 0;
        if (g > 255) g = 255;
        if (b < 0) b = 0;
        if (b > 255) b = 255;
        String hexR = Integer.toHexString(r);
        if (hexR.length() < 2) {
            hexR = "0" + hexR;
        }
        String hexG = Integer.toHexString(g);
        if (hexG.length() < 2) {
            hexG = "0" + hexG;
        }
        String hexB = Integer.toHexString(b);
        if (hexB.length() < 2) {
            hexB = "0" + hexB;
        }
        String colorCode = "#" + hexR + hexG + hexB;
        return ChatColor.of(colorCode);
    }
}
