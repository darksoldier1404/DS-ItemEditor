package com.darksoldier1404.die.commands;

import com.darksoldier1404.die.ItemEditor;
import com.darksoldier1404.die.functions.IEFunction;
import com.darksoldier1404.die.utils.NBT;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("all")
public class IECommand implements CommandExecutor, TabCompleter {
    private final String prefix = ItemEditor.prefix;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("player use only");
            return false;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage(prefix + "/ie name <Display Name>");
            p.sendMessage(prefix + "/ie lore add <lore>");
            p.sendMessage(prefix + "/ie lore del <line>");
            p.sendMessage(prefix + "/ie lore set <line> <lore>");
            p.sendMessage(prefix + "/ie type <Material>");
            p.sendMessage(prefix + "/ie nbt del <key>");
            p.sendMessage(prefix + "/ie nbt set <key> <value>");
            p.sendMessage(prefix + "/ie nbt list");
            return false;
        }
        if (p.getInventory().getItemInMainHand().getType() == Material.AIR) {
            p.sendMessage(prefix + "please hold an item for edit");
            return false;
        }
        ItemStack item = p.getInventory().getItemInMainHand();
        ItemMeta im = item.getItemMeta();
        if (args[0].equals("name")) {
            if (args.length == 1) {
                p.sendMessage(prefix + "please enter the display name");
                return false;
            }
            im.setDisplayName(IEFunction.getColoredText(args, 1));
            item.setItemMeta(im);
            return false;
        }
        if (args[0].equals("lore")) {
            if (args.length == 1) {
                p.sendMessage(prefix + "please enter the lore options");
                return false;
            }
            if (args[1].equals("add")) {
                if (!im.hasLore()) {
                    im.setLore(Collections.singletonList(IEFunction.getColoredText(args, 2)));
                    item.setItemMeta(im);
                    return false;
                }
                List<String> lore = im.getLore();
                lore.add(IEFunction.getColoredText(args, 2));
                im.setLore(lore);
                item.setItemMeta(im);
                return false;
            }
            if (args[1].equals("del")) {
                if (args.length == 2) {
                    p.sendMessage(prefix + "please enter the line number");
                    return false;
                }
                if (!im.hasLore()) {
                    p.sendMessage(prefix + "this item has no lore");
                    return false;
                }
                try {
                    List<String> lore = im.getLore();
                    assert lore != null;
                    lore.remove(Integer.parseInt(args[2]));
                    im.setLore(lore);
                    item.setItemMeta(im);
                } catch (Exception e) {
                    p.sendMessage(prefix + "line number must be number");
                    return false;
                }
                return false;
            }
        }
        if (args[0].equals("type")) {
            if (args.length == 1) {
                p.sendMessage(prefix + "please enter the material name");
                return false;
            }
            try {
                Material m = Material.getMaterial(args[1]);
                item.setType(m);
            } catch (Exception e) {
                p.sendMessage(prefix + "this is not a minecraft material name");
                return false;
            }
        }
        if (args[0].equals("nbt")) {
            if (args.length == 1) {
                p.sendMessage(prefix + "please enter the nbt options");
                return false;
            }
            if (args[1].equals("set")) {
                if (args.length == 2) {
                    p.sendMessage(prefix + "please enter the nbt key");
                    return false;
                }
                if (args.length == 3) {
                    p.sendMessage(prefix + "please enter the nbt value");
                    return false;
                }
                p.getInventory().setItemInMainHand(NBT.setTag(item, args[2], args[3]));
                return false;
            }
            if (args[1].equals("del")) {
                if (args.length == 2) {
                    p.sendMessage(prefix + "please enter the nbt key");
                    return false;
                }
                p.getInventory().setItemInMainHand(NBT.removeTag(item, args[2]));
                return false;
            }
            if (args[1].equals("list")) {
                String s = "";
                Map<String, String> tags = NBT.getAllStringTag(item);
                if(tags.size() == 0) {
                    p.sendMessage(prefix + "no nbt found.");
                    return false;
                }
                for(String key : tags.keySet()) {
                    if(key.equals("display")) continue;
                    String value = tags.get(key);
                    s += "key : " + key + " | value: " + value + "\n";
                }
                p.sendMessage(prefix + s);
                return false;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("player use only");
            return new ArrayList<>();
        }
        if (p.getInventory().getItemInMainHand().getType() == Material.AIR) {
            p.sendMessage(prefix + "please hold an item for edit");
            return new ArrayList<>();
        }
        ItemStack item = p.getInventory().getItemInMainHand();
        if (args.length == 1) {
            return Arrays.asList("name", "lore", "type", "nbt");
        }
        if (args[0].equals("lore")) {
            if (args.length == 2) {
                return Arrays.asList("add", "del", "set");
            }
            if (args[1].equals("del") || args[1].equals("set")) {
                List<String> index = new ArrayList<>();
                for (int i = 0; i < item.lore().size(); i++) {
                    index.add(i + "");
                }
                return index;
            }
        }
        if (args[0].equals("type")) {
            if (args.length == 2) {
                return ItemEditor.materials;
            }
        }
        if (args[0].equals("nbt")) {
            if (args.length == 2) {
                return Arrays.asList("set", "del", "list");
            }
        }
        return new ArrayList<>();
    }
}
