package com.battlechambers.manager;

import com.battlechambers.BattleChambers;
import com.battlechambers.config.ConfigManager;
import com.battlechambers.model.Arena;
import com.battlechambers.utils.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * Manages all arenas in the plugin
 * Handles arena creation, loading, and management
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class ArenaManager {

    private final BattleChambers plugin;
    private final ConfigManager configManager;
    private final Map<String, Arena> arenas;

    /**
     * Constructor for ArenaManager
     * @param plugin The main plugin instance
     * @param configManager The configuration manager
     */
    public ArenaManager(BattleChambers plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.arenas = new HashMap<>();
        loadArenas();
    }

    /**
     * Load all arenas from configuration
     */
    public void loadArenas() {
        try {
            FileConfiguration config = configManager.getConfig("arenas.yml");
            if (config == null) {
                Logger.warn("Arenas configuration not found");
                return;
            }

            ConfigurationSection arenasSection = config.getConfigurationSection("arenas");
            if (arenasSection == null) {
                Logger.warn("No arenas configured");
                return;
            }

            int loadedCount = 0;
            for (String arenaName : arenasSection.getKeys(false)) {
                ConfigurationSection arenaSection = arenasSection.getConfigurationSection(arenaName);
                if (arenaSection != null) {
                    Arena arena = loadArena(arenaName, arenaSection);
                    if (arena != null) {
                        arenas.put(arenaName, arena);
                        loadedCount++;
                    }
                }
            }

            Logger.info("Loaded " + loadedCount + " arena(s)");
        } catch (Exception e) {
            Logger.error("Error loading arenas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load a single arena from configuration
     * @param arenaName The arena name
     * @param section The configuration section
     * @return The loaded Arena or null
     */
    private Arena loadArena(String arenaName, ConfigurationSection section) {
        try {
            Arena arena = new Arena(arenaName);
            
            // Load basic properties
            if (section.contains("display-name")) {
                arena.setDisplayName(section.getString("display-name"));
            }
            
            arena.setEnabled(section.getBoolean("enabled", true));
            arena.setMaxPlayers(section.getInt("max-players", 24));
            
            // Load lobby spawn
            if (section.contains("lobby-spawn")) {
                Location lobbySpawn = loadLocation(section.getConfigurationSection("lobby-spawn"));
                if (lobbySpawn != null) {
                    arena.setLobbySpawn(lobbySpawn);
                }
            }
            
            // Load spectator spawn
            if (section.contains("spectator-spawn")) {
                Location spectatorSpawn = loadLocation(section.getConfigurationSection("spectator-spawn"));
                if (spectatorSpawn != null) {
                    arena.setSpectatorSpawn(spectatorSpawn);
                }
            }
            
            // Load arena boundaries
            if (section.contains("min-location")) {
                Location minLoc = loadLocation(section.getConfigurationSection("min-location"));
                if (minLoc != null) {
                    arena.setMinLocation(minLoc);
                }
            }
            
            if (section.contains("max-location")) {
                Location maxLoc = loadLocation(section.getConfigurationSection("max-location"));
                if (maxLoc != null) {
                    arena.setMaxLocation(maxLoc);
                }
            }
            
            Logger.info("Loaded arena: " + arenaName);
            return arena;
        } catch (Exception e) {
            Logger.error("Error loading arena " + arenaName + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Load a location from configuration section
     * @param section The location section
     * @return The loaded Location or null
     */
    private Location loadLocation(ConfigurationSection section) {
        if (section == null) return null;
        
        try {
            String worldName = section.getString("world");
            if (worldName == null) return null;
            
            World world = plugin.getServer().getWorld(worldName);
            if (world == null) {
                Logger.warn("World not found: " + worldName);
                return null;
            }
            
            double x = section.getDouble("x", 0);
            double y = section.getDouble("y", 0);
            double z = section.getDouble("z", 0);
            float yaw = (float) section.getDouble("yaw", 0);
            float pitch = (float) section.getDouble("pitch", 0);
            
            return new Location(world, x, y, z, yaw, pitch);
        } catch (Exception e) {
            Logger.error("Error loading location: " + e.getMessage());
            return null;
        }
    }

    /**
     * Save a location to configuration section
     * @param section The configuration section
     * @param location The location to save
     */
    private void saveLocation(ConfigurationSection section, Location location) {
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
    }

    /**
     * Create a new arena
     * @param name The arena name
     * @return The created Arena
     */
    public Arena createArena(String name) {
        if (arenas.containsKey(name)) {
            Logger.warn("Arena already exists: " + name);
            return null;
        }
        
        Arena arena = new Arena(name);
        arenas.put(name, arena);
        saveArena(name, arena);
        Logger.info("Created arena: " + name);
        return arena;
    }

    /**
     * Delete an arena
     * @param name The arena name
     * @return True if successful
     */
    public boolean deleteArena(String name) {
        if (!arenas.containsKey(name)) {
            Logger.warn("Arena not found: " + name);
            return false;
        }
        
        arenas.remove(name);
        
        // Remove from configuration
        FileConfiguration config = configManager.getConfig("arenas.yml");
        if (config != null) {
            config.set("arenas." + name, null);
            configManager.saveConfig("arenas.yml");
        }
        
        Logger.info("Deleted arena: " + name);
        return true;
    }

    /**
     * Get an arena by name
     * @param name The arena name
     * @return The Arena or null if not found
     */
    public Arena getArena(String name) {
        return arenas.get(name);
    }

    /**
     * Get all arenas
     * @return Collection of all arenas
     */
    public Collection<Arena> getAllArenas() {
        return new ArrayList<>(arenas.values());
    }

    /**
     * Get enabled arenas
     * @return Collection of enabled arenas
     */
    public Collection<Arena> getEnabledArenas() {
        List<Arena> enabled = new ArrayList<>();
        for (Arena arena : arenas.values()) {
            if (arena.isEnabled()) {
                enabled.add(arena);
            }
        }
        return enabled;
    }

    /**
     * Get a random arena
     * @return A random Arena or null if none available
     */
    public Arena getRandomArena() {
        Collection<Arena> enabledArenas = getEnabledArenas();
        if (enabledArenas.isEmpty()) {
            return null;
        }
        
        List<Arena> arenaList = new ArrayList<>(enabledArenas);
        return arenaList.get(new Random().nextInt(arenaList.size()));
    }

    /**
     * Save an arena to configuration
     * @param name The arena name
     * @param arena The arena to save
     */
    public void saveArena(String name, Arena arena) {
        try {
            FileConfiguration config = configManager.getConfig("arenas.yml");
            if (config == null) return;
            
            String prefix = "arenas." + name + ".";
            config.set(prefix + "display-name", arena.getDisplayName());
            config.set(prefix + "enabled", arena.isEnabled());
            config.set(prefix + "max-players", arena.getMaxPlayers());
            
            if (arena.getLobbySpawn() != null) {
                saveLocation(config.createSection(prefix + "lobby-spawn"), arena.getLobbySpawn());
            }
            
            if (arena.getSpectatorSpawn() != null) {
                saveLocation(config.createSection(prefix + "spectator-spawn"), arena.getSpectatorSpawn());
            }
            
            if (arena.getMinLocation() != null) {
                saveLocation(config.createSection(prefix + "min-location"), arena.getMinLocation());
            }
            
            if (arena.getMaxLocation() != null) {
                saveLocation(config.createSection(prefix + "max-location"), arena.getMaxLocation());
            }
            
            configManager.saveConfig("arenas.yml");
            Logger.info("Saved arena: " + name);
        } catch (Exception e) {
            Logger.error("Error saving arena: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get the number of arenas
     * @return Number of arenas
     */
    public int getArenaCount() {
        return arenas.size();
    }
}
