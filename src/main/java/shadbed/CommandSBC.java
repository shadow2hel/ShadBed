package shadbed;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import shadow2hel.shadylibrary.ShadyLibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
                                main.updateConfig("PERCENTAGE_SLEEPING", input);
                                player.sendMessage(String.format("%s%s%% of people need to be sleeping!", shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(main.config.getString("prefix")), input));

                            } catch (IllegalArgumentException e) {
                                player.sendMessage("The last argument was not a number!");
                                return false;
                            }
                            break;
                        case "sleepmessagenight":
                            if(words.length!=0){
                                String textSleep = String.join(" ", words);
                                main.updateConfig("sleepMessageNight", textSleep);
                                sender.sendMessage(String.format("%sSleepmessagenight has been changed to %s", shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(main.config.getString("prefix")),
                                        shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(textSleep)));
                            } else {
                                return false;
                            }

                            break;
                        case "leavemessagenight":
                            if(words.length!=0){
                                String textLeave = String.join(" ", words);
                                main.updateConfig("leaveMessageNight", textLeave);
                                sender.sendMessage(String.format("%sLeavemessagenight has been changed to %s", shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(main.config.getString("prefix")),
                                        shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(textLeave)));
                            } else {
                                return false;
                            }
                            break;
                        case "skipmessagenight":
                            if(words.length!=0){
                                String textSkip = String.join(" ", words);
                                main.updateConfig("skipMessageNight", textSkip);
                                sender.sendMessage(String.format("%sSkipmessagenight has been changed to %s", shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(main.config.getString("prefix")),
                                        shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(textSkip)));
                            } else {
                                return false;
                            }
                            break;
                        case "sleepmessagestorm":
                            if(words.length!=0){
                                String textSleep = String.join(" ", words);
                                main.updateConfig("sleepMessageStorm", textSleep);
                                sender.sendMessage(String.format("%sPrefix has been changed to %s", shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(main.config.getString("prefix")),
                                        shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(textSleep)));
                            } else {
                                return false;
                            }
                            break;
                        case "leavemessagestorm":
                            if(words.length!=0){
                                String textLeave = String.join(" ", words);
                                main.updateConfig("leaveMessageStorm", textLeave);
                                sender.sendMessage(String.format("%sleaveMessageStorm has been changed to %s", shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(main.config.getString("prefix")),
                                        shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(textLeave)));
                            } else {
                                return false;
                            }
                            break;
                        case "skipmessagestorm":
                            if(words.length!=0){
                                String textSkip = String.join(" ", words);
                                main.updateConfig("skipMessageStorm", textSkip);
                                sender.sendMessage(String.format("%sSkipmessagestorm has been changed to %s", shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(main.config.getString("prefix")),
                                        shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(textSkip)));
                            } else {
                                return false;
                            }
                            break;
                        case "prefix":
                            if(words.length!=0){
                                String textPrefix = String.join(" ", words);
                                String oldPrefix = shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(main.config.getString("prefix"));
                                main.updateConfig("prefix", textPrefix + " ");
                                sender.sendMessage(String.format("%sPrefix has been changed to %s", oldPrefix,
                                        shadow2hel.shadylibrary.util.StringUtil.filterColorCoding(textPrefix)));
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
            final String[] COMMANDS = {"sleepmessageStorm", "skipmessageStorm", "leavemessageStorm", "sleepmessageNight", "skipmessageNight", "leavemessageNight", "percentage", "prefix"};
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
