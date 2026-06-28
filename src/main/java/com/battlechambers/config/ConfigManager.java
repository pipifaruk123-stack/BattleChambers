package com.battlechambers.config;

import com.battlechambers.utils.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages all configuration files for the plugin
 * Loads, reloads, and provides access to configuration data
 * 
 * Supported configurations:
 * - config.yml (Main settings)
 * - arenas.yml (Arena configurations)
 * - games.yml (Game settings)
 * - messages.yml (Language/messages)
 * - menus.yml (GUI menus)
 * - sounds.yml (Sound effects)
 * - scoreboard.yml (Scoreboard design)
 * - database.yml (Database connection)
 * - rewards.yml (Reward settings)
 * - quests.yml (Quest configurations)
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class ConfigManager {

    private final JavaPlugin plugin;
    private final Map<String, FileConfiguration> configs;
    private final File configDirectory;

    /**
     * Constructor for ConfigManager
     * @param plugin The main plugin instance
     */
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configs = new HashMap<>();
        this.configDirectory = new File(plugin.getDataFolder(), "");
        
        // Create data folder if it doesn't exist
        if (!this.configDirectory.exists()) {
            this.configDirectory.mkdirs();
        }
    }

    /**
     * Load all configuration files
     */
    public void loadAllConfigs() {
        try {
            loadConfig("config.yml");
            loadConfig("arenas.yml");
            loadConfig("games.yml");
            loadConfig("messages.yml");
            loadConfig("menus.yml");
            loadConfig("sounds.yml");
            loadConfig("scoreboard.yml");
            loadConfig("database.yml");
            loadConfig("rewards.yml");
            loadConfig("quests.yml");
            
            Logger.info("All configuration files loaded successfully!");
        } catch (Exception e) {
            Logger.error("Error loading configuration files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load a specific configuration file
     * Creates default files if they don't exist
     * @param fileName The name of the file to load
     */
    private void loadConfig(String fileName) {
        try {
            File configFile = new File(configDirectory, fileName);
            
            // Create default file if it doesn't exist
            if (!configFile.exists()) {
                createDefaultConfig(fileName, configFile);
                Logger.info("Created default configuration file: " + fileName);
            }
            
            // Load the configuration
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            configs.put(fileName, config);
            
        } catch (Exception e) {
            Logger.error("Failed to load config file: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Create default configuration files
     * @param fileName The file name
     * @param file The file object
     */
    private void createDefaultConfig(String fileName, File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            
            // Set default values based on file type
            switch (fileName) {
                case "config.yml" -> createDefaultMainConfig(config);
                case "arenas.yml" -> createDefaultArenasConfig(config);
                case "games.yml" -> createDefaultGamesConfig(config);
                case "messages.yml" -> createDefaultMessagesConfig(config);
                case "menus.yml" -> createDefaultMenusConfig(config);
                case "sounds.yml" -> createDefaultSoundsConfig(config);
                case "scoreboard.yml" -> createDefaultScoreboardConfig(config);
                case "database.yml" -> createDefaultDatabaseConfig(config);
                case "rewards.yml" -> createDefaultRewardsConfig(config);
                case "quests.yml" -> createDefaultQuestsConfig(config);
            }
            
            config.save(file);
        } catch (IOException e) {
            Logger.error("Failed to create default config: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Create default main configuration
     * @param config The FileConfiguration object
     */
    private void createDefaultMainConfig(FileConfiguration config) {
        config.set("plugin.name", "BattleChambers");
        config.set("plugin.version", "1.0.0");
        config.set("plugin.language", "en");
        
        // Game settings
        config.set("game.min-players", 2);
        config.set("game.max-players", 24);
        config.set("game.countdown-duration", 15);
        config.set("game.duration", 120);
        config.set("game.wait-for-players", true);
        config.set("game.wait-duration", 300);
        
        // Lobby settings
        config.set("lobby.enabled", true);
        config.set("lobby.teleport-on-join", true);
        config.set("lobby.protection", true);
        
        // Player settings
        config.set("player.hunger-enabled", true);
        config.set("player.damage-enabled", true);
        config.set("player.fall-damage-enabled", false);
        
        // Rewards
        config.set("rewards.enabled", true);
        config.set("rewards.give-coins", true);
        config.set("rewards.give-xp", true);
        
        // Database
        config.set("database.enabled", true);
        config.set("database.type", "sqlite");
    }

    /**
     * Create default arenas configuration
     * @param config The FileConfiguration object
     */
    private void createDefaultArenasConfig(FileConfiguration config) {
        config.set("arenas.example-arena.enabled", true);
        config.set("arenas.example-arena.display-name", "&6Example Arena");
        config.set("arenas.example-arena.world", "world");
        config.set("arenas.example-arena.min-height", 0);
        config.set("arenas.example-arena.max-height", 256);
    }

    /**
     * Create default games configuration
     * @param config The FileConfiguration object
     */
    private void createDefaultGamesConfig(FileConfiguration config) {
        // Example game configuration
        config.set("games.bow-battle.enabled", true);
        config.set("games.bow-battle.name", "Bow Battle");
        config.set("games.bow-battle.description", "Fight with bows!");
        config.set("games.bow-battle.duration", 120);
        config.set("games.bow-battle.min-players", 2);
        config.set("games.bow-battle.max-players", 24);
    }

    /**
     * Create default messages configuration
     * @param config The FileConfiguration object
     */
    private void createDefaultMessagesConfig(FileConfiguration config) {
        config.set("messages.game.start", "&a✓ &fGame started!");
        config.set("messages.game.end", "&a✓ &fGame ended!");
        config.set("messages.game.join", "&a✓ &f{player} joined the game!");
        config.set("messages.game.quit", "&a✓ &f{player} left the game!");
        config.set("messages.game.win", "&6★ &f{player} won the game!");
        config.set("messages.error.not-in-game", "&cYou are not in a game!");
        config.set("messages.error.already-in-game", "&cYou are already in a game!");
    }

    /**
     * Create default menus configuration
     * @param config The FileConfiguration object
     */
    private void createDefaultMenusConfig(FileConfiguration config) {
        config.set("menus.main.size", 27);
        config.set("menus.main.title", "&6BattleChambers");
    }

    /**
     * Create default sounds configuration
     * @param config The FileConfiguration object
     */
    private void createDefaultSoundsConfig(FileConfiguration config) {
        config.set("sounds.start", "ENTITY_ENDER_DRAGON_GROWL");
        config.set("sounds.end", "BLOCK_NOTE_BLOCK_DING");
        config.set("sounds.win", "ENTITY_PLAYER_LEVELUP");
    }

    /**
     * Create default scoreboard configuration
     * @param config The FileConfiguration object
     */
    private void createDefaultScoreboardConfig(FileConfiguration config) {
        config.set("scoreboard.lobby.title", "&6BattleChambers");
        config.set("scoreboard.game.title", "&6Game Info");
    }

    /**
     * Create default database configuration
     * @param config The FileConfiguration object
     */
    private void createDefaultDatabaseConfig(FileConfiguration config) {
        config.set("database.type", "sqlite");
        config.set("database.file", "data.db");
        config.set("database.mysql.host", "localhost");
        config.set("database.mysql.port", 3306);
        config.set("database.mysql.username", "root");
        config.set("database.mysql.password", "password");
        config.set("database.mysql.database", "battlechambers");
        config.set("database.connection-pool.size", 10);
        config.set("database.connection-pool.max-lifetime", 1800000);
    }

    /**
     * Create default rewards configuration
     * @param config The FileConfiguration object
     */
    private void createDefaultRewardsConfig(FileConfiguration config) {
        config.set("rewards.first-place.coins", 100);
        config.set("rewards.first-place.xp", 10);
        config.set("rewards.second-place.coins", 75);
        config.set("rewards.second-place.xp", 8);
        config.set("rewards.third-place.coins", 50);
        config.set("rewards.third-place.xp", 6);
    }

    /**
     * Create default quests configuration
     * @param config The FileConfiguration object
     */
    private void createDefaultQuestsConfig(FileConfiguration config) {
        config.set("quests.daily.enabled", true);
        config.set("quests.weekly.enabled", true);
    }

    /**
     * Get a configuration file
     * @param fileName The file name
     * @return The FileConfiguration object, or null if not found
     */
    public FileConfiguration getConfig(String fileName) {
        return configs.get(fileName);
    }

    /**
     * Get the main config.yml
     * @return The main FileConfiguration
     */
    public FileConfiguration getMainConfig() {
        return configs.get("config.yml");
    }

    /**
     * Get the messages.yml
     * @return The messages FileConfiguration
     */
    public FileConfiguration getMessages() {
        return configs.get("messages.yml");
    }

    /**
     * Get the database.yml
     * @return The database FileConfiguration
     */
    public FileConfiguration getDatabaseConfig() {
        return configs.get("database.yml");
    }

    /**
     * Reload all configurations
     */
    public void reloadAllConfigs() {
        Logger.info("Reloading all configuration files...");
        configs.clear();
        loadAllConfigs();
        Logger.info("Configuration files reloaded successfully!");
    }

    /**
     * Get a configuration value with a default
     * @param fileName The file name
     * @param path The configuration path
     * @param defaultValue The default value
     * @return The configuration value
     */
    public Object get(String fileName, String path, Object defaultValue) {
        FileConfiguration config = configs.get(fileName);
        if (config == null) {
            return defaultValue;
        }
        return config.get(path, defaultValue);
    }

    /**
     * Set a configuration value
     * @param fileName The file name
     * @param path The configuration path
     * @param value The value to set
     */
    public void set(String fileName, String path, Object value) {
        FileConfiguration config = configs.get(fileName);
        if (config != null) {
            config.set(path, value);
            saveConfig(fileName);
        }
    }

    /**
     * Save a configuration file
     * @param fileName The file name to save
     */
    public void saveConfig(String fileName) {
        try {
            FileConfiguration config = configs.get(fileName);
            if (config != null) {
                File configFile = new File(configDirectory, fileName);
                config.save(configFile);
            }
        } catch (IOException e) {
            Logger.error("Failed to save config file: " + fileName);
            e.printStackTrace();
        }
    }
}
