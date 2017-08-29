package com.mobkinz78.swiftswim;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffectType;
import com.mobkinz78.swiftswim.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mark on 7/27/2016.
 * This file will contain the events, teleport and weather change, as to not clutter up the main class.
 */
public class Events implements Listener {

    @EventHandler
    public void weatherChange(WeatherChangeEvent e) { // This throw is unchecked. There is an error that spams console???
        List<String> worldNames = Core.getInstance().getConfig().getStringList("enabled-worlds");
        if(!worldNames.contains(e.getWorld().getName())) return;
        for(String world : worldNames) {
            if (e.getWorld().getName().equalsIgnoreCase(world)) {
                if (!e.toWeatherState()) { //Turns to clear
                    ArrayList<Player> tempList = Core.playerNames; // To avoid ConcurrentModificationExceptions
                    for (Player player : tempList) {
                        Core.playerNames.remove(player); //remove players from arraylist
                        Core.playerIds.remove(player.getUniqueId());
                        player.sendMessage(Core.prefix + "§cSwift Swim has been §4§ldisabled §cbecause the rain has stopped!");

                        //Clear effects from each person
                        PotionEffectType speed = PotionEffectType.SPEED;
                        player.removePotionEffect(speed);
                    }
                }
                if (e.toWeatherState()) { //Turns to rain
                    for (Player name : Bukkit.getServer().getOnlinePlayers()) {
                        World playersWorld = name.getWorld();
                        for(String worldName : Core.getInstance().getConfig().getStringList("enabled-worlds")){
                            if (playersWorld.getName().equalsIgnoreCase(worldName)) {
                                name.sendMessage(Core.prefix + "§b§oIt is now raining in your world.");
                                name.sendMessage(Core.prefix + "§9To enable swift swim, type §a§l/swiftswim enable.");
                            }
                        }
                    }
                }
            } else {
                //System.out.println("The weather has been changed in a world not listed!");
            }
        }
        if(!Core.getInstance().getConfig().getStringList("enabled-worlds").contains(e.getWorld().getName())){
            // Do nothing
        }
    }

    @EventHandler
    public void playerTeleportEvent(PlayerTeleportEvent e) throws EventException {
        Player user = e.getPlayer();
        String worldNameFrom = e.getFrom().getWorld().getName();
        String worldNameTo = e.getTo().getWorld().getName();

        if(worldNameFrom.equals(worldNameTo)){
            return;
        }

        if (Core.playerIds.contains(e.getPlayer().getUniqueId())) {
            user.sendMessage(Core.prefix + "§b§oSwift Swim has been cancelled because you teleported to a different world!");
            user.sendMessage(Core.prefix + "§9If it is raining in the world you are in, and swift swim is allowed, you can re-enable swift swim.");
            Core.playerNames.remove(e.getPlayer());
            Core.playerIds.remove(user.getUniqueId());
            PotionEffectType speed = PotionEffectType.SPEED;
            user.removePotionEffect(speed);

        }
        if(!Core.playerIds.contains(e.getPlayer().getUniqueId())){
            if(e.getTo().getWorld().hasStorm()){
                if(Core.getInstance().getConfig().getStringList("enabled-worlds").contains(e.getTo().getWorld().getName())){
                    user.sendMessage(Core.prefix + "§9The world you have teleported to has swift swim enabled and it is raining!");
                    user.sendMessage(Core.prefix + "§9Use /swiftswim enable to turn it on.");
                }
            }
        }

    }

}
