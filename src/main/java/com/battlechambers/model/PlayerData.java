package com.battlechambers.model;

import java.util.UUID;

/**
 * Player data model representing a BattleChambers player
 * Stores all player statistics and progress
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class PlayerData {

    private final UUID uuid;
    private String username;
    private int level;
    private long xp;
    private long coins;
    private int wins;
    private int kills;
    private int deaths;
    private int gamesPlayed;
    private long playtimeSeconds;
    private long joinedAt;
    private long lastPlayed;

    /**
     * Constructor for PlayerData
     * @param uuid The player's UUID
     */
    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.level = 1;
        this.xp = 0;
        this.coins = 0;
        this.wins = 0;
        this.kills = 0;
        this.deaths = 0;
        this.gamesPlayed = 0;
        this.playtimeSeconds = 0;
        this.joinedAt = System.currentTimeMillis();
        this.lastPlayed = System.currentTimeMillis();
    }

    // Getters
    public UUID getUuid() { return uuid; }
    public String getUsername() { return username; }
    public int getLevel() { return level; }
    public long getXp() { return xp; }
    public long getCoins() { return coins; }
    public int getWins() { return wins; }
    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }
    public int getGamesPlayed() { return gamesPlayed; }
    public long getPlaytimeSeconds() { return playtimeSeconds; }
    public long getJoinedAt() { return joinedAt; }
    public long getLastPlayed() { return lastPlayed; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setLevel(int level) { this.level = Math.max(1, level); }
    public void setXp(long xp) { this.xp = Math.max(0, xp); }
    public void setCoins(long coins) { this.coins = Math.max(0, coins); }
    public void setWins(int wins) { this.wins = Math.max(0, wins); }
    public void setKills(int kills) { this.kills = Math.max(0, kills); }
    public void setDeaths(int deaths) { this.deaths = Math.max(0, deaths); }
    public void setGamesPlayed(int gamesPlayed) { this.gamesPlayed = Math.max(0, gamesPlayed); }
    public void setPlaytimeSeconds(long playtimeSeconds) { this.playtimeSeconds = Math.max(0, playtimeSeconds); }
    public void setLastPlayed(long lastPlayed) { this.lastPlayed = lastPlayed; }

    // Actions
    public void addXp(long amount) { this.xp += Math.max(0, amount); }
    public void removeXp(long amount) { this.xp = Math.max(0, this.xp - amount); }
    public void addCoins(long amount) { this.coins += Math.max(0, amount); }
    public void removeCoins(long amount) { this.coins = Math.max(0, this.coins - amount); }
    public void addKill() { this.kills++; }
    public void addDeath() { this.deaths++; }
    public void addWin() { this.wins++; }
    public void addGamePlayed() { this.gamesPlayed++; }
    public void addPlaytime(long seconds) { this.playtimeSeconds += Math.max(0, seconds); }

    /**
     * Get the kill/death ratio
     * @return The KDR
     */
    public double getKDR() {
        return deaths == 0 ? kills : (double) kills / deaths;
    }

    /**
     * Get the win rate percentage
     * @return The win rate
     */
    public double getWinRate() {
        return gamesPlayed == 0 ? 0 : (double) wins / gamesPlayed * 100;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", level=" + level +
                ", coins=" + coins +
                ", wins=" + wins +
                ", kills=" + kills +
                ", deaths=" + deaths +
                '}' ;
    }
}
