package org.melonmc.melonstats.misc;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.melonmc.melonstats.MelonStats;
import org.melonmc.melonstats.sql.PlayerData;

import java.util.Random;

public class Listeners implements Listener {

    private final MelonStats plugin;

    public Listeners(MelonStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void giveMoney(PlayerDeathEvent e) {
        if(e.getEntity().getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        Player p = e.getEntity().getKiller();
        if (e.getEntityType() != EntityType.PLAYER) {
            return;
        }
        int min = plugin.getConfig().getInt("MIN_KILL_REWARD");
        int max = plugin.getConfig().getInt("MAX_KILL_REWARD");
        Random rand = new Random();
        int amount = rand.nextInt(max - min + 1) + min;
        MelonStats.getInstance().getVaultAPI().getEconomy().depositPlayer(p, amount);
        p.sendMessage(MelonStats.formatMsg("RECEIVED_MONEY").replace("%amount%", amount+"").replace("%player%", e.getEntity().getName()));
    }
    @EventHandler
    public void deathMessage(PlayerDeathEvent e) {
        if(!(e.getEntity().getKiller() instanceof Player)) {
            return;
        }
        Player p = e.getEntity().getKiller();
        if (e.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Bukkit.broadcastMessage(MelonStats.formatMsg("DEATH_MESSAGE").replace("%player%", e.getEntity().getName()).replace("%killer%", p.getName()));
    }
    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(e.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Bukkit.broadcastMessage(MelonStats.formatMsg("DEATH_MESSAGE_2").replace("%player%", e.getEntity().getName()));
    }
}
