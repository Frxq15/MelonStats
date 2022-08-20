package org.melonmc.melonstats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.melonmc.melonstats.api.PlaceholderAPI;
import org.melonmc.melonstats.api.VaultAPI;
import org.melonmc.melonstats.commands.StatsCommand;
import org.melonmc.melonstats.commands.testCommand;
import org.melonmc.melonstats.leaderboard.LeaderboardManager;
import org.melonmc.melonstats.misc.Listeners;
import org.melonmc.melonstats.sql.PlayerData;
import org.melonmc.melonstats.sql.SQLListeners;
import org.melonmc.melonstats.sql.SQLSetterGetter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public final class MelonStats extends JavaPlugin {
    private static MelonStats instance;
    private Connection connection;
    public String host, database, username, password, table;
    public int port;
    public SQLSetterGetter sqlSetterGetter;
    public VaultAPI vaultAPI;
    public PlaceholderAPI placeholderAPI;
    public LeaderboardManager leaderboard;

    @Override
    public void onEnable() {
        instance = this;
        sqlSetterGetter = new SQLSetterGetter();
        vaultAPI = new VaultAPI();
        placeholderAPI = new PlaceholderAPI();
        Bukkit.getPluginManager().registerEvents(new SQLListeners(this), this);
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        saveDefaultConfig();
        SQLSetup();
        getSQLManager().createTable(table);
        startSavingTask();
        getVaultAPI().runSetup();
        getPlaceholderAPI().setupPapi();
        leaderboard = new LeaderboardManager(this);
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("test").setExecutor(new testCommand());

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static MelonStats getInstance() { return instance; }

    public VaultAPI getVaultAPI() { return vaultAPI; }
    public PlaceholderAPI getPlaceholderAPI() { return placeholderAPI; }

    public SQLSetterGetter getSQLManager() { return sqlSetterGetter; }
    public LeaderboardManager getLeaderboard() { return leaderboard; }

    public void SQLSetup() {
        host = getInstance().getConfig().getString("DATABASE." + "HOST");
        port = getInstance().getConfig().getInt("DATABASE." + "PORT");
        database = getInstance().getConfig().getString("DATABASE." + "DATABASE");
        username = getInstance().getConfig().getString("DATABASE." + "USERNAME");
        password = getInstance().getConfig().getString("DATABASE." + "PASSWORD");
        table = getInstance().getConfig().getString("DATABASE." + "TABLE");

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password="+ password));
                log("MySQL Connected successfully.");

            }

        }catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            log("Please setup your MySQL database in the config.yml.");
        }
    }
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public void log(String input) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"[MelonMC] "+input);
    }
    public static String formatMsg(String input) {
        return ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString(input));
    }
    public static String colourize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
    public void startSavingTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> PlayerData.getAllPlayerData().forEach((uuid, playerData) -> getSQLManager().setKills(uuid, playerData.getKills())), 20L * 60L * 5L, 20L * 60L * 5L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> PlayerData.getAllPlayerData().forEach((uuid, playerData) -> getSQLManager().setDeaths(uuid, playerData.getDeaths())), 20L * 60L * 5L, 20L * 60L * 5L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> PlayerData.getAllPlayerData().forEach((uuid, playerData) -> getSQLManager().setStreak(uuid, playerData.getStreak())), 20L * 60L * 5L, 20L * 60L * 5L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> PlayerData.getAllPlayerData().forEach((uuid, playerData) -> getSQLManager().setHighestStreak(uuid, playerData.getHighestStreak())), 20L * 60L * 5L, 20L * 60L * 5L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                getLeaderboard().updateLeaderboards();
            }
        }, 0L, 20 * 60 * 10L);
    }
}
