package com.mobkinz78.swiftswim;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffectType;
import com.mobkinz78.swiftswim.Core;

import java.util.UUID;

/**
 * Created by Mark on 7/27/2016.
 * This file will contain the events, teleport and weather change, as to not clutter up the main class.
 */
public class Events implements Listener {

    @EventHandler
    public void weatherChange(WeatherChangeEvent e) { // This throw is unchecked. There is an error that spams console???
        if (e.getWorld().getName().equalsIgnoreCase("towny")) {
            if (!e.toWeatherState()) { //Turns to clear
                //System.out.println(prefix + "Weather changed to clear!");
                for (Player name : Core.playerNames) { // Needs to be the rain event in towny!!! (This entire event, actually)
                    Core.playerNames.remove(name); //remove players from arraylist
                    Core.playerIds.remove(name.getUniqueId());
                    name.sendMessage(Core.prefix + "§cSwift Swim has been §4§ldisabled, §cbecause the rain has stopped!");

                    //Clear effects from each person
                    PotionEffectType speed = PotionEffectType.SPEED;
                    name.removePotionEffect(speed);

                    for (UUID id : Core.playerIds) {
                        Core.playerIds.remove(id);
                    }
                }
            }
            if (e.toWeatherState()) { //Turns to rain
                // System.out.println("Weather changed to rain!");
                // Broadcast players to world (worlds in config file? fun challenge!) that swift swim can be enabled
                for (Player name : Bukkit.getServer().getOnlinePlayers()) {
                    World world = name.getWorld();
                    if (world.getName().equalsIgnoreCase("towny")) {
                        name.sendMessage(Core.prefix + "§b§oIt is now raining in your world.");
                        name.sendMessage(Core.prefix + "§9To enable swift swim, type §a§l/swiftswim enable.");
                    } else {
                        // Nothing required
                    }
                }
            }
        } else {
            //System.out.println("The weather has been changed in a world not listed!");
        }
    }

    @EventHandler
    public void playerTeleportEvent(PlayerTeleportEvent e) {
        // System.out.println("Teleport event triggered by: " + e.getPlayer().getName());

        Player user = e.getPlayer();

        if (Core.playerIds.contains(e.getPlayer().getUniqueId())) {
            user.sendMessage(Core.prefix + "§b§oSwift Swim has been cancelled because you teleported!");
            user.sendMessage(Core.prefix + "§9If you are still in a world with swiftswim, you can re-enable it."); // List worlds in the arraylist if using config
            Core.playerNames.remove(e.getPlayer());
            Core.playerIds.remove(user.getUniqueId());
            //user.sendMessage(prefix + user.getName() + " was successfully removed from arraylist");
            PotionEffectType speed = PotionEffectType.SPEED;
            user.removePotionEffect(speed);

        } else {
            //user.sendMessage("You were teleported, but not from within the arraylist!");
        }

    }

}
