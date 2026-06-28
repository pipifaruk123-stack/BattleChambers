package com.battlechambers.game;

import com.battlechambers.BattleChambers;
import com.battlechambers.games.*;
import com.battlechambers.model.Arena;
import com.battlechambers.model.GameSession;
import com.battlechambers.utils.Logger;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Manages all mini-games and game sessions
 * Handles game creation, registration, and lifecycle management
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class GameManager {

    private final BattleChambers plugin;
    private final Map<String, AbstractMiniGame> registeredGames;
    private final Map<String, GameSession> activeSessions;
    private final com.battlechambers.manager.ArenaManager arenaManager;
    private final com.battlechambers.config.ConfigManager configManager;

    /**
     * Constructor for GameManager
     * @param plugin The main plugin instance
     * @param arenaManager The arena manager
     * @param configManager The config manager
     */
    public GameManager(BattleChambers plugin, com.battlechambers.manager.ArenaManager arenaManager,
                      com.battlechambers.config.ConfigManager configManager) {
        this.plugin = plugin;
        this.registeredGames = new HashMap<>();
        this.activeSessions = Collections.synchronizedMap(new HashMap<>());
        this.arenaManager = arenaManager;
        this.configManager = configManager;
        registerDefaultGames();
    }

    /**
     * Register all default mini-games
     * Add new games here as they are created
     */
    private void registerDefaultGames() {
        try {
            // Example games - Add more as created
            registerGame(new BowBattle());
            registerGame(new SwordFight());
            registerGame(new TNTRun());
            registerGame(new BlockParty());
            registerGame(new ParkourChallenge());
            registerGame(new SumoArena());
            registerGame(new LavaEscape());
            registerGame(new SnowballFight());
            registerGame(new FishChallenge());
            registerGame(new HotPotato());
            
            Logger.info("Registered " + registeredGames.size() + " mini-games");
        } catch (Exception e) {
            Logger.error("Error registering games: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Register a mini-game
     * @param game The game to register
     */
    public void registerGame(AbstractMiniGame game) {
        if (registeredGames.containsKey(game.getGameId())) {
            Logger.warn("Game already registered: " + game.getGameId());
            return;
        }
        
        registeredGames.put(game.getGameId(), game);
        Logger.info("Registered game: " + game.getGameName());
    }

    /**
     * Create a new game session
     * @param gameId The game ID to create
     * @param arena The arena to use
     * @param players The players joining
     * @return The created GameSession or null
     */
    public GameSession createGameSession(String gameId, Arena arena, Set<Player> players) {
        AbstractMiniGame game = registeredGames.get(gameId);
        if (game == null) {
            Logger.error("Game not found: " + gameId);
            return null;
        }

        if (players.size() < game.getMinPlayers() || players.size() > game.getMaxPlayers()) {
            Logger.error("Invalid player count for game: " + gameId);
            return null;
        }

        String sessionId = UUID.randomUUID().toString();
        GameSession session = new GameSession(sessionId, gameId, arena);

        for (Player player : players) {
            session.addPlayer(player.getUniqueId());
        }

        game.initialize(arena, players);
        game.gameSession = session;
        game.arena = arena;

        activeSessions.put(sessionId, session);
        Logger.info("Created game session: " + sessionId + " for game: " + gameId);
        return session;
    }

    /**
     * Get a random registered game
     * @return A random AbstractMiniGame
     */
    public AbstractMiniGame getRandomGame() {
        if (registeredGames.isEmpty()) {
            Logger.error("No games registered!");
            return null;
        }

        List<AbstractMiniGame> games = new ArrayList<>(registeredGames.values());
        return games.get(new Random().nextInt(games.size()));
    }

    /**
     * Get a game by ID
     * @param gameId The game ID
     * @return The AbstractMiniGame or null
     */
    public AbstractMiniGame getGame(String gameId) {
        return registeredGames.get(gameId);
    }

    /**
     * Get all registered games
     * @return Collection of all games
     */
    public Collection<AbstractMiniGame> getAllGames() {
        return new ArrayList<>(registeredGames.values());
    }

    /**
     * Get an active game session by ID
     * @param sessionId The session ID
     * @return The GameSession or null
     */
    public GameSession getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    /**
     * Get all active game sessions
     * @return Collection of active sessions
     */
    public Collection<GameSession> getAllSessions() {
        return new ArrayList<>(activeSessions.values());
    }

    /**
     * Remove a game session
     * @param sessionId The session ID to remove
     */
    public void removeSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

    /**
     * Stop all active games
     */
    public void stopAllGames() {
        List<String> sessionIds = new ArrayList<>(activeSessions.keySet());
        for (String sessionId : sessionIds) {
            GameSession session = activeSessions.get(sessionId);
            if (session != null) {
                AbstractMiniGame game = registeredGames.get(session.getGameType());
                if (game != null) {
                    game.end();
                }
                activeSessions.remove(sessionId);
            }
        }
    }

    /**
     * Get the number of registered games
     * @return Number of registered games
     */
    public int getGameCount() {
        return registeredGames.size();
    }

    /**
     * Get the number of active sessions
     * @return Number of active sessions
     */
    public int getActiveSessionCount() {
        return activeSessions.size();
    }
}
