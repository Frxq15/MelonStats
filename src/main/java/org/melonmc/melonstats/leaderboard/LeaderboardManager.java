package org.melonmc.melonstats.leaderboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.melonmc.melonstats.MelonStats;
import org.melonmc.melonstats.sql.PlayerData;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LeaderboardManager {
    private final MelonStats plugin;

    public LinkedHashMap<String, Integer> kills = new LinkedHashMap<>();


    public LeaderboardManager(MelonStats plugin) {
        this.plugin = plugin;
    }

    public void updateKills(LinkedHashMap<String, Integer> refresh) {
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
    public void displayKillsByPosition(int pos) {
        int total = kills.size() -1;
        if(pos > total) {
            return;
        }
        AtomicInteger position = new AtomicInteger(0);
        kills.forEach((player, amount) -> {
            position.getAndIncrement();
            if(position.get() == pos) {
                Bukkit.broadcastMessage("#"+position.get()+" Player: "+player + " Kills: "+amount);
            }
        });
    }
    public Integer getKillsByPosition(int pos) {
        int total = kills.size() -1;
        AtomicInteger am = new AtomicInteger(0);
        if(pos > total) {
            return 0;
        }
        AtomicInteger position = new AtomicInteger(0);
        kills.forEach((player, amount) -> {
            position.getAndIncrement();
            if(position.get() == pos) {
                am.set(amount);
            }
        });
        return am.get();
    }
    public String getNameByKillsPosition(int pos) {
        int total = kills.size() -1;
        AtomicReference name = new AtomicReference("None");
        if(pos > total) {
            return "None";
        }
        AtomicInteger position = new AtomicInteger(0);
        kills.forEach((player, amount) -> {
            position.getAndIncrement();
            if(position.get() == pos) {
                name.set(player);
            }
        });
        return (String) name.get();
    }
    public Integer getKillsPositionByName(String name) {
        int total = kills.size() -1;
        AtomicInteger position = new AtomicInteger(0);
        AtomicInteger pos = new AtomicInteger(0);
        kills.forEach((player, amount) -> {
            position.getAndIncrement();
            if(player.equalsIgnoreCase(name)) {
                pos.set(position.get());
            }
        });
        return pos.get();
    }
}
