package com.shadow2hel.shadbed;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ShadBed extends JavaPlugin {

    public FileConfiguration config = this.getConfig();
    public World earth;

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

        for(World world : getServer().getWorlds()){
            if(world.getEnvironment() == World.Environment.NORMAL){
                earth = world;
            }
        }
        config.addDefault("PERCENTAGE_SLEEPING", 75);
        config.addDefault("Enabled", true);
        config.addDefault("sleepMessage", "%player% is sleeping [%sleepers%/%total%]");
        config.addDefault("leaveMessage", "%player% left the bed [%sleepers%/%total%]");
        config.addDefault("skipMessage", "Skipping the night..");
        config.options().copyDefaults();
        saveConfig();
        this.getCommand("sb").setExecutor(new CommandSB(this));
        this.getCommand("sbchange").setExecutor(new CommandSBC(this));
        BedListener bl = new BedListener(this);
        getServer().getPluginManager().registerEvents(bl, this);
        new TimeEvent(this, 2000);
        getServer().getPluginManager().registerEvents(new DayListener(bl), this);
    }



}
