package org.melonmc.melonstats.sql;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.melonmc.melonstats.MelonStats;

import java.util.UUID;

public class SQLListeners implements Listener {
    private final MelonStats plugin;

    public SQLListeners(MelonStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String name = event.getName();

        plugin.getSQLManager().createPlayer(uuid, name);

        PlayerData playerData = PlayerData.getPlayerData(plugin, uuid);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSQLManager().updatePlayerName(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                UUID uuid = event.getPlayer().getUniqueId();
                PlayerData playerData = PlayerData.getPlayerData(plugin, uuid);
                playerData.uploadPlayerData(plugin);
                PlayerData.removePlayerData(uuid);
            });
    }
    @EventHandler
    public void addKill(PlayerDeathEvent e) {
        Player p = e.getEntity().getKiller();
        PlayerData playerData = PlayerData.getPlayerData(plugin, p.getUniqueId());
        if(e.getEntityType() != EntityType.PLAYER) {
            return;
        }
        int current = playerData.getStreak();
        playerData.addToStreak();
        playerData.addKill();

        if(current >= playerData.getHighestStreak()) {
            playerData.setHighestStreak(current);
        }

        Player p2 = e.getEntity();
        PlayerData playerData1 = PlayerData.getPlayerData(plugin, p2.getUniqueId());
        playerData1.addDeath();

    }
}
