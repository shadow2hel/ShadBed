package com.shadow2hel.shadbed;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldSleepCounter {
    public World world;
    public int counter;
    public Player oldPlayer;

    public WorldSleepCounter(World world){
        this.world = world;
        this.counter = 0;
    }
}
