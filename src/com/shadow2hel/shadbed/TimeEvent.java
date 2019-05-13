package com.shadow2hel.shadbed;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Timer;
import java.util.TimerTask;

public class TimeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Timer timer;
    private TimerTask repeatedTask;
    private long lastTime;
    private ShadBed main;

    public TimeEvent(ShadBed main){
        this.main = main;
        lastTime = main.earth.getTime();
        initiate();
    }

    private void initiate(){
        repeatedTask = new TimerTask() {
            public void run() {
                if(main.earth.getTime() < lastTime){
                    lastTime = main.earth.getTime();
                    Bukkit.getPluginManager().callEvent(new DayEvent());
                }
            }
        };
        timer = new Timer("Day Timer");
        long delay  = 0L;
        long period = 1000L * 60 * 2;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}