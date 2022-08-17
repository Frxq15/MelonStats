package org.melonmc.melonstats.api;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.melonmc.melonstats.MelonStats;

public class VaultAPI {
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;


    public Economy getEconomy() {
        return econ;
    }
    public Permission getPermissions() {
        return perms;
    }
    public Chat getChat() {
        return chat;
    }
    private boolean setupEconomy() {
        if (MelonStats.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = MelonStats.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = MelonStats.getInstance().getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = MelonStats.getInstance().getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    public void runSetup() {
        if (!setupEconomy()) {
            MelonStats.getInstance().log("Plugin disabled due to no Vault dependency found.");
            MelonStats.getInstance().getServer().getPluginManager().disablePlugin(MelonStats.getInstance());
            return;
        }
        setupPermissions();
        setupChat();
    }
}
