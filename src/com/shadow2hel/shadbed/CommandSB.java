package com.shadow2hel.shadbed;

import java.util.*;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

public class CommandSB implements TabExecutor {
    private ShadBed main;

    public CommandSB(ShadBed main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg3) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(arg3.length != 0) {
                String[] words = new String[arg3.length-1];
                for (int i = 1; i < arg3.length; i++) {
                    words[i] = arg3[i];
                }
                if(arg3.length < 1) {
                    player.sendMessage("You forgot a parameter!");
                    return false;
                } else {
                    switch(arg3[0].toLowerCase()) {
                        case "reload":
                            Collection<? extends Player> players = main.getServer().getOnlinePlayers();
                            for(Player pl : players) {
                                if(pl.hasPermission("ShadBed.sb")) {
                                    pl.sendMessage("Reloading ShadBed config..");
                                }
                                if(pl.isSleeping()) {
                                    LivingEntity le = (LivingEntity) pl;
                                    if(pl.getGameMode() == GameMode.CREATIVE){
                                        pl.setGameMode(GameMode.SURVIVAL);
                                        le.damage(0);
                                        pl.setGameMode(GameMode.CREATIVE);
                                    } else {
                                        le.damage(0);
                                    }
                                }
                            }
                            main.reloadConfig();
                            break;
                    }
                }
            } else {
                player.sendMessage(main.config.getInt("PERCENTAGE_SLEEPING") + "% of people need to be sleeping.");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        //create new array
        final String[] COMMANDS = {"reload"};
        final List<String> completions = new ArrayList<>();
        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        if(strings.length>1){
            completions.clear();
        } else {
            StringUtil.copyPartialMatches(strings[0], Arrays.asList(COMMANDS), completions);
        }
        //sort the list
        Collections.sort(completions);
        return completions;
    }
}
