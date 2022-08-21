package org.melonmc.melonstats.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.melonmc.melonstats.MelonStats;
import org.melonmc.melonstats.sql.PlayerData;

import java.text.DecimalFormat;

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
            DecimalFormat df = new DecimalFormat("#.##");
            PlayerData playerData = PlayerData.getPlayerData(plugin, p.getUniqueId());
            MelonStats.getInstance().getConfig().getStringList("STATS_MESSAGE").forEach(line -> {
                line = line.replace("%player%", p.getName()).replace("%kills%", playerData.getKills()+"")
                        .replace("%deaths%", playerData.getDeaths()+"").replace("%currentks%", playerData.getStreak()+"")
                        .replace("%kdr%", df.format(playerData.getKDR())+"")
                        .replace("%highestks%", playerData.getHighestStreak()+"")
                        .replace("%kills_position%", plugin.getLeaderboard().getKillsPositionByName(p.getName())+"")
                        .replace("%deaths_position%", plugin.getLeaderboard().getDeathsPositionByName(p.getName())+"")
                        .replace("%highest_streak_position%", plugin.getLeaderboard().getHighestStreakPositionByName(p.getName())+"");
                p.sendMessage(MelonStats.colourize(line));
            });
            return true;
        }
        if(args.length == 1) {
            if(!p.hasPermission("melonstats.stats.others")) {
                p.sendMessage(MelonStats.formatMsg("NO_PERMISSION"));
                return true;
            }
            String target = args[0];
            if(Bukkit.getPlayer(target) == null) {
                p.sendMessage(MelonStats.formatMsg("PLAYER_NOT_FOUND"));
                return true;
            }
            DecimalFormat df = new DecimalFormat("#.##");
            PlayerData playerData = PlayerData.getPlayerData(plugin, Bukkit.getPlayer(target).getUniqueId());
            MelonStats.getInstance().getConfig().getStringList("STATS_MESSAGE").forEach(line -> {
                line = line.replace("%player%", Bukkit.getPlayer(target).getName()).replace("%kills%", playerData.getKills()+"")
                        .replace("%deaths%", playerData.getDeaths()+"").replace("%currentks%", playerData.getStreak()+"")
                        .replace("%kdr%", df.format(playerData.getKDR())+"").replace("%highestks%", playerData.getHighestStreak()+"");
                p.sendMessage(MelonStats.colourize(line));
            });
            return true;
        }
        p.sendMessage(MelonStats.colourize("&cUsage: /stats <player>"));
        return true;
    }
}
