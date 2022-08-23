package org.melonmc.melonstats.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.melonmc.melonstats.MelonStats;
import org.melonmc.melonstats.gui.menus.leaderboardsMenu;

public class leaderboardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            MelonStats.getInstance().log("This command cannot be executed from console.");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("melonstats.leaderboard")) {
            p.sendMessage(MelonStats.formatMsg("NO_PERMISSION"));
            return true;
        }
        new leaderboardsMenu(MelonStats.getInstance()).open((Player) sender);
        return true;
    }
}
