package com.mobkinz78.swiftswim;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Mark on 3/27/2016.
 * I am aware this is all in one class and really shouldn't be. There are some console errors that I think just need to
 * be surrounded by try and catch...
 */
public class Core extends JavaPlugin implements Listener {

    private String prefix = "§8(§b§lSwift Swim§8)§r ";

    @Override
    public void onEnable() {
        getLogger().info(prefix + "Swift Swim has been successfully enabled!");
        getServer().getPluginManager().registerEvents(this, this);
        //this.setExecutor(new SSCommand());
        //§
    }

    @Override
    public void onDisable() {
        getLogger().info(prefix + "Swift Swim has been successfully disabled!");
    }

    public ArrayList<UUID> playerIds = new ArrayList<UUID>(); // Not really necessary, just stores UUIDs. Plugin resets on restart anyway. Fun to maybe store names in config?
    public ArrayList<Player> playerNames = new ArrayList<Player>(); // Players in this arraylist are given speed 2 for swift swim
    public ArrayList<World> worlds = new ArrayList<World>(); // Use this arraylist to add worlds from a config file.

    World newfactions;

    @EventHandler
    public void weatherChange(WeatherChangeEvent e) { // This throw is unchecked. There is an error that spams console???
        if (e.getWorld().getName().equalsIgnoreCase("towny")) {
            if (!e.toWeatherState()) { //Turns to clear
                //System.out.println(prefix + "Weather changed to clear!");
                for (Player name : playerNames) { // Needs to be the rain event in towny!!! (This entire event, actually)
                    playerNames.remove(name); //remove players from arraylist
                    playerIds.remove(name.getUniqueId());
                    name.sendMessage(prefix + "§cSwift Swim has been §4§ldisabled, §cbecause the rain has stopped!");

                    //Clear effects from each person
                    PotionEffectType speed = PotionEffectType.SPEED;
                    name.removePotionEffect(speed);

                    for (UUID id : playerIds) {
                        playerIds.remove(id);
                    }
                }
            }
            if (e.toWeatherState()) { //Turns to rain
                // System.out.println("Weather changed to rain!");
                // Broadcast players to world (worlds in config file? fun challenge!) that swift swim can be enabled
                for (Player name : getServer().getOnlinePlayers()) {
                    World world = name.getWorld();
                    if (world.getName().equalsIgnoreCase("towny")) {
                        name.sendMessage(prefix + "§b§oIt is now raining in your world.");
                        name.sendMessage(prefix + "§9To enable swift swim, type §a§l/swiftswim enable.");
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

        if (playerIds.contains(e.getPlayer().getUniqueId())) {
            user.sendMessage(prefix + "§b§oSwift Swim has been cancelled because you teleported!");
            user.sendMessage(prefix + "§9If you are still in a world with swiftswim, you can re-enable it."); // List worlds in the arraylist if using config
            playerNames.remove(e.getPlayer());
            playerIds.remove(user.getUniqueId());
            //user.sendMessage(prefix + user.getName() + " was successfully removed from arraylist");
            PotionEffectType speed = PotionEffectType.SPEED;
            user.removePotionEffect(speed);

        } else {
            //user.sendMessage("You were teleported, but not from within the arraylist!");
        }

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        UUID senderUUID = ((Player) sender).getUniqueId();
        World w = player.getWorld();

        /*if (cmd.getName().equalsIgnoreCase("checklist")) {
            System.out.println("UUIDS: " + playerIds);
            System.out.println("Names: " + playerNames);

            sender.sendMessage("UUIDS: " + playerIds);
            sender.sendMessage("Names: " + playerNames);

            return true;
        }*/
        //if (cmd.getName().equalsIgnoreCase("swiftswim")) sender.sendMessage("SSCommand was executed");

        if (cmd.getName().equalsIgnoreCase("swiftswim") && w.hasStorm() && w.getName().equalsIgnoreCase("towny")) {
            if (args.length == 0) {
                sender.sendMessage(prefix + "§cNo argument given! Accepted arguments: ");
                sender.sendMessage(prefix + "§9Enable §r§lor §r§9disable");

                return true;
            }
            if (args[0].equalsIgnoreCase("disable") && w.hasStorm() && w.getName().equalsIgnoreCase("towny")) {
                sender.sendMessage(prefix + "§9§lSwift Swim has been §c§ldisabled.");
                sender.sendMessage(prefix + "§r§9§oUse /swiftswim enable to enable while it is raining.");
                playerIds.remove(senderUUID);
                playerNames.remove(sender.getName());

                Player user = (Player) sender;
                PotionEffectType speed = PotionEffectType.SPEED;
                user.removePotionEffect(speed);

                return true;
            }
            if (args[0].equalsIgnoreCase("enable") && w.hasStorm() && w.getName().equalsIgnoreCase("towny")) {
                sender.sendMessage(prefix + "§9§lSwift Swim has been §a§lenabled");
                sender.sendMessage(prefix + "§r§9§oUse /swiftswim disable to disable while it is raining.");
                playerIds.add(senderUUID);
                playerNames.add(player);

                Player user = (Player) sender;
                PotionEffectType speed = PotionEffectType.SPEED;
                user.addPotionEffect(speed.createEffect(99999, 1));
                // Soon to come... add a particle effect!

                return true;
            }
            if (cmd.getName().equalsIgnoreCase("swiftswim") && w.hasStorm() && !(w.getName().equalsIgnoreCase("towny"))) {
                sender.sendMessage(prefix + "§9§oSwiftSwim can not be enabled in this world!");
            } else if (!(args[0].equalsIgnoreCase("no")) || !(args[0].equalsIgnoreCase("yes"))) {
                sender.sendMessage(prefix + "§cArgument not recognized. Accepted arguments: ");
                sender.sendMessage(prefix + "§9Enable §r§oor §r§9disable");

                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("swiftswim") && (!w.hasStorm())) { //Also may need something that says swiftswim is not enabled in that world
            sender.sendMessage(prefix + "§9§oIt is not raining in your world!");

            return true;
        } else if (cmd.getName().equalsIgnoreCase("swiftswim") && (!w.getName().equalsIgnoreCase("towny"))){
            sender.sendMessage(prefix + "§cSwift Swim is not available for this world.");
            sender.sendMessage(prefix + "§cAvailable Worlds: §a[Towny]");

            return true;
        }

        return true;

    }
}

