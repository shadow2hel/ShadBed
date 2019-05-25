package com.shadow2hel.shadbed;

import com.shadow2hel.shadylibrary.ShadyLibrary;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ShadBed extends JavaPlugin {

    public FileConfiguration config = this.getConfig();
    public BedListener bl;
    public World earth;
    public Logger log;

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        log = Bukkit.getLogger();
        ShadyLibrary.setPlugin(this);
        for(World world : getServer().getWorlds()){
            if(world.getEnvironment() == World.Environment.NORMAL){
                earth = world;
            }
        }
        config.addDefault("PERCENTAGE_SLEEPING", 75);
        config.addDefault("Enabled", true);
        config.addDefault("prefix", "[&7Shad&3Bed&r] ");
        config.addDefault("sleepMessageNight", "%player% is sleeping [%sleepers%/%required%]");
        config.addDefault("leaveMessageNight", "%player% left the bed [%sleepers%/%required%]");
        config.addDefault("skipMessageNight", "Skipping the night..");
        config.addDefault("sleepMessageStorm", "%player% is sleeping [%sleepers%/%required%]");
        config.addDefault("leaveMessageStorm", "%player% left the bed [%sleepers%/%required%]");
        config.addDefault("skipMessageStorm", "Skipping the thunderstorm..");
        config.options().copyDefaults();
        saveConfig();
        this.getCommand("sb").setExecutor(new CommandSB(this));
        this.getCommand("sbchange").setExecutor(new CommandSBC(this));
        bl = new BedListener(this);
        getServer().getPluginManager().registerEvents(bl, this);
        new TimeEvent(this, 2000);
        getServer().getPluginManager().registerEvents(new DayListener(bl), this);
    }



}
