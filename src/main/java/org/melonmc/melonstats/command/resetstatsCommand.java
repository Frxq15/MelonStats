package org.melonmc.melonstats.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.melonmc.melonstats.MelonStats;

import java.util.ArrayList;
import java.util.List;

public class resetstatsCommand implements CommandExecutor, Listener {
    boolean reset = false;
    List<String> confirming = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!s.hasPermission("melonstats.resetstats")) {
            s.sendMessage(MelonStats.formatMsg("NO_PERMISSION"));
            return true;
        }
        if(!MelonStats.getInstance().getConfig().getStringList("ALLOWED_RESET_PLAYERS").contains(s.getName())) {
            s.sendMessage(MelonStats.colourize("&cYour account is not authorized to use this command."));
            return true;
        }
        if(args.length == 0) {
            if(confirming.contains(s.getName())) {
                s.sendMessage(MelonStats.formatMsg("RESET_ANSWER_NEEDED"));
                return true;
            }
            confirming.add(s.getName());
            runConfirm(s.getName(), 60);
            return true;
        }
        if(args.length == 1) {
            String answer = args[0];

            if(!confirming.contains(s.getName())) {
                s.sendMessage(MelonStats.colourize("&cUsage: /resetstats"));
                return true;
            }

            switch (answer.toLowerCase()) {
                case "confirm":
                    s.sendMessage(MelonStats.formatMsg("RESET_CONFIRMED"));
                    Bukkit.getServer().setWhitelist(true);
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.kickPlayer("Stats reset in progress.");
                    });
                    reset = true;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(MelonStats.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            MelonStats.getInstance().getSQLManager().resetData();
                        }
                    }, 20 * 20L);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(MelonStats.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.getServer().setWhitelist(false);
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
                        }
                    }, 40 * 20L);
                    return true;

                case "deny":
                    s.sendMessage(MelonStats.formatMsg("RESET_DENIED"));
                    confirming.remove(s.getName());
                    return true;
            }
        }
        s.sendMessage(MelonStats.colourize("&cUsage: /resetstats <confirm|deny>"));
        return true;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onJoin(PlayerLoginEvent e) {
        if(isActive()) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "A stats reset is currently in progress");
        }
    }
    public boolean isActive() {
        return reset;
    }
    public void runConfirm(String p, Integer time) {
        final int[] count = {time};
        Player player = Bukkit.getPlayer(p);
        player.sendMessage(MelonStats.formatMsg("RESET_ANSWER_NEEDED"));
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (count[0] == 0) {
                    // Starting
                    confirming.remove(p);
                    player.sendMessage(MelonStats.formatMsg("RESET_CONFIRM_FAILED"));
                    cancel();

                } else {
                    count[0]--;
                }
            }
        }.runTaskTimer(MelonStats.getInstance(), 20L, 20L);
    }
}
