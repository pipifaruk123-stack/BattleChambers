package com.battlechambers.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Game session model representing an active game
 * Tracks all players, scores, and game state
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class GameSession {

    private final String gameId;
    private final String gameType;
    private final Arena arena;
    private final Map<UUID, Integer> playerScores;
    private final Map<UUID, Integer> playerKills;
    private final Map<UUID, Integer> playerDeaths;
    private long startTime;
    private long endTime;
    private GameState state;
    private boolean started;
    private boolean ended;

    /**
     * Enum for game states
     */
    public enum GameState {
        WAITING, COUNTDOWN, RUNNING, FINISHING, ENDED
    }

    /**
     * Constructor for GameSession
     * @param gameId The unique game ID
     * @param gameType The type of game
     * @param arena The arena being used
     */
    public GameSession(String gameId, String gameType, Arena arena) {
        this.gameId = gameId;
        this.gameType = gameType;
        this.arena = arena;
        this.playerScores = new HashMap<>();
        this.playerKills = new HashMap<>();
        this.playerDeaths = new HashMap<>();
        this.state = GameState.WAITING;
        this.started = false;
        this.ended = false;
    }

    // Getters
    public String getGameId() { return gameId; }
    public String getGameType() { return gameType; }
    public Arena getArena() { return arena; }
    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }
    public GameState getState() { return state; }
    public boolean isStarted() { return started; }
    public boolean isEnded() { return ended; }
    public Map<UUID, Integer> getPlayerScores() { return playerScores; }
    public Map<UUID, Integer> getPlayerKills() { return playerKills; }
    public Map<UUID, Integer> getPlayerDeaths() { return playerDeaths; }

    // Setters
    public void setState(GameState state) { this.state = state; }

    // Actions
    public void start() {
        this.started = true;
        this.startTime = System.currentTimeMillis();
        this.state = GameState.RUNNING;
    }

    public void end() {
        this.ended = true;
        this.endTime = System.currentTimeMillis();
        this.state = GameState.ENDED;
    }

    public void addPlayer(UUID uuid) {
        playerScores.putIfAbsent(uuid, 0);
        playerKills.putIfAbsent(uuid, 0);
        playerDeaths.putIfAbsent(uuid, 0);
    }

    public void removePlayer(UUID uuid) {
        playerScores.remove(uuid);
        playerKills.remove(uuid);
        playerDeaths.remove(uuid);
    }

    public void addScore(UUID uuid, int score) {
        playerScores.put(uuid, playerScores.getOrDefault(uuid, 0) + Math.max(0, score));
    }

    public void addKill(UUID uuid) {
        playerKills.put(uuid, playerKills.getOrDefault(uuid, 0) + 1);
    }

    public void addDeath(UUID uuid) {
        playerDeaths.put(uuid, playerDeaths.getOrDefault(uuid, 0) + 1);
    }

    public int getScore(UUID uuid) {
        return playerScores.getOrDefault(uuid, 0);
    }

    public int getKills(UUID uuid) {
        return playerKills.getOrDefault(uuid, 0);
    }

    public int getDeaths(UUID uuid) {
        return playerDeaths.getOrDefault(uuid, 0);
    }

    public int getPlayerCount() {
        return playerScores.size();
    }

    /**
     * Get the duration of the game in seconds
     * @return Duration in seconds
     */
    public long getDurationSeconds() {
        if (!started) return 0;
        long end = ended ? endTime : System.currentTimeMillis();
        return (end - startTime) / 1000;
    }

    /**
     * Get the top scorer
     * @return UUID of top scorer, or null if no players
     */
    public UUID getTopScorer() {
        return playerScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "gameId='" + gameId + '\'' +
                ", gameType='" + gameType + '\'' +
                ", state=" + state +
                ", players=" + playerScores.size() +
                '}' ;
    }
}
