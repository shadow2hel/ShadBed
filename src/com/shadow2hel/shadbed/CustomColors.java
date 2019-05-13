package com.shadow2hel.shadbed;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomColors {
    private static final Map<String, ChatColor> colormap = new LinkedHashMap<String, ChatColor>(){{
        put("&0", ChatColor.BLACK);
        put("&1", ChatColor.DARK_BLUE);
        put("&2", ChatColor.DARK_GREEN);
        put("&3", ChatColor.DARK_AQUA);
        put("&4", ChatColor.DARK_RED);
        put("&5", ChatColor.DARK_PURPLE);
        put("&6", ChatColor.GOLD);
        put("&7", ChatColor.GRAY);
        put("&8", ChatColor.DARK_GRAY);
        put("&9", ChatColor.BLUE);
        put("&a", ChatColor.GREEN);
        put("&b", ChatColor.AQUA);
        put("&c", ChatColor.RED);
        put("&d", ChatColor.LIGHT_PURPLE);
        put("&e", ChatColor.YELLOW);
        put("&f", ChatColor.WHITE);
        put("&k", ChatColor.MAGIC);
        put("&l", ChatColor.BOLD);
        put("&m", ChatColor.STRIKETHROUGH);
        put("&n", ChatColor.UNDERLINE);
        put("&o", ChatColor.ITALIC);
        put("&r", ChatColor.RESET);
    }};

    private CustomColors(){}

    public static ChatColor getColor(String colorCode){
        return colormap.get(colorCode);
    }

    public static HashMap<String, ChatColor> getMap(){
        return (HashMap<String, ChatColor>) colormap;
    }
}
