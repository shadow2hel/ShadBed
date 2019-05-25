package com.shadow2hel.shadbed;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class CommandSBC implements TabExecutor {
    ShadBed main;

    public CommandSBC(ShadBed main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg3) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(arg3.length != 0) {

                if(arg3.length < 2) {
                    player.sendMessage("You forgot a parameter!");
                    return false;
                } else {
                    String[] words = new String[arg3.length-1];
                    System.arraycopy(arg3, 1, words, 0, arg3.length - 1);
                    String opt = arg3[0].toLowerCase();
                    switch(opt) {
                        case "percentage":
                            int input;
                            try {
                                input = Integer.parseInt(arg3[1]);
                                main.config.set("PERCENTAGE_SLEEPING", input);
                                main.config.options().copyDefaults();
                                main.saveConfig();
                                player.sendMessage(input + "% of people need to be sleeping now!");

                            } catch (IllegalArgumentException e) {
                                player.sendMessage("The last argument was not a number!");
                                return false;
                            }
                            break;
                        case "sleepmessagenight":
                            if(words.length!=0){
                                String textSleep = String.join(" ", words);
                                main.config.set("sleepMessageNight", textSleep);
                                main.config.options().copyDefaults();
                                main.saveConfig();
                            } else {
                                return false;
                            }

                            break;
                        case "leavemessagenight":
                            if(words.length!=0){
                                String textLeave = String.join(" ", words);
                                main.config.set("leaveMessageNight", textLeave);
                                main.config.options().copyDefaults();
                                main.saveConfig();
                            } else {
                                return false;
                            }
                            break;
                        case "skipmessagenight":
                            if(words.length!=0){
                                String textSkip = String.join(" ", words);
                                main.config.set("skipMessageNight", textSkip);
                                main.config.options().copyDefaults();
                                main.saveConfig();
                            } else {
                                return false;
                            }
                            break;
                        case "sleepmessagestorm":
                            if(words.length!=0){
                                String textSleep = String.join(" ", words);
                                main.config.set("sleepMessageStorm", textSleep);
                                main.config.options().copyDefaults();
                                main.saveConfig();
                            } else {
                                return false;
                            }

                            break;
                        case "leavemessagestorm":
                            if(words.length!=0){
                                String textLeave = String.join(" ", words);
                                main.config.set("leaveMessageStorm", textLeave);
                                main.config.options().copyDefaults();
                                main.saveConfig();
                            } else {
                                return false;
                            }
                            break;
                        case "skipmessagestorm":
                            if(words.length!=0){
                                String textSkip = String.join(" ", words);
                                main.config.set("skipMessageStorm", textSkip);
                                main.config.options().copyDefaults();
                                main.saveConfig();
                            } else {
                                return false;
                            }
                            break;
                        default:
                            return false;
                    }
                }
            } else {
                player.sendMessage("You didn't enter an option!");
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        //create new array
        if(command.getLabel().equals("sbchange")) {
            final String[] COMMANDS = {"sleepmessageStorm", "skipmessageStorm", "leavemessageStorm", "sleepmessageNight", "skipmessageNight", "leavemessageNight", "percentage"};
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
        } else {
            return null;
        }
    }

}
