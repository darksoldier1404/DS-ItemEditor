package com.darksoldier1404.die.functions;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Iterator;

public class IEFunction {

    public static String getColoredText(String[] args, int line) {
        StringBuilder s = new StringBuilder();
        args = Arrays.copyOfRange(args, line, args.length);
        Iterator<String> i = Arrays.stream(args).iterator();
        while (i.hasNext()) {
            s.append(i.next()).append(" ");
        }
        return ChatColor.translateAlternateColorCodes('&', s.toString());
    }
}
