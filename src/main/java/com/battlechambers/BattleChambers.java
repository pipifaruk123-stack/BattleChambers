package com.battlechambers;

import com.battlechambers.command.BattleChambersCommand;
import com.battlechambers.config.ConfigManager;
import com.battlechambers.game.GameManager;
import com.battlechambers.listener.PlayerListener;
import com.battlechambers.listener.ServerListener;
import com.battlechambers.listener.WorldListener;
import com.battlechambers.manager.ArenaManager;
import com.battlechambers.manager.PlayerDataManager;
import com.battlechambers.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * BattleChambers - Main Plugin Class
 * A comprehensive mini-game server plugin for Minecraft
 * 
 * Features:
 * - 10+ mini-games with unique mechanics
 * - Arena management system
 * - Player progression and statistics
 * - Command system with tab completion
 * - Event-driven architecture
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class BattleChambers extends JavaPlugin {

    private static BattleChambers instance;
    
    private ConfigManager configManager;
    private ArenaManager arenaManager;
    private GameManager gameManager;
    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        instance = this;
        
        Logger.setPlugin(this);
        Logger.info("=".repeat(50));
        Logger.info("BattleChambers v" + getDescription().getVersion() + " is loading...");
        Logger.info("=".repeat(50));

        try {
            // Initialize configuration
            saveDefaultConfig();
            this.configManager = new ConfigManager(this);
            Logger.info("✓ Configuration loaded");

            // Initialize managers
            this.arenaManager = new ArenaManager(this, configManager);
            Logger.info("✓ Arena Manager initialized (" + arenaManager.getArenaCount() + " arenas)");

            this.gameManager = new GameManager(this, arenaManager, configManager);
            Logger.info("✓ Game Manager initialized (" + gameManager.getGameCount() + " games)");

            this.playerDataManager = new PlayerDataManager(this, configManager);
            Logger.info("✓ Player Data Manager initialized");

            // Register commands
            registerCommands();
            Logger.info("✓ Commands registered");

            // Register listeners
            registerListeners();
            Logger.info("✓ Event listeners registered");

            // Schedule tasks
            scheduleTasks();
            Logger.info("✓ Scheduled tasks initialized");

            Logger.info("=".repeat(50));
            Logger.info("BattleChambers v" + getDescription().getVersion() + " enabled successfully!");
            Logger.info("=".repeat(50));

            // Notify online players
            Bukkit.broadcastMessage("\n§a[BattleChambers] §lPlugin loaded! Use §e/bc help §r§afor commands.\n");

        } catch (Exception e) {
            Logger.error("Failed to enable BattleChambers!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        Logger.info("=".repeat(50));
        Logger.info("BattleChambers is disabling...");

        // Stop all active games
        if (gameManager != null) {
            gameManager.stopAllGames();
            Logger.info("✓ Stopped all active games");
        }

        // Save all player data
        if (playerDataManager != null) {
            playerDataManager.saveAllPlayers();
            Logger.info("✓ Saved all player data");
        }

        // Save configuration
        if (configManager != null) {
            configManager.saveConfig();
            Logger.info("✓ Configuration saved");
        }

        Logger.info("BattleChambers disabled.");
        Logger.info("=".repeat(50));
    }

    /**
     * Register plugin commands
     */
    private void registerCommands() {
        BattleChambersCommand mainCommand = new BattleChambersCommand(this);
        getCommand("bc").setExecutor(mainCommand);
        getCommand("bc").setTabCompleter(mainCommand);
    }

    /**
     * Register event listeners
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
        getServer().getPluginManager().registerEvents(new ServerListener(this), this);
    }

    /**
     * Schedule recurring tasks
     */
    private void scheduleTasks() {
        // Save player data every 5 minutes
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (playerDataManager != null) {
                playerDataManager.saveAllPlayers();
            }
        }, 6000L, 6000L); // 5 minutes

        // Update leaderboards every minute
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (playerDataManager != null) {
                playerDataManager.updateLeaderboards();
            }
        }, 1200L, 1200L); // 1 minute
    }

    /**
     * Get plugin instance
     * @return The plugin instance
     */
    public static BattleChambers getInstance() {
        return instance;
    }

    /**
     * Get the configuration manager
     * @return The ConfigManager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Get the arena manager
     * @return The ArenaManager
     */
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    /**
     * Get the game manager
     * @return The GameManager
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Get the player data manager
     * @return The PlayerDataManager
     */
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}
