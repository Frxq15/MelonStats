package org.melonmc.melonstats.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.melonmc.melonstats.MelonStats;

public class testCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Bukkit.broadcastMessage("top kills");
        MelonStats.getInstance().getLeaderboard().displayKills();
        Bukkit.broadcastMessage("top deaths");
        MelonStats.getInstance().getLeaderboard().displayDeaths();
        Bukkit.broadcastMessage("highest streak");
        MelonStats.getInstance().getLeaderboard().displayHighestStreak();
        return true;
    }
}
