package org.melonmc.melonstats.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.melonmc.melonstats.MelonStats;

public class testCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        MelonStats.getInstance().getLeaderboard().displayKills();
        Bukkit.broadcastMessage("num 3");
        MelonStats.getInstance().getLeaderboard().displayKillsByPosition(3);
        return true;
    }
}
