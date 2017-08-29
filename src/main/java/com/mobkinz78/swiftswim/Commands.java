package com.mobkinz78.swiftswim;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mark on 7/27/2016.
 */
public class Commands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players!");
            return true;
        }
        Player player = (Player) sender;
        UUID senderUUID = ((Player) sender).getUniqueId();
        World w = player.getWorld();
        List<String> enabledWorlds = Core.getInstance().getConfig().getStringList("enabled-worlds");

            if(cmd.getName().equalsIgnoreCase("swiftswim")){
                if(player.hasPermission("swiftswim.use") || player.isOp()) {
                    if (args.length == 0) {
                        sender.sendMessage(Core.prefix + "§cNo argument given! Accepted arguments: ");
                        sender.sendMessage(Core.prefix + "§9Enable §r§lor §r§9disable");
                        if(player.hasPermission("swiftswim.addworld") || player.isOp()){
                            sender.sendMessage(Core.prefix + "§9To add a world for enabling Swift Swim, use §b/swiftswim addworld <worldname>");
                        }
                        return true;
                    }
                    if (!enabledWorlds.contains(w.getName())) {
                        player.sendMessage(Core.prefix + "§cSorry, swift swim is not enabled in this world!");
                        player.sendMessage(Core.prefix + "§cAvailable Worlds: §9" + Core.getInstance().getConfig().getStringList("enabled-worlds").toString());
                        return true;
                    }
                    if (enabledWorlds.contains(w.getName())) {
                        if (!w.hasStorm()) {
                            player.sendMessage(Core.prefix + "It is not raining in this world!");
                            return true;
                        }
                        if (w.hasStorm()) {
                            if (args[0].equalsIgnoreCase("enable")) {
                                sender.sendMessage(Core.prefix + "§9Swift Swim has been §a§lenabled");
                                sender.sendMessage(Core.prefix + "§r§9§oUse /swiftswim disable to disable while it is raining.");
                                Core.playerIds.add(senderUUID);
                                Core.playerNames.add(player);
                                Player user = (Player) sender;
                                PotionEffectType speed = PotionEffectType.SPEED;
                                user.addPotionEffect(speed.createEffect(99999, 1));
                                // Soon to come... add a particle effect!

                                return true;
                            }
                            if (args[0].equalsIgnoreCase("disable")) {
                                sender.sendMessage(Core.prefix + "§9Swift Swim has been §c§ldisabled.");
                                sender.sendMessage(Core.prefix + "§r§9§oUse /swiftswim enable to enable while it is raining.");
                                Core.playerIds.remove(senderUUID);
                                Core.playerNames.remove(sender.getName());

                                Player user = (Player) sender;
                                PotionEffectType speed = PotionEffectType.SPEED;
                                user.removePotionEffect(speed);

                                return true;
                            }
                        }
                    }
                }
                if(!player.hasPermission("swiftswim.use") && !player.isOp()){
                    player.sendMessage(Core.prefix + "§9Sorry, you are not able to use swift swim!");
                    player.sendMessage(Core.prefix + "§9Required permission: §cswiftswim.use");
                    return true;
                }
                if(player.hasPermission("swiftswim.addworld") || player.isOp()){ /* /swiftswim addworld <worldname> */
                    if(args[0].equalsIgnoreCase("addworld")){
                        if(args.length < 2){
                            player.sendMessage(Core.prefix + "§9Please provide a worldname.");
                            return true;
                        }
                        if(Core.getInstance().getConfig().getStringList("enabled-worlds").contains(args[1])){
                            player.sendMessage("§9That world already has swift swim enabled!");
                            return true;
                        }
                        List<String> worlds = Core.getInstance().getConfig().getStringList("enabled-worlds");
                        worlds.add(args[1]);
                        Core.getInstance().getConfig().set("enabled-worlds", worlds);
                        player.sendMessage(Core.prefix + "§9Successfully added " + args[1] + " to swift swim's enabled worlds!");
                        Core.getInstance().saveConfig();
                        Core.getInstance().reloadConfig();
                        return true;
                    }
                    return true;
                }
                if(!player.hasPermission("swiftswim.addworld") && !player.isOp()){
                    player.sendMessage(Core.prefix + "§cSorry, you don't have access to this command!");
                    player.sendMessage(Core.prefix + "§bRequired permission: §bswiftswim.addworld");
                    return true;
                }
            }

        return true;

    }

}
