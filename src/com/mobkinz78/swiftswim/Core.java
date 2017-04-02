package com.mobkinz78.swiftswim;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Mark on 3/27/2016.
 * There are some console errors that I think just need to
 * be surrounded by try and catch...
 */
public class Core extends JavaPlugin implements Listener {

    public static String prefix = "§8(§b§lSwift Swim§8)§r ";

    private static Core instance;

    @Override
    public void onEnable() {
        getLogger().info(prefix + "Swift Swim has been successfully enabled!");
        getServer().getPluginManager().registerEvents(new Events(), this);
        getCommand("swiftswim").setExecutor(new Commands());
        createConfig();

        Core.instance = this;
        if(getConfig().getStringList("enabled-worlds").isEmpty()){
            getLogger().log(Level.WARNING, "[Swift Swim] There are no worlds in the configuration file!");
            getLogger().log(Level.INFO, "[Swift Swim] Please note that the plugin may not work correctly if no worlds are put into the list.");
        }
        //§
    }

    @Override
    public void onDisable() {
        getLogger().info(prefix + "Swift Swim has been successfully disabled!");
    }

    public static ArrayList<UUID> playerIds = new ArrayList<UUID>(); // Not really necessary, just stores UUIDs. Plugin resets on restart anyway. Fun to maybe store names in config?
    public static ArrayList<Player> playerNames = new ArrayList<Player>(); // Players in this arraylist are given speed 2 for swift swim
    public static ArrayList<World> worlds = new ArrayList<World>(); // Use this arraylist to add worlds from a config file.

    public static Core getInstance(){
        return instance;
    }

    public void createConfig(){
        if(!getDataFolder().exists()){
            getDataFolder().mkdirs();
        }
        File file = new File(getDataFolder(), "config.yml");
        if(!file.exists()){
            getLogger().info(prefix + "Config.yml not found, creating now...");
            saveDefaultConfig();
        } else {
            getLogger().info(prefix + "Config.yml found for HotSauce, loading now...");
        }
    }

}

