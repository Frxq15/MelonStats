package org.melonmc.melonstats.leaderboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.melonmc.melonstats.MelonStats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CachedProfiles {
    private final MelonStats plugin;

    public Map<String, Integer> kills = new HashMap<>();


    public CachedProfiles(MelonStats plugin) {
        this.plugin = plugin;
    }

    public void updateKills(Map<String, Integer> refresh) {
        this.kills = refresh;
    }

    public void updateLeaderboards() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> updateKills(plugin.getSQLManager().getTopKills()));
    }
    public void displayKills() {
        AtomicInteger pos = new AtomicInteger(0);
        kills.forEach((player, amount) -> {
            pos.getAndIncrement();
            Bukkit.broadcastMessage("#"+pos.get()+" Player: "+player + " Kills: "+amount);
        });
    }
}
