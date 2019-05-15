package com.shadow2hel.shadbed;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;

import java.util.Collection;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class BedListener implements Listener {
    public Player oldPlayer;
    private int sleepers;
    private boolean isDay;
    private ShadBed main;

    private enum Setting {
        SKIP, SLEEP, LEAVE;
    }

    public BedListener(ShadBed main) {
        this.sleepers = 0;
        this.main = main;
    }

    private String filterPlaceholder(String text, Player crntSleeper){
        String returnMessage = "";
        boolean found = false;
        int beginIndex = 0;
        int lastIndex = 0;
        for(int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            if(letter == '%') {
                if(found) {
                    lastIndex = i;
                    String sub = new String(text.substring(beginIndex,lastIndex + 1));
                    switch(sub) {
                        case "%player%":
                            returnMessage += crntSleeper.getDisplayName();
                            break;
                        case "%total%":
                            returnMessage += crntSleeper.getServer().getOnlinePlayers().size();
                            break;
                        case "%sleepers%":
                            returnMessage += sleepers;
                            break;
                        case "%required%":
                            double perc =  main.config.getDouble("PERCENTAGE_SLEEPING") / (double)crntSleeper.getServer().getOnlinePlayers().size();
                            perc = Math.round(perc);
                            returnMessage += perc;
                        default:
                    }
                    found = false;
                    beginIndex = 0;
                    lastIndex = 0;
                } else {
                    beginIndex = i;
                    found = true;
                }
            } else if(!found) {
                returnMessage += letter;
            }
        }
        return returnMessage;
    }

    private String ShadBedMessage(Player crntSleeper, Setting typeMessage) {
        String returnMessage;
        String text = "";
        switch(typeMessage){
            case SKIP:
                text = main.config.getString("skipMessage");
                break;
            case LEAVE:
                text = main.config.getString("leaveMessage");
                break;
            case SLEEP:
                text = main.config.getString("sleepMessage");
                break;
            default:
        }
        returnMessage = filterPlaceholder(text, crntSleeper);

        return returnMessage;
    }

    @EventHandler
    private void onPlayerBedEnter(PlayerBedEnterEvent event) {
        FileConfiguration config = main.config;
        Collection<? extends Player> players = event.getPlayer().getServer().getOnlinePlayers();
        if(isDay) {
            isDay = false;
        }
        if(event.getBedEnterResult() == BedEnterResult.OK) {
            sleepers++;
            if(oldPlayer != event.getPlayer()) {
                players.forEach(p -> p.sendMessage(ShadBedMessage(event.getPlayer(), Setting.SLEEP)));
            }
            if(((double)sleepers/(double)players.size())*100>=config.getInt("PERCENTAGE_SLEEPING")) {
                event.getPlayer().getWorld().setTime(23470);
                players.forEach(p -> p.sendMessage(ShadBedMessage(event.getPlayer(), Setting.SKIP)));
                sleepers = 0;
                isDay = true;
                oldPlayer = null;
            }
        }

    }

    @EventHandler
    private void onPlayerBedLeaveEvent(PlayerBedLeaveEvent event) {
        Collection<? extends Player> players = event.getPlayer().getServer().getOnlinePlayers();
        if(!isDay && sleepers >= 0) {
            sleepers--;
            if(oldPlayer != event.getPlayer()) {
                players.forEach(p -> p.sendMessage(ShadBedMessage(event.getPlayer(), Setting.LEAVE)));
            }

        }
        oldPlayer = event.getPlayer();
    }
}
