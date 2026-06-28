package com.battlechambers.model;

import org.bukkit.Location;

/**
 * Arena data model representing a BattleChambers arena
 * Contains arena configuration and spawn locations
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class Arena {

    private final String name;
    private String displayName;
    private Location lobbySpawn;
    private Location spectatorSpawn;
    private Location minLocation; // Min boundary
    private Location maxLocation; // Max boundary
    private boolean enabled;
    private int maxPlayers;

    /**
     * Constructor for Arena
     * @param name The arena name
     */
    public Arena(String name) {
        this.name = name;
        this.displayName = name;
        this.enabled = true;
        this.maxPlayers = 24;
    }

    // Getters
    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public Location getLobbySpawn() { return lobbySpawn; }
    public Location getSpectatorSpawn() { return spectatorSpawn; }
    public Location getMinLocation() { return minLocation; }
    public Location getMaxLocation() { return maxLocation; }
    public boolean isEnabled() { return enabled; }
    public int getMaxPlayers() { return maxPlayers; }

    // Setters
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setLobbySpawn(Location lobbySpawn) { this.lobbySpawn = lobbySpawn; }
    public void setSpectatorSpawn(Location spectatorSpawn) { this.spectatorSpawn = spectatorSpawn; }
    public void setMinLocation(Location minLocation) { this.minLocation = minLocation; }
    public void setMaxLocation(Location maxLocation) { this.maxLocation = maxLocation; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = Math.max(1, maxPlayers); }

    /**
     * Check if a location is within arena boundaries
     * @param location The location to check
     * @return True if within boundaries
     */
    public boolean isWithinBounds(Location location) {
        if (minLocation == null || maxLocation == null || location == null) {
            return false;
        }

        if (!minLocation.getWorld().equals(location.getWorld())) {
            return false;
        }

        double minX = Math.min(minLocation.getX(), maxLocation.getX());
        double minY = Math.min(minLocation.getY(), maxLocation.getY());
        double minZ = Math.min(minLocation.getZ(), maxLocation.getZ());
        double maxX = Math.max(minLocation.getX(), maxLocation.getX());
        double maxY = Math.max(minLocation.getY(), maxLocation.getY());
        double maxZ = Math.max(minLocation.getZ(), maxLocation.getZ());

        return location.getX() >= minX && location.getX() <= maxX &&
               location.getY() >= minY && location.getY() <= maxY &&
               location.getZ() >= minZ && location.getZ() <= maxZ;
    }

    @Override
    public String toString() {
        return "Arena{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", enabled=" + enabled +
                ", maxPlayers=" + maxPlayers +
                '}';
    }
}
