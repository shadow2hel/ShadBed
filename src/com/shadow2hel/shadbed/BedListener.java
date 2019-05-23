package com.shadow2hel.shadbed;

import com.shadow2hel.shadylibrary.ShadyLibrary;
import com.shadow2hel.shadylibrary.util.StringUtil;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.LinkedList;
import java.util.List;

public class BedListener implements Listener {
    public List<WorldSleepCounter> wrldSleepers;
    private boolean isDay;
    private ShadBed main;

    private enum SleepSetting {
        SKIPNIGHT, SLEEPNIGHT, LEAVENIGHT, SKIPSTORM, SLEEPSTORM, LEAVESTORM
    }

    public BedListener(ShadBed main) {
        wrldSleepers = new LinkedList<>();
        for (World world : main.getServer().getWorlds()) {
            if (world.getEnvironment() == World.Environment.NORMAL) {
                wrldSleepers.add(new WorldSleepCounter(world));
            }
        }
        this.main = main;
    }

    private String filterPlaceholder(String text, Player crntSleeper) {
        for (WorldSleepCounter wrldsleeper : wrldSleepers) {
            World world = wrldsleeper.world;
            int sleepers = wrldsleeper.counter;
            if (crntSleeper.getWorld() == world) {
                String returnMessage = "";
                boolean found = false;
                text = StringUtil.filterColorCoding(text);
                int beginIndex = 0;
                int lastIndex = 0;
                for (int i = 0; i < text.length(); i++) {
                    char letter = text.charAt(i);
                    if (letter == '%') {
                        if (found) {
                            lastIndex = i;
                            String sub = text.substring(beginIndex, lastIndex + 1);
                            switch (sub) {
                                case "%player%":
                                    returnMessage += crntSleeper.getDisplayName();
                                    break;
                                case "%total%":
                                    returnMessage += world.getPlayers().size();
                                    break;
                                case "%sleepers%":
                                    returnMessage += sleepers;
                                    break;
                                case "%required%":
                                    int perc = (int)((main.config.getDouble("PERCENTAGE_SLEEPING")/100) * (double) world.getPlayers().size());
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
                    } else if (!found) {
                        returnMessage += letter;
                    }
                }
                return returnMessage;
            }
        }
        return null;
    }

    private String ShadBedMessage(Player crntSleeper, SleepSetting typeMessage) {
        String returnMessage;
        String text = "";
        switch (typeMessage) {
            case SKIPNIGHT:
                text = main.config.getString("skipMessageNight");
                break;
            case LEAVENIGHT:
                text = main.config.getString("leaveMessageNight");
                break;
            case SLEEPNIGHT:
                text = main.config.getString("sleepMessageNight");
                break;
            case SKIPSTORM:
                text = main.config.getString("skipMessageStorm");
                break;
            case LEAVESTORM:
                text = main.config.getString("leaveMessageStorm");
                break;
            case SLEEPSTORM:
                text = main.config.getString("sleepMessageStorm");
                break;
            default:
        }
        returnMessage = filterPlaceholder(main.getConfig().getString("prefix") + text, crntSleeper);

        return returnMessage;
    }

    @EventHandler
    private void onPlayerBedEnter(PlayerBedEnterEvent event) {
        FileConfiguration config = main.config;
        if (isDay) {
            isDay = false;
        }

        skipSomething(event);
    }

    @EventHandler
    private void onPlayerBedLeaveEvent(PlayerBedLeaveEvent event) {
        for (WorldSleepCounter wrldsleeper : wrldSleepers) {
            World world = wrldsleeper.world;
            List<Player> players = world.getPlayers();
            if (event.getPlayer().getWorld() == world) {
                if (!isDay && wrldsleeper.counter >= 0) {
                    wrldsleeper.counter--;
                    if (wrldsleeper.oldPlayer != event.getPlayer()) {
                        if (!world.hasStorm()) {
                            players.forEach(p -> p.sendMessage(ShadBedMessage(event.getPlayer(), SleepSetting.LEAVENIGHT)));
                        } else {
                            players.forEach(p -> p.sendMessage(ShadBedMessage(event.getPlayer(), SleepSetting.LEAVESTORM)));
                        }
                    }
                }
                wrldsleeper.oldPlayer = event.getPlayer();
            }
        }
    }

    private void skipSomething(PlayerBedEnterEvent event){
        for (WorldSleepCounter wrldSleeper : wrldSleepers) {
            World world = wrldSleeper.world;
            List<Player> players = world.getPlayers();

            if(world.hasStorm()){
                if (event.getPlayer().getWorld() == world && event.getBedEnterResult() == BedEnterResult.OK) {
                    wrldSleeper.counter++;
                    if (wrldSleeper.oldPlayer != event.getPlayer()) {
                        for (Player player : players) {
                            player.sendMessage(ShadBedMessage(event.getPlayer(), SleepSetting.SLEEPSTORM));
                        }
                    }
                    if (((double) wrldSleeper.counter / (double) world.getPlayers().size()) * 100 >= main.getConfig().getInt("PERCENTAGE_SLEEPING")) {
                        world.setTime(23470);
                        players.forEach(p -> {
                            if (p.isSleeping()) {
                                p.setStatistic(Statistic.TIME_SINCE_REST, 0); // RESETTING PHANTOM SPAWNING FUCK YEAH
                            }
                            p.sendMessage(ShadBedMessage(event.getPlayer(), SleepSetting.SKIPSTORM));
                        });
                        wrldSleeper.counter = 0;
                        isDay = true;
                        wrldSleeper.oldPlayer = null;
                    }
                }
            } else {
                if (event.getPlayer().getWorld() == world && event.getBedEnterResult() == BedEnterResult.OK) {
                    wrldSleeper.counter++;
                    if (wrldSleeper.oldPlayer != event.getPlayer()) {
                        for (Player player : players) {
                            player.sendMessage(ShadBedMessage(event.getPlayer(), SleepSetting.SLEEPNIGHT));
                        }
                    }
                    if (((double) wrldSleeper.counter / (double) world.getPlayers().size()) * 100 >= main.getConfig().getInt("PERCENTAGE_SLEEPING")) {
                        world.setTime(23470);
                        players.forEach(p -> {
                            if (p.isSleeping()) {
                                p.setStatistic(Statistic.TIME_SINCE_REST, 0); // RESETTING PHANTOM SPAWNING FUCK YEAH
                            }
                            p.sendMessage(ShadBedMessage(event.getPlayer(), SleepSetting.SKIPNIGHT));
                        });
                        wrldSleeper.counter = 0;
                        isDay = true;
                        wrldSleeper.oldPlayer = null;
                    }
                }
            }
        }

    }
}
