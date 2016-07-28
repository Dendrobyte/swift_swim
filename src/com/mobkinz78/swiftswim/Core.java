package com.mobkinz78.swiftswim;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Mark on 3/27/2016.
 * I am aware this is all in one class and really shouldn't be. There are some console errors that I think just need to
 * be surrounded by try and catch...
 */
public class Core extends JavaPlugin implements Listener {

    public static String prefix = "§8(§b§lSwift Swim§8)§r ";

    @Override
    public void onEnable() {
        getLogger().info(prefix + "Swift Swim has been successfully enabled!");
        getServer().getPluginManager().registerEvents(new Events(), this);
        getCommand("swiftswim").setExecutor(new Commands());
        //§
    }

    @Override
    public void onDisable() {
        getLogger().info(prefix + "Swift Swim has been successfully disabled!");
    }

    public static ArrayList<UUID> playerIds = new ArrayList<UUID>(); // Not really necessary, just stores UUIDs. Plugin resets on restart anyway. Fun to maybe store names in config?
    public static ArrayList<Player> playerNames = new ArrayList<Player>(); // Players in this arraylist are given speed 2 for swift swim
    public static ArrayList<World> worlds = new ArrayList<World>(); // Use this arraylist to add worlds from a config file.

}

