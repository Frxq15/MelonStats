package org.melonmc.melonstats.sql;

import org.bukkit.Bukkit;
import org.melonmc.melonstats.MelonStats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private final static Map<UUID, PlayerData> players = new HashMap<>();

    private final UUID uuid;
    private int streak = 0;
    private int highest_streak = 0;
    private int deaths = 0;
    private int kills = 0;
    private double kdr = 0;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        players.put(uuid, this);
    }
    public UUID getUUID() { return uuid; }
    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }
    public int getStreak() { return streak; }
    public int getHighestStreak() { return highest_streak; }
    public double getKDR() { return kdr; }

    public void setKills(int kills) {
        this.kills = kills;
        updateKDR();
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;
        updateKDR();
    }
    public void setStreak(int streak) { this.streak = streak; }
    public void setHighestStreak(int streak) { this.highest_streak = highest_streak; }
    public void updateKDR() { this.kdr = (getKills()/getDeaths()); }

    public void addKill() {
        setKills((getKills()+1));
        updateKDR();
     }
    public void addDeath() {
        setDeaths((getDeaths()+1));
        updateKDR();
    }
    public void addToStreak() {
        setStreak((getStreak()+1));
    }

    public static void removePlayerData(UUID uuid) { players.remove(uuid); }

    public static PlayerData getPlayerData(MelonStats plugin, UUID uuid) {
        if (!players.containsKey(uuid)) {
            PlayerData playerData = new PlayerData(uuid);
            playerData.setKills(plugin.getSQLManager().getKills(uuid));
            playerData.setDeaths(plugin.getSQLManager().getDeaths(uuid));
            playerData.setStreak(plugin.getSQLManager().getStreak(uuid));
            playerData.setHighestStreak(plugin.getSQLManager().getHighestStreak(uuid));
            playerData.updateKDR();
        }
        return players.get(uuid);
    }

    public void uploadPlayerData(MelonStats plugin) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSQLManager().setKills(uuid, kills));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSQLManager().setDeaths(uuid, deaths));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSQLManager().setStreak(uuid, streak));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSQLManager().setHighestStreak(uuid, highest_streak));
    }

    public static Map<UUID, PlayerData> getAllPlayerData() {
        return players;
    }
}
