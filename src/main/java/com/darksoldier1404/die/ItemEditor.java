package com.darksoldier1404.die;

import com.darksoldier1404.die.commands.IECommand;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public final class ItemEditor extends JavaPlugin {
    public static String prefix = "§f[ §6EIE §f] ";
    private static ItemEditor plugin;
    public static List<String> materials = new ArrayList<>();

    public static ItemEditor getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        getCommand("eie").setExecutor(new IECommand());
        initMaterialList();
    }

    public void initMaterialList() {
        for(Material m : Material.values()) {
            materials.add(m.name());
        }
    }
}
