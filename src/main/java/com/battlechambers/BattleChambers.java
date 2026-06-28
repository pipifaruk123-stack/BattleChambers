package com.battlechambers;

import com.battlechambers.api.BattleChambersAPI;
import com.battlechambers.command.CommandManager;
import com.battlechambers.config.ConfigManager;
import com.battlechambers.database.DatabaseManager;
import com.battlechambers.game.GameManager;
import com.battlechambers.listener.GameListeners;
import com.battlechambers.manager.ArenaManager;
import com.battlechambers.manager.PlayerDataManager;
import com.battlechambers.npc.NPCManager;
import com.battlechambers.reward.RewardManager;
import com.battlechambers.scheduler.GameScheduler;
import com.battlechambers.storage.StorageProvider;
import com.battlechambers.utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for BattleChambers
 * 
 * Responsible for:
 * - Initializing all managers and systems
 * - Loading configuration files
 * - Setting up database connections
 * - Registering listeners and commands
 * - Managing plugin lifecycle
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class BattleChambers extends JavaPlugin {

    // Static instance for API access
    private static BattleChambers instance;

    // Core managers
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private StorageProvider storageProvider;
    private ArenaManager arenaManager;
    private GameManager gameManager;
    private PlayerDataManager playerDataManager;
    private RewardManager rewardManager;
    private GameScheduler gameScheduler;
    private CommandManager commandManager;
    private NPCManager npcManager;
    private GameListeners gameListeners;

    /**
     * Called when the plugin is enabled
     * Initializes all systems and managers
     */
    @Override
    public void onEnable() {
        instance = this;
        
        try {
            // Log startup
            Logger.info("§6================================================");
            Logger.info("§6  BattleChambers v" + getDescription().getVersion());
            Logger.info("§6  Starting initialization...");
            Logger.info("§6================================================");

            // Step 1: Load configuration files
            Logger.info("§7[1/8] Loading configuration files...");
            this.configManager = new ConfigManager(this);
            this.configManager.loadAllConfigs();
            Logger.info("§aConfiguration files loaded successfully!");

            // Step 2: Initialize database
            Logger.info("§7[2/8] Initializing database connection...");
            this.databaseManager = new DatabaseManager(this, this.configManager);
            this.databaseManager.connect();
            Logger.info("§aDatabase connected successfully!");

            // Step 3: Initialize storage provider
            Logger.info("§7[3/8] Initializing storage provider...");
            this.storageProvider = new StorageProvider(this.databaseManager);
            Logger.info("§aStorage provider initialized!");

            // Step 4: Initialize arena manager
            Logger.info("§7[4/8] Initializing arena manager...");
            this.arenaManager = new ArenaManager(this, this.configManager);
            Logger.info("§aArena manager initialized!");

            // Step 5: Initialize game manager
            Logger.info("§7[5/8] Initializing game manager...");
            this.gameManager = new GameManager(this, this.arenaManager, this.configManager);
            Logger.info("§aGame manager initialized!");

            // Step 6: Initialize player data manager
            Logger.info("§7[6/8] Initializing player data manager...");
            this.playerDataManager = new PlayerDataManager(this, this.storageProvider);
            Logger.info("§aPlayer data manager initialized!");

            // Step 7: Initialize reward manager
            Logger.info("§7[7/8] Initializing reward manager...");
            this.rewardManager = new RewardManager(this, this.configManager, this.storageProvider);
            Logger.info("§aReward manager initialized!");

            // Step 8: Register listeners and commands
            Logger.info("§7[8/8] Registering listeners and commands...");
            this.gameScheduler = new GameScheduler(this, this.gameManager);
            this.commandManager = new CommandManager(this);
            this.gameListeners = new GameListeners(this);
            this.npcManager = new NPCManager(this);
            Logger.info("§aListeners and commands registered!");

            // Final startup message
            Logger.info("§6================================================");
            Logger.info("§a✓ BattleChambers enabled successfully!");
            Logger.info("§7Use §f/bc help §7for available commands");
            Logger.info("§6================================================");

        } catch (Exception e) {
            Logger.error("Failed to initialize BattleChambers!");
            Logger.error(e.getMessage());
            e.printStackTrace();
            // Disable plugin on initialization failure
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    /**
     * Called when the plugin is disabled
     * Cleans up all resources and connections
     */
    @Override
    public void onDisable() {
        try {
            Logger.info("§6================================================");
            Logger.info("§6  BattleChambers is shutting down...");
            Logger.info("§6================================================");

            // Stop all running games
            if (this.gameManager != null) {
                this.gameManager.stopAllGames();
                Logger.info("§7All games stopped");
            }

            // Disconnect players
            if (this.playerDataManager != null) {
                this.playerDataManager.disconnectAllPlayers();
                Logger.info("§7All players disconnected");
            }

            // Close database connection
            if (this.databaseManager != null) {
                this.databaseManager.disconnect();
                Logger.info("§7Database connection closed");
            }

            // Unregister listeners
            org.bukkit.plugin.PluginManager pm = getServer().getPluginManager();
            org.bukkit.event.HandlerList.unregisterAll(this);
            Logger.info("§7Listeners unregistered");

            Logger.info("§a✓ BattleChambers disabled successfully!");
            Logger.info("§6================================================");

        } catch (Exception e) {
            Logger.error("Error during plugin shutdown:");
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get the plugin instance (Singleton for convenience)
     * @return Plugin instance
     */
    public static BattleChambers getInstance() {
        return instance;
    }

    /**
     * Get the configuration manager
     * @return ConfigManager instance
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Get the database manager
     * @return DatabaseManager instance
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * Get the storage provider
     * @return StorageProvider instance
     */
    public StorageProvider getStorageProvider() {
        return storageProvider;
    }

    /**
     * Get the arena manager
     * @return ArenaManager instance
     */
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    /**
     * Get the game manager
     * @return GameManager instance
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Get the player data manager
     * @return PlayerDataManager instance
     */
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    /**
     * Get the reward manager
     * @return RewardManager instance
     */
    public RewardManager getRewardManager() {
        return rewardManager;
    }

    /**
     * Get the game scheduler
     * @return GameScheduler instance
     */
    public GameScheduler getGameScheduler() {
        return gameScheduler;
    }

    /**
     * Get the command manager
     * @return CommandManager instance
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Get the NPC manager
     * @return NPCManager instance
     */
    public NPCManager getNPCManager() {
        return npcManager;
    }

    /**
     * Get the BattleChambers API
     * @return BattleChambersAPI instance
     */
    public BattleChambersAPI getAPI() {
        return new BattleChambersAPI(this);
    }
}
