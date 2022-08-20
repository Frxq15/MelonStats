package org.melonmc.melonstats.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.melonmc.melonstats.MelonStats;
import org.melonmc.melonstats.sql.PlayerData;

import java.text.DecimalFormat;

public class Placeholders extends PlaceholderExpansion {
    private MelonStats plugin;

    public Placeholders(MelonStats plugin) {
        this.plugin = plugin;
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return this.plugin.getDescription().getAuthors().toString();
    }

    public String getIdentifier() {
        return "melonstats";
    }

    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(final Player player, final String identifier) {
        if (player == null) {
            return "Invalid Player";
        }
        PlayerData playerData = PlayerData.getPlayerData(plugin, player.getUniqueId());

        switch(identifier.toLowerCase()) {
            case "kills":
                return String.valueOf(playerData.getKills());

            case "deaths":
                return String.valueOf(playerData.getDeaths());

            case "kdr":
                DecimalFormat df = new DecimalFormat("#.##");
                return df.format(playerData.getKDR())+"";

            case "streak":
                return String.valueOf(playerData.getStreak());

            case "highest_streak":
                return String.valueOf(playerData.getHighestStreak());

            case "leaderboard_kills_position":
                return String.valueOf(plugin.getLeaderboard().getKillsPositionByName(player.getName()));
        }
        return null;
    }
}
