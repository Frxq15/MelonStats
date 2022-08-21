package org.melonmc.melonstats.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.melonmc.melonstats.MelonStats;
import org.melonmc.melonstats.leaderboard.LeaderboardManager;

import java.util.concurrent.atomic.AtomicInteger;

public class topKillsCommand implements CommandExecutor {
    private MelonStats plugin = MelonStats.getInstance();
    private LeaderboardManager leaderboard = plugin.getLeaderboard();
    @Override
    public boolean onCommand(@NotNull CommandSender p, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!p.hasPermission("melonstats.topkills")) {
            p.sendMessage(MelonStats.formatMsg("NO_PERMISSION"));
            return true;
        }
        plugin.getConfig().getStringList("TOP_KILLS").forEach(line -> {
            for (int i = 1; i < 15; i++) {
                line = line.replace("%topkills_name_"+i+"%", leaderboard.getNameByKillsPosition(i));
                line = line.replace("%topkills_kills_"+i+"%", leaderboard.getKillsByPosition(i)+"");
            }
            p.sendMessage(MelonStats.colourize(line));
        });
        return true;
    }
}
