package org.melonmc.melonstats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.melonmc.melonstats.MelonStats;

public class StatsCommand implements CommandExecutor {
    private final MelonStats plugin = MelonStats.getInstance();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            plugin.log("This command cannot be executed from console.");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("melonstats.stats")) {
            p.sendMessage(MelonStats.formatMsg("NO_PERMISSION"));
            return true;
        }
        if(args.length == 0) {
            //insert stats stringlist here
            return true;
        }
        if(args.length == 1) {
            if(!p.hasPermission("melonstats.stats.others")) {
                p.sendMessage(MelonStats.formatMsg("NO_PERMISSION"));
                return true;
            }
            return true;
        }
        p.sendMessage(MelonStats.colourize("&cUsage: /stats <player>"));
        return true;
    }
}
