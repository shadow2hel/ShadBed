package com.shadow2hel.shadbed;

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
import org.bukkit.event.weather.WeatherChangeEvent;

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
                                    int requiredSleepers = (int) Math.floor((main.config.getDouble("PERCENTAGE_SLEEPING") / 100) * (double) world.getPlayers().size());
                                    requiredSleepers = (requiredSleepers < 1) ? 1 : requiredSleepers;
                                    returnMessage += requiredSleepers;
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
    private void onWeatherChange(WeatherChangeEvent event) {
        for (WorldSleepCounter wrldSleeper : wrldSleepers) {
            World world = wrldSleeper.world;
            if (world == event.getWorld() && wrldSleeper.isBeingSkipped && !event.toWeatherState()) {
                wrldSleeper.isBeingSkipped = false;
            }
        }
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
    private void onPlayerBedLeaveEvent(final PlayerBedLeaveEvent event) {
        for (final WorldSleepCounter wrldsleeper : this.wrldSleepers) {
            final World world = wrldsleeper.world;
            final List<Player> players = world.getPlayers();
            if (!wrldsleeper.isBeingSkipped && event.getPlayer().getWorld() == world) {
                if (!this.isDay && wrldsleeper.counter >= 0) {
                    final WorldSleepCounter worldSleepCounter = wrldsleeper;
                    --worldSleepCounter.counter;
                    if (wrldsleeper.oldPlayer != event.getPlayer()) {
                        if (!world.hasStorm()) {
                            players.forEach(p -> p.sendMessage(this.ShadBedMessage(event.getPlayer(), SleepSetting.LEAVENIGHT)));
                        } else {
                            players.forEach(p -> p.sendMessage(this.ShadBedMessage(event.getPlayer(), SleepSetting.LEAVESTORM)));
                        }
                    } else {
                        if(!world.hasStorm()) {
                            event.getPlayer().sendMessage(this.ShadBedMessage(event.getPlayer(), SleepSetting.LEAVENIGHT));
                        } else {
                            event.getPlayer().sendMessage(this.ShadBedMessage(event.getPlayer(), SleepSetting.LEAVESTORM));
                        }
                    }
                }
                wrldsleeper.oldPlayer = event.getPlayer();
            }
        }
    }

    private void skipSomething(PlayerBedEnterEvent event) {
        for (WorldSleepCounter wrldSleeper : wrldSleepers) {
            World world = wrldSleeper.world;
            List<Player> players = world.getPlayers();

            if (world == event.getPlayer().getWorld() && !wrldSleeper.isBeingSkipped) {
                if (world.hasStorm()) {
                    if (event.getBedEnterResult() == BedEnterResult.OK) {
                        wrldSleeper.counter++;
                        if (wrldSleeper.oldPlayer != event.getPlayer()) {
                            for (Player player : players) {
                                player.sendMessage(ShadBedMessage(event.getPlayer(), SleepSetting.SLEEPSTORM));
                            }
                        } else {
                            event.getPlayer().sendMessage(ShadBedMessage(event.getPlayer(), SleepSetting.SLEEPSTORM));
                        }
                        int requiredSleepers = (int) Math.floor((main.config.getDouble("PERCENTAGE_SLEEPING") / 100) * (double) world.getPlayers().size());
                        requiredSleepers = (requiredSleepers < 1) ? 1 : requiredSleepers;
                        if (wrldSleeper.counter >= requiredSleepers) {
                            world.setTime(23470);
                            wrldSleeper.isBeingSkipped = true;
                            world.setStorm(false);
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
                    if (event.getBedEnterResult() == BedEnterResult.OK) {
                        wrldSleeper.counter++;
                        if (wrldSleeper.oldPlayer != event.getPlayer()) {
                            for (Player player : players) {
                                player.sendMessage(ShadBedMessage(event.getPlayer(), SleepSetting.SLEEPNIGHT));
                            }
                        } else {
                            event.getPlayer().sendMessage(ShadBedMessage(event.getPlayer(), SleepSetting.SLEEPNIGHT));
                        }
                        int requiredSleepers = (int) Math.floor((main.config.getDouble("PERCENTAGE_SLEEPING") / 100) * (double) world.getPlayers().size());
                        requiredSleepers = (requiredSleepers < 1) ? 1 : requiredSleepers;
                        if (wrldSleeper.counter >= requiredSleepers) {
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
}
