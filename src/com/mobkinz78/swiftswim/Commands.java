package com.mobkinz78.swiftswim;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * Created by Mark on 7/27/2016.
 */
public class Commands implements CommandExecutor {

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
        if(!(sender instanceof Player)){
            sender.sendMessage("This command can only be executed by players!");
        }
        if (cmd.getName().equalsIgnoreCase("swiftswim") && w.hasStorm() && w.getName().equalsIgnoreCase("towny")) {
            if (args.length == 0) {
                sender.sendMessage(Core.prefix + "§cNo argument given! Accepted arguments: ");
                sender.sendMessage(Core.prefix + "§9Enable §r§lor §r§9disable");

                return true;
            }
            if (args[0].equalsIgnoreCase("disable") && w.hasStorm() && w.getName().equalsIgnoreCase("towny")) {
                sender.sendMessage(Core.prefix + "§9§lSwift Swim has been §c§ldisabled.");
                sender.sendMessage(Core.prefix + "§r§9§oUse /swiftswim enable to enable while it is raining.");
                Core.playerIds.remove(senderUUID);
                Core.playerNames.remove(sender.getName());

                Player user = (Player) sender;
                PotionEffectType speed = PotionEffectType.SPEED;
                user.removePotionEffect(speed);

                return true;
            }
            if (args[0].equalsIgnoreCase("enable") && w.hasStorm() && w.getName().equalsIgnoreCase("towny")) {
                sender.sendMessage(Core.prefix + "§9§lSwift Swim has been §a§lenabled");
                sender.sendMessage(Core.prefix + "§r§9§oUse /swiftswim disable to disable while it is raining.");
                Core.playerIds.add(senderUUID);
                Core.playerNames.add(player);

                Player user = (Player) sender;
                PotionEffectType speed = PotionEffectType.SPEED;
                user.addPotionEffect(speed.createEffect(99999, 1));
                // Soon to come... add a particle effect!

                return true;
            }
            if (cmd.getName().equalsIgnoreCase("swiftswim") && w.hasStorm() && !(w.getName().equalsIgnoreCase("towny"))) {
                sender.sendMessage(Core.prefix + "§9§oSwiftSwim can not be enabled in this world!");
            } else if (!(args[0].equalsIgnoreCase("no")) || !(args[0].equalsIgnoreCase("yes"))) {
                sender.sendMessage(Core.prefix + "§cArgument not recognized. Accepted arguments: ");
                sender.sendMessage(Core.prefix + "§9Enable §r§oor §r§9disable");

                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("swiftswim") && (!w.hasStorm())) { //Also may need something that says swiftswim is not enabled in that world
            sender.sendMessage(Core.prefix + "§9§oIt is not raining in your world!");

            return true;
        } else if (cmd.getName().equalsIgnoreCase("swiftswim") && (!w.getName().equalsIgnoreCase("towny"))){
            sender.sendMessage(Core.prefix + "§cSwift Swim is not available for this world.");
            sender.sendMessage(Core.prefix + "§cAvailable Worlds: §a[Towny]");

            return true;
        }

        return true;

    }

}
