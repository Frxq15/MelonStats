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

    public LinkedHashMap<String, Integer> deaths = new LinkedHashMap<>();
    public LinkedHashMap<String, Integer> highest_streak = new LinkedHashMap<>();


    public LeaderboardManager(MelonStats plugin) {
        this.plugin = plugin;
    }

    public void updateKills(LinkedHashMap<String, Integer> refresh) {
        this.kills = refresh;
    }
    public void updateDeaths(LinkedHashMap<String, Integer> refresh) {
        this.deaths = refresh;
    }

    public void updateHighestStreak(LinkedHashMap<String, Integer> refresh) {
        this.highest_streak = refresh;
    }

    public void updateLeaderboards() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> updateKills(plugin.getSQLManager().getTopKills()));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> updateDeaths(plugin.getSQLManager().getTopDeaths()));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> updateHighestStreak(plugin.getSQLManager().getTopHighestStreak()));
    }
    public void displayKills() {
        AtomicInteger pos = new AtomicInteger(0);
        kills.forEach((player, amount) -> {
            pos.getAndIncrement();
            Bukkit.broadcastMessage("#"+pos.get()+" Player: "+player + " Kills: "+amount);
        });
    }
    public void displayDeaths() {
        AtomicInteger pos = new AtomicInteger(0);
        deaths.forEach((player, amount) -> {
            pos.getAndIncrement();
            Bukkit.broadcastMessage("#"+pos.get()+" Player: "+player + " Deaths: "+amount);
        });
    }
    public void displayHighestStreak() {
        AtomicInteger pos = new AtomicInteger(0);
        highest_streak.forEach((player, amount) -> {
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

    public void displayDeathsByPosition(int pos) {
        int total = deaths.size() -1;
        if(pos > total) {
            return;
        }
        AtomicInteger position = new AtomicInteger(0);
        deaths.forEach((player, amount) -> {
            position.getAndIncrement();
            if(position.get() == pos) {
                Bukkit.broadcastMessage("#"+position.get()+" Player: "+player + " Kills: "+amount);
            }
        });
    }
    public Integer getDeathsByPosition(int pos) {
        int total = deaths.size() -1;
        AtomicInteger am = new AtomicInteger(0);
        if(pos > total) {
            return 0;
        }
        AtomicInteger position = new AtomicInteger(0);
        deaths.forEach((player, amount) -> {
            position.getAndIncrement();
            if(position.get() == pos) {
                am.set(amount);
            }
        });
        return am.get();
    }
    public String getNameByDeathsPosition(int pos) {
        int total = deaths.size() -1;
        AtomicReference name = new AtomicReference("None");
        if(pos > total) {
            return "None";
        }
        AtomicInteger position = new AtomicInteger(0);
        deaths.forEach((player, amount) -> {
            position.getAndIncrement();
            if(position.get() == pos) {
                name.set(player);
            }
        });
        return (String) name.get();
    }
    public Integer getDeathsPositionByName(String name) {
        int total = deaths.size() -1;
        AtomicInteger position = new AtomicInteger(0);
        AtomicInteger pos = new AtomicInteger(0);
        deaths.forEach((player, amount) -> {
            position.getAndIncrement();
            if(player.equalsIgnoreCase(name)) {
                pos.set(position.get());
            }
        });
        return pos.get();
    }
    public void displayHighestStreakByPosition(int pos) {
        int total = highest_streak.size() -1;
        if(pos > total) {
            return;
        }
        AtomicInteger position = new AtomicInteger(0);
        highest_streak.forEach((player, amount) -> {
            position.getAndIncrement();
            if(position.get() == pos) {
                Bukkit.broadcastMessage("#"+position.get()+" Player: "+player + " Highest Streak: "+amount);
            }
        });
    }
    public Integer getHighestStreakByPosition(int pos) {
        int total = highest_streak.size() -1;
        AtomicInteger am = new AtomicInteger(0);
        if(pos > total) {
            return 0;
        }
        AtomicInteger position = new AtomicInteger(0);
        highest_streak.forEach((player, amount) -> {
            position.getAndIncrement();
            if(position.get() == pos) {
                am.set(amount);
            }
        });
        return am.get();
    }
    public String getNameByHighestStreakPosition(int pos) {
        int total = kills.size() -1;
        AtomicReference name = new AtomicReference("None");
        if(pos > total) {
            return "None";
        }
        AtomicInteger position = new AtomicInteger(0);
        highest_streak.forEach((player, amount) -> {
            position.getAndIncrement();
            if(position.get() == pos) {
                name.set(player);
            }
        });
        return (String) name.get();
    }
    public Integer getHighestStreakPositionByName(String name) {
        int total = highest_streak.size() -1;
        AtomicInteger position = new AtomicInteger(0);
        AtomicInteger pos = new AtomicInteger(0);
        highest_streak.forEach((player, amount) -> {
            position.getAndIncrement();
            if(player.equalsIgnoreCase(name)) {
                pos.set(position.get());
            }
        });
        return pos.get();
    }
}
