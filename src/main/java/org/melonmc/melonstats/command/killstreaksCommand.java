package org.melonmc.melonstats.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.melonmc.melonstats.MelonStats;
import org.melonmc.melonstats.sql.PlayerData;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class killstreaksCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!s.hasPermission("melonstats.killstreaks")) {
            s.sendMessage(MelonStats.formatMsg("NO_PERMISSION"));
            return true;
        }
        HashMap m = new HashMap<String, Integer>();
        Map<UUID, PlayerData> players = new HashMap<>();
        players = PlayerData.getAllPlayerData();
        players.forEach((uuid, pd) -> {
            if(Bukkit.getPlayer(pd.getUUID()).isOnline()) {
                m.put(Bukkit.getPlayer(pd.getUUID()).getName(), pd.getStreak());
            }
        });
        Map<String, Integer> sorted = new HashMap<>();
        sorted = sortByValue(m);
        AtomicInteger i = new AtomicInteger(10);
        MelonStats.getInstance().getConfig().getStringList("KILLSTREAKS_HEADER").forEach(line -> {
            s.sendMessage(MelonStats.colourize(line));

        });
        List<String> used = new ArrayList<String>();
        AtomicInteger totalks = new AtomicInteger(0);
        while(i.get() > 0) {
            sorted.forEach((name, streak) -> {
                totalks.addAndGet(streak);
                if(!used.contains(name)) {
                    if (streak > 0) {
                        used.add(name);
                        s.sendMessage(MelonStats.formatMsg("KILLSTREAKS_FORMAT").replace("%player_name%", name)
                                .replace("%streak%", streak + ""));
                    }
                }
            });
            i.getAndDecrement();
        }
            if(totalks.get() < 1) {
                s.sendMessage(MelonStats.formatMsg("NO_KILLSTREAKS_FOUND"));
            }
            MelonStats.getInstance().getConfig().getStringList("KILLSTREAKS_FOOTER").forEach(line -> {
                s.sendMessage(MelonStats.colourize(line));

        });
        return true;
    }
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> unsortedMap)
    {
        HashMap<String, Integer> sortedMap = unsortedMap.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> { throw new AssertionError(); },
                        LinkedHashMap::new
                ));

        return sortedMap;
    }
}
