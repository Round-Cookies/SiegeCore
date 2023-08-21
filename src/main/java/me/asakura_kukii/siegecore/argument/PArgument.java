package me.asakura_kukii.siegecore.argument;

import me.asakura_kukii.siegecore.SiegeCore;
import org.bukkit.ChatColor;

public class PArgument {
    int current = 0;
    public String error = "";
    public String label = "";
    public boolean success = true;
    private final String[] args;

    public PArgument(String label, String[] args){
        this.label = label;
        this.args = args;
    }

    public static boolean completeString(String arg, String startsWith) {
        if (arg == null || startsWith == null)
            return false;
        return arg.toLowerCase().startsWith(startsWith.toLowerCase());
    }

    public boolean hasNext(){
        return current < args.length;
    }

    public Integer nextInt(){
        try{
            String arg = args[current++];
            return Integer.parseInt(arg);
        } catch (Exception e){
            current--;
            if (!hasNext()) {
                error = "Integer expected";
            } else {
                error = "Integer expected at <" + args[current] + ">";
            }
            success = false;
            return null;
        }
    }

    public Double nextDouble(){
        try{
            String arg = args[current++];
            return Double.parseDouble(arg);
        } catch (Exception e){
            current--;
            if (!hasNext()) {
                error = "Double expected";
            } else {
                error = "Double expected at <" + args[current] + ">";
            }
            success = false;
            return null;
        }
    }

    public Long nextLong(){
        try{
            String arg = args[current++];
            return Long.parseLong(arg);
        } catch (Exception e){
            current --;
            if (!hasNext()) {
                error = "Long expected";
            } else {
                error = "Long expected at <" + args[current] + ">";
            }
            success = false;
            return null;
        }
    }

    public Boolean nextBoolean(){
        try{
            String arg = args[current++];
            return Boolean.parseBoolean(arg);
        } catch (Exception e){
            current --;
            if (!hasNext()) {
                error = "Boolean expected";
            } else {
                error = "Boolean expected at <" + args[current] + ">";
            }
            success = false;
            return null;
        }
    }

    public String nextString(){
        try{
            return args[current++];
        } catch (Exception e){
            current --;
            if (!hasNext()) {
                error = "String expected";
            } else {
                error = "String expected at <" + args[current] + ">";
            }
            success = false;
            return null;
        }
    }

    public String peek() {
        return args[current];
    }
    public String colorize() {
        StringBuilder cSB = new StringBuilder();
        cSB.append(ChatColor.GRAY).append("/").append(label).append(" ");
        int cSBI = 0;
        for (String s : args) {
            if(cSBI == 0) {
                cSB.append(ChatColor.RED);
            }
            if(cSBI == 1) {
                cSB.append(ChatColor.WHITE);
            }
            if(cSBI == 2) {
                cSB.append(ChatColor.GOLD);
            }
            if(cSBI == 3) {
                cSB.append(ChatColor.LIGHT_PURPLE);
            }
            if(cSBI == 4) {
                cSB.append(ChatColor.AQUA);
            }
            if(cSBI == 5) {
                cSB.append(ChatColor.GREEN);
            }
            if(cSBI == 6) {
                cSB.append(ChatColor.BLUE);
            }
            if(cSBI == 7) {
                cSB.append(ChatColor.DARK_RED);
            }
            cSB.append(s).append(" ");
            cSBI++;
        }
        return cSB.toString();
    }
}
