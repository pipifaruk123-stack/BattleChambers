package com.battlechambers.game;

import com.battlechambers.model.Arena;
import com.battlechambers.model.GameSession;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * Abstract base class for all mini-games
 * Every mini-game must extend this class and implement the required methods
 * 
 * This design allows for:
 * - Easy addition of new games (just extend this class)
 * - Consistent game behavior
 * - Centralized game logic
 * - Customizable game settings per game
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public abstract class AbstractMiniGame {

    protected final String gameId;
    protected final String gameName;
    protected final String gameDescription;
    protected final int minPlayers;
    protected final int maxPlayers;
    protected final int duration; // in seconds
    protected GameSession gameSession;
    protected Arena arena;
    protected boolean running;

    /**
     * Constructor for AbstractMiniGame
     * @param gameId Unique game identifier
     * @param gameName Display name
     * @param gameDescription Game description
     * @param minPlayers Minimum players required
     * @param maxPlayers Maximum players allowed
     * @param duration Game duration in seconds
     */
    public AbstractMiniGame(String gameId, String gameName, String gameDescription, 
                            int minPlayers, int maxPlayers, int duration) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameDescription = gameDescription;
        this.minPlayers = Math.max(1, minPlayers);
        this.maxPlayers = Math.max(minPlayers, maxPlayers);
        this.duration = Math.max(10, duration);
        this.running = false;
    }

    /**
     * Initialize the game with arena and players
     * Called before the game starts
     * @param arena The arena to use
     * @param players The players playing
     */
    public abstract void initialize(Arena arena, Set<Player> players);

    /**
     * Start the game
     * Called when countdown finishes
     */
    public abstract void start();

    /**
     * Handle game tick (called every 1 second)
     * @param secondsElapsed Seconds elapsed since start
     */
    public abstract void tick(int secondsElapsed);

    /**
     * End the game
     * Called when time is up or game ends prematurely
     */
    public abstract void end();

    /**
     * Handle player death
     * @param player The player who died
     * @param killer The player who killed them (can be null)
     */
    public abstract void onPlayerDeath(Player player, Player killer);

    /**
     * Handle player quit
     * @param player The player who quit
     */
    public abstract void onPlayerQuit(Player player);

    /**
     * Get the winner(s) of the game
     * @return List of winning players
     */
    public abstract List<Player> getWinners();

    /**
     * Get the leaderboard for the game
     * @return String representation of leaderboard
     */
    public abstract String getLeaderboard();

    /**
     * Check if the game can be started with current players
     * @return True if can start
     */
    public boolean canStart() {
        return gameSession != null && gameSession.getPlayerCount() >= minPlayers;
    }

    /**
     * Check if the game is running
     * @return True if running
     */
    public boolean isRunning() {
        return running;
    }

    // Getters
    public String getGameId() { return gameId; }
    public String getGameName() { return gameName; }
    public String getGameDescription() { return gameDescription; }
    public int getMinPlayers() { return minPlayers; }
    public int getMaxPlayers() { return maxPlayers; }
    public int getDuration() { return duration; }
    public GameSession getGameSession() { return gameSession; }
    public Arena getArena() { return arena; }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + gameId + '\'' +
                ", name='" + gameName + '\'' +
                ", minPlayers=" + minPlayers +
                ", maxPlayers=" + maxPlayers +
                ", duration=" + duration +
                '}';
    }
}
