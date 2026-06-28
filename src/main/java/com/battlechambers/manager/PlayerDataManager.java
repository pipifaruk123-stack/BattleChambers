package com.battlechambers.manager;

import com.battlechambers.BattleChambers;
import com.battlechambers.model.PlayerData;
import com.battlechambers.storage.StorageProvider;
import com.battlechambers.utils.Logger;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Manages player data and statistics
 * Handles loading, caching, and saving player information
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class PlayerDataManager {

    private final BattleChambers plugin;
    private final StorageProvider storageProvider;
    private final Map<UUID, PlayerData> cachedPlayers;

    /**
     * Constructor for PlayerDataManager
     * @param plugin The main plugin instance
     * @param storageProvider The storage provider
     */
    public PlayerDataManager(BattleChambers plugin, StorageProvider storageProvider) {
        this.plugin = plugin;
        this.storageProvider = storageProvider;
        this.cachedPlayers = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Load player data (async)
     * @param player The player to load
     */
    public void loadPlayer(Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                UUID uuid = player.getUniqueId();
                PlayerData data = storageProvider.loadPlayerData(uuid);
                
                if (data == null) {
                    // Create new player data
                    data = new PlayerData(uuid);
                    data.setUsername(player.getName());
                    storageProvider.savePlayerData(data);
                }
                
                cachedPlayers.put(uuid, data);
                Logger.debug("Loaded player: " + player.getName());
            } catch (Exception e) {
                Logger.error("Error loading player: " + e.getMessage());
            }
        });
    }

    /**
     * Save player data (async)
     * @param uuid The player UUID
     */
    public void savePlayer(UUID uuid) {
        PlayerData data = cachedPlayers.get(uuid);
        if (data == null) return;
        
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                storageProvider.savePlayerData(data);
                Logger.debug("Saved player: " + data.getUsername());
            } catch (Exception e) {
                Logger.error("Error saving player: " + e.getMessage());
            }
        });
    }

    /**
     * Get player data from cache
     * @param uuid The player UUID
     * @return PlayerData or null if not found
     */
    public PlayerData getPlayerData(UUID uuid) {
        return cachedPlayers.get(uuid);
    }

    /**
     * Get or create player data
     * @param player The player
     * @return PlayerData
     */
    public PlayerData getOrCreatePlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerData data = cachedPlayers.get(uuid);
        
        if (data == null) {
            data = new PlayerData(uuid);
            data.setUsername(player.getName());
            cachedPlayers.put(uuid, data);
        }
        
        return data;
    }

    /**
     * Add coins to a player
     * @param uuid The player UUID
     * @param amount The amount to add
     */
    public void addCoins(UUID uuid, long amount) {
        PlayerData data = cachedPlayers.get(uuid);
        if (data != null) {
            data.addCoins(amount);
            savePlayer(uuid);
        }
    }

    /**
     * Remove coins from a player
     * @param uuid The player UUID
     * @param amount The amount to remove
     */
    public void removeCoins(UUID uuid, long amount) {
        PlayerData data = cachedPlayers.get(uuid);
        if (data != null) {
            data.removeCoins(amount);
            savePlayer(uuid);
        }
    }

    /**
     * Add XP to a player
     * @param uuid The player UUID
     * @param amount The amount to add
     */
    public void addXp(UUID uuid, long amount) {
        PlayerData data = cachedPlayers.get(uuid);
        if (data != null) {
            data.addXp(amount);
            checkLevelUp(uuid, data);
            savePlayer(uuid);
        }
    }

    /**
     * Remove XP from a player
     * @param uuid The player UUID
     * @param amount The amount to remove
     */
    public void removeXp(UUID uuid, long amount) {
        PlayerData data = cachedPlayers.get(uuid);
        if (data != null) {
            data.removeXp(amount);
            savePlayer(uuid);
        }
    }

    /**
     * Check if player should level up
     * XP formula: 100 * level
     * @param uuid The player UUID
     * @param data The player data
     */
    private void checkLevelUp(UUID uuid, PlayerData data) {
        long xpRequired = (long) 100 * data.getLevel();
        
        if (data.getXp() >= xpRequired) {
            data.setLevel(data.getLevel() + 1);
            data.setXp(data.getXp() - xpRequired);
            
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null) {
                player.sendTitle(
                    "§6✨ Level Up!",
                    "§fLevel " + data.getLevel(),
                    10, 70, 20
                );
                player.playSound(player.getLocation(), "ENTITY_PLAYER_LEVELUP", 1, 1);
            }
            
            // Check for another level up
            checkLevelUp(uuid, data);
        }
    }

    /**
     * Disconnect a player
     * @param uuid The player UUID
     */
    public void disconnectPlayer(UUID uuid) {
        PlayerData data = cachedPlayers.get(uuid);
        if (data != null) {
            savePlayer(uuid);
            cachedPlayers.remove(uuid);
        }
    }

    /**
     * Disconnect all players
     */
    public void disconnectAllPlayers() {
        List<UUID> uuids = new ArrayList<>(cachedPlayers.keySet());
        for (UUID uuid : uuids) {
            disconnectPlayer(uuid);
        }
    }

    /**
     * Get top players by coins
     * @param limit The number of players
     * @return List of PlayerData
     */
    public List<PlayerData> getTopPlayersByCoins(int limit) {
        return storageProvider.getTopPlayersByCoins(limit);
    }

    /**
     * Get top players by wins
     * @param limit The number of players
     * @return List of PlayerData
     */
    public List<PlayerData> getTopPlayersByWins(int limit) {
        return storageProvider.getTopPlayersByWins(limit);
    }

    /**
     * Get top players by kills
     * @param limit The number of players
     * @return List of PlayerData
     */
    public List<PlayerData> getTopPlayersByKills(int limit) {
        return storageProvider.getTopPlayersByKills(limit);
    }

    /**
     * Get the number of cached players
     * @return Number of players
     */
    public int getCachedPlayerCount() {
        return cachedPlayers.size();
    }
}
