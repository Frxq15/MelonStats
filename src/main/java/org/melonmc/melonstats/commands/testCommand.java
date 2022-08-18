package org.melonmc.melonstats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.melonmc.melonstats.MelonStats;

public class testCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        MelonStats.getInstance().getCachedProfiles().displayKills();
        return true;
    }
}
