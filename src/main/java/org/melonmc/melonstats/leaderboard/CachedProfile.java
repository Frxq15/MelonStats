package org.melonmc.melonstats.leaderboard;

import org.bukkit.entity.Player;
import org.melonmc.melonstats.MelonStats;

import java.util.UUID;

public class CachedProfile {
    private final MelonStats plugin;
    public Player player;
    public UUID uuid;


    public CachedProfile(MelonStats plugin, UUID uuid) {
        this.plugin = plugin;
    }
}
