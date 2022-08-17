package org.melonmc.melonstats.api;

import org.bukkit.Bukkit;
import org.melonmc.melonstats.MelonStats;

public class PlaceholderAPI {
    public void setupPapi() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            MelonStats.getInstance().log("PlaceholderAPI not found, placeholders will not be enabled.");
            return;
        }
        new Placeholders(MelonStats.getInstance()).register();
        MelonStats.getInstance().log("Hooked into PlaceholderAPI successfully");
    }
}
