package org.melonmc.melonstats.sql;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.melonmc.melonstats.MelonStats;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.UUID;

public class SQLSetterGetter {
    static final MelonStats plugin = MelonStats.getInstance();
    public static boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void createPlayer(final UUID uuid, String name) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            if (!playerExists(uuid)) {
                PreparedStatement insert = plugin.getConnection()
                        .prepareStatement("INSERT INTO " + plugin.table + "(uuid,player,kills,deaths,streak,highest_streak) VALUES (?,?,?,?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, name);
                insert.setInt(3, 0);
                insert.setInt(4, 0);
                insert.setInt(5, 0);
                insert.setInt(6, 0);
                insert.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerName(Player player) {
        try {
            PreparedStatement selectPlayer = plugin.getConnection().prepareStatement("SELECT * FROM `" + plugin.table + "` WHERE uuid = ?;");
            selectPlayer.setString(1, player.getUniqueId().toString());
            ResultSet playerResult = selectPlayer.executeQuery();

            if (playerResult.next() && !playerResult.getString("player").equals(player.getName())) {
                PreparedStatement updateName = plugin.getConnection().prepareStatement("UPDATE `"+plugin.table + "` SET player = ? WHERE uuid = ?;");
                updateName.setString(1, player.getName());
                updateName.setString(2, player.getUniqueId().toString());
                updateName.executeUpdate();
            }

            playerResult.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(String table) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin.getInstance(), () -> {
            try {
                PreparedStatement statement = plugin.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + table + "` (uuid VARCHAR(36) PRIMARY KEY, player VARCHAR(16), kills INT(11), deaths INT(11), streak INT(11), highest_streak INT(11));");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    //SETTER METHODS


    public void setStreak(UUID uuid, int streak) {
        if(!playerExists(uuid)) {
            plugin.log("An error whilst setting streak for uuid "+uuid+", please contact the developer about this error.");
            return;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE " + plugin.table + " SET streak=? WHERE UUID=?");
            statement.setInt(1, streak);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setKills(UUID uuid, int kills) {
        if(!playerExists(uuid)) {
            plugin.log("An error whilst setting kills for uuid "+uuid+", please contact the developer about this error.");
            return;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE " + plugin.table + " SET kills=? WHERE UUID=?");
            statement.setInt(1, kills);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setDeaths(UUID uuid, int deaths) {
        if(!playerExists(uuid)) {
            plugin.log("An error whilst setting deaths for uuid "+uuid+", please contact the developer about this error.");
            return;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE " + plugin.table + " SET deaths=? WHERE UUID=?");
            statement.setInt(1, deaths);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setHighestStreak(UUID uuid, int streak) {
        if(!playerExists(uuid)) {
            plugin.log("An error whilst setting highest streak for uuid "+uuid+", please contact the developer about this error.");
            return;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE " + plugin.table + " SET highest_streak=? WHERE UUID=?");
            statement.setInt(1, streak);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // GETTER METHODS

    public int getStreak(UUID uuid) {
        if (!playerExists(uuid)) {
            plugin.log("An error whilst getting streak for uuid " + uuid + ", please contact the developer about this error.");
            return 0;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt("streak");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getKills(UUID uuid) {
        if (!playerExists(uuid)) {
            plugin.log("An error whilst getting streak for uuid " + uuid + ", please contact the developer about this error.");
            return 0;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt("kills");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getDeaths(UUID uuid) {
        if (!playerExists(uuid)) {
            plugin.log("An error whilst getting streak for uuid " + uuid + ", please contact the developer about this error.");
            return 0;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt("deaths");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getHighestStreak(UUID uuid) {
        if (!playerExists(uuid)) {
            plugin.log("An error whilst getting streak for uuid " + uuid + ", please contact the developer about this error.");
            return 0;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt("highest_streak");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public LinkedHashMap<String, Integer> getTopKills() {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement
                    ("SELECT player, kills FROM " + plugin.table + " GROUP BY player ORDER BY `KILLS` DESC LIMIT 10000");
            ResultSet results = statement.executeQuery();
            LinkedHashMap<String, Integer> players = new LinkedHashMap<>();
            while (results != null && results.next()) {
                players.put(results.getString("player"), results.getInt("kills"));
            }
            return players;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public LinkedHashMap<String, Integer> getTopHighestStreak() {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement
                    ("SELECT player, highest_streak FROM " + plugin.table + " GROUP BY player ORDER BY `HIGHEST_STREAK` DESC LIMIT 10000");
            ResultSet results = statement.executeQuery();
            LinkedHashMap<String, Integer> players = new LinkedHashMap<>();
            while (results != null && results.next()) {
                players.put(results.getString("player"), results.getInt("highest_streak"));
            }
            return players;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public LinkedHashMap<String, Integer> getTopDeaths() {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement
                    ("SELECT player, deaths FROM " + plugin.table + " GROUP BY player ORDER BY `DEATHS` DESC LIMIT 10000");
            ResultSet results = statement.executeQuery();
            LinkedHashMap<String, Integer> players = new LinkedHashMap<>();
            while (results != null && results.next()) {
                players.put(results.getString("player"), results.getInt("deaths"));
            }
            return players;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteTable() {
        try {
            plugin.getConnection().prepareStatement("DROP TABLE IF EXISTS " + plugin.table).executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void resetData() {
        try {
            plugin.getConnection().prepareStatement("TRUNCATE TABLE " + plugin.table).executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
