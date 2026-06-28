package com.battlechambers.storage;

import com.battlechambers.database.DatabaseManager;
import com.battlechambers.model.PlayerData;
import com.battlechambers.utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Storage provider for persistent data management
 * Handles all database read/write operations
 * Provides async operations for non-blocking database access
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class StorageProvider {

    private final DatabaseManager databaseManager;

    /**
     * Constructor for StorageProvider
     * @param databaseManager The database manager instance
     */
    public StorageProvider(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Load player data from database
     * @param uuid The player UUID
     * @return PlayerData object or null if not found
     */
    public PlayerData loadPlayerData(UUID uuid) {
        String query = "SELECT * FROM players WHERE uuid = '" + uuid + "'";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT * FROM players WHERE uuid = ?"
             )) {
            
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                PlayerData data = new PlayerData(uuid);
                data.setUsername(rs.getString("username"));
                data.setLevel(rs.getInt("level"));
                data.setXp(rs.getLong("xp"));
                data.setCoins(rs.getLong("coins"));
                data.setWins(rs.getInt("wins"));
                data.setKills(rs.getInt("kills"));
                data.setDeaths(rs.getInt("deaths"));
                data.setGamesPlayed(rs.getInt("games_played"));
                data.setPlaytimeSeconds(rs.getLong("playtime_seconds"));
                data.setLastPlayed(rs.getTimestamp("last_played").getTime());
                
                return data;
            }
        } catch (SQLException e) {
            Logger.error("Error loading player data: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Save player data to database
     * @param data The player data to save
     * @return True if successful
     */
    public boolean savePlayerData(PlayerData data) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(
                 "SELECT uuid FROM players WHERE uuid = ?"
             )) {
            
            checkStmt.setString(1, data.getUuid().toString());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Update existing player
                return updatePlayerData(data);
            } else {
                // Insert new player
                return insertPlayerData(data);
            }
        } catch (SQLException e) {
            Logger.error("Error saving player data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Insert new player data
     * @param data The player data to insert
     * @return True if successful
     */
    private boolean insertPlayerData(PlayerData data) {
        String sql = "INSERT INTO players (uuid, username, level, xp, coins, wins, kills, deaths, games_played, playtime_seconds) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, data.getUuid().toString());
            stmt.setString(2, data.getUsername());
            stmt.setInt(3, data.getLevel());
            stmt.setLong(4, data.getXp());
            stmt.setLong(5, data.getCoins());
            stmt.setInt(6, data.getWins());
            stmt.setInt(7, data.getKills());
            stmt.setInt(8, data.getDeaths());
            stmt.setInt(9, data.getGamesPlayed());
            stmt.setLong(10, data.getPlaytimeSeconds());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.error("Error inserting player data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update existing player data
     * @param data The player data to update
     * @return True if successful
     */
    private boolean updatePlayerData(PlayerData data) {
        String sql = "UPDATE players SET username=?, level=?, xp=?, coins=?, wins=?, kills=?, deaths=?, " +
                     "games_played=?, playtime_seconds=?, last_played=CURRENT_TIMESTAMP WHERE uuid=?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, data.getUsername());
            stmt.setInt(2, data.getLevel());
            stmt.setLong(3, data.getXp());
            stmt.setLong(4, data.getCoins());
            stmt.setInt(5, data.getWins());
            stmt.setInt(6, data.getKills());
            stmt.setInt(7, data.getDeaths());
            stmt.setInt(8, data.getGamesPlayed());
            stmt.setLong(9, data.getPlaytimeSeconds());
            stmt.setString(10, data.getUuid().toString());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.error("Error updating player data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get top players by coins
     * @param limit The number of players to fetch
     * @return List of PlayerData objects
     */
    public List<PlayerData> getTopPlayersByCoins(int limit) {
        List<PlayerData> players = new ArrayList<>();
        String sql = "SELECT * FROM players ORDER BY coins DESC LIMIT ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                PlayerData data = new PlayerData(UUID.fromString(rs.getString("uuid")));
                data.setUsername(rs.getString("username"));
                data.setCoins(rs.getLong("coins"));
                players.add(data);
            }
        } catch (SQLException e) {
            Logger.error("Error fetching top players: " + e.getMessage());
        }
        
        return players;
    }

    /**
     * Get top players by wins
     * @param limit The number of players to fetch
     * @return List of PlayerData objects
     */
    public List<PlayerData> getTopPlayersByWins(int limit) {
        List<PlayerData> players = new ArrayList<>();
        String sql = "SELECT * FROM players ORDER BY wins DESC LIMIT ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                PlayerData data = new PlayerData(UUID.fromString(rs.getString("uuid")));
                data.setUsername(rs.getString("username"));
                data.setWins(rs.getInt("wins"));
                players.add(data);
            }
        } catch (SQLException e) {
            Logger.error("Error fetching top players by wins: " + e.getMessage());
        }
        
        return players;
    }

    /**
     * Get top players by kills
     * @param limit The number of players to fetch
     * @return List of PlayerData objects
     */
    public List<PlayerData> getTopPlayersByKills(int limit) {
        List<PlayerData> players = new ArrayList<>();
        String sql = "SELECT * FROM players ORDER BY kills DESC LIMIT ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                PlayerData data = new PlayerData(UUID.fromString(rs.getString("uuid")));
                data.setUsername(rs.getString("username"));
                data.setKills(rs.getInt("kills"));
                players.add(data);
            }
        } catch (SQLException e) {
            Logger.error("Error fetching top players by kills: " + e.getMessage());
        }
        
        return players;
    }
}
