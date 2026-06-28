package com.battlechambers.game;

import com.battlechambers.model.GameSession;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Game engine for managing game lifecycle
 * Handles countdown, game ticks, and state transitions
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class GameEngine {

    private final AbstractMiniGame game;
    private final GameSession session;
    private int countdownSeconds;
    private int gameSeconds;
    private boolean isRunning;
    private Set<Player> currentPlayers;

    /**
     * Constructor for GameEngine
     * @param game The game to run
     * @param session The game session
     */
    public GameEngine(AbstractMiniGame game, GameSession session) {
        this.game = game;
        this.session = session;
        this.countdownSeconds = 15; // Default countdown
        this.gameSeconds = 0;
        this.isRunning = false;
        this.currentPlayers = new HashSet<>();
    }

    /**
     * Start the countdown
     */
    public void startCountdown() {
        session.setState(GameSession.GameState.COUNTDOWN);
    }

    /**
     * Handle countdown tick
     * Called every second during countdown
     * @return True if countdown finished
     */
    public boolean countdownTick() {
        countdownSeconds--;
        if (countdownSeconds <= 0) {
            startGame();
            return true;
        }
        return false;
    }

    /**
     * Start the game
     */
    public void startGame() {
        isRunning = true;
        session.start();
        game.running = true;
        game.start();
    }

    /**
     * Handle game tick
     * Called every second during the game
     * @return True if game should end
     */
    public boolean gameTick() {
        if (!isRunning) return false;
        
        gameSeconds++;
        game.tick(gameSeconds);
        
        // Check if game duration exceeded
        return gameSeconds >= game.getDuration();
    }

    /**
     * End the game
     */
    public void endGame() {
        isRunning = false;
        session.end();
        game.running = false;
        game.end();
    }

    /**
     * Get countdown remaining
     * @return Seconds remaining in countdown
     */
    public int getCountdownRemaining() {
        return Math.max(0, countdownSeconds);
    }

    /**
     * Get game time elapsed
     * @return Seconds elapsed in game
     */
    public int getGameTimeElapsed() {
        return gameSeconds;
    }

    /**
     * Get game time remaining
     * @return Seconds remaining in game
     */
    public int getGameTimeRemaining() {
        return Math.max(0, game.getDuration() - gameSeconds);
    }

    /**
     * Check if engine is running
     * @return True if running
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Check if countdown is active
     * @return True if counting down
     */
    public boolean isCountingDown() {
        return session.getState() == GameSession.GameState.COUNTDOWN;
    }
}
