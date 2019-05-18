package com.shadow2hel.shadbed;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class DayListener implements Listener {
    private static final HandlerList HANDLERS = new HandlerList();
    private BedListener bl;

    public DayListener(BedListener bl) {
        this.bl = bl;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @EventHandler
    public void onDayPassed(DayEvent event){
        for(WorldSleepCounter wrldsleeper : bl.wrldSleepers){
            wrldsleeper.oldPlayer = null;
        }
    }
}
